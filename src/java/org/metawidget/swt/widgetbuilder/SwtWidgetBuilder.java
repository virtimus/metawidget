// Metawidget
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

package org.metawidget.swt.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.metawidget.swing.widgetprocessor.binding.BindingConverter;
import org.metawidget.swt.Stub;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * WidgetBuilder for SWT environments.
 * <p>
 * Creates native SWT <code>Controls</code>, such as <code>Text</code> and
 * <code>Combo</code>, to suit the inspected fields.
 *
 * @author Richard Kennard
 */

public class SwtWidgetBuilder
	implements WidgetBuilder<Control, SwtMetawidget>
{
	//
	// Public methods
	//

	public Control buildWidget( String elementName, Map<String, String> attributes, SwtMetawidget metawidget )
	{
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return new Stub( metawidget, SWT.None );

		// Action

		if ( ACTION.equals( elementName ) )
		{
			Button button = new Button( metawidget, SWT.None );
			button.setText( metawidget.getLabelString( attributes ) );

			return button;
		}

		String type = WidgetBuilderUtils.getActualClassOrType( attributes );

		// If no type, assume a String

		if ( type == null )
			type = String.class.getName();

		// Lookup the Class

		Class<?> clazz = ClassUtils.niceForName( type );

		// Support mandatory Booleans (can be rendered as a checkbox, even though they have a
		// Lookup)

		if ( Boolean.class.equals( clazz ) && TRUE.equals( attributes.get( REQUIRED ) ) )
			return new Button( metawidget, SWT.CHECK );

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
		{
			Combo comboDropDown = new Combo( metawidget, SWT.READ_ONLY );

			// Add an empty choice (if nullable, and not required)

			if ( WidgetBuilderUtils.needsEmptyLookupItem( attributes ) )
				comboDropDown.add( "" );

			List<String> values = CollectionUtils.fromString( lookup );
			BindingConverter converter = metawidget.getWidgetProcessor( BindingConverter.class );

			for ( String value : values )
			{
				// Convert (if supported)

				Object convertedValue;

				if ( converter == null )
					convertedValue = value;
				else
					convertedValue = converter.convertFromString( value, clazz );

				comboDropDown.add( String.valueOf( convertedValue ));
			}

			// May have alternate labels

			/*
			String lookupLabels = attributes.get( LOOKUP_LABELS );

			if ( lookupLabels != null && !"".equals( lookupLabels ) )
			{
				Map<String, String> labelsMap = SwtWidgetBuilderUtils.getLabelsMap( values, CollectionUtils.fromString( attributes.get( LOOKUP_LABELS ) ) );

				comboBox.setEditor( new LookupComboBoxEditor( labelsMap ) );
				comboBox.setRenderer( new LookupComboBoxRenderer( labelsMap ) );
			}
			*/

			return comboDropDown;
		}

		if ( clazz != null )
		{
			// Primitives

			if ( clazz.isPrimitive() )
			{
				// booleans

				if ( boolean.class.equals( clazz ) )
					return new Button( metawidget, SWT.CHECK );

				// chars

				if ( char.class.equals( clazz ) )
					return new Text( metawidget, SWT.None );

				// Ranged

				String minimumValue = attributes.get( MINIMUM_VALUE );
				String maximumValue = attributes.get( MAXIMUM_VALUE );

				if ( minimumValue != null && !"".equals( minimumValue ) && maximumValue != null && !"".equals( maximumValue ) )
				{
					Scale scale = new Scale( metawidget, SWT.NONE );
					scale.setMinimum( Integer.parseInt( minimumValue ) );
					scale.setSelection( scale.getMinimum() );
					scale.setMaximum( Integer.parseInt( maximumValue ) );

					return scale;
				}

				// Not-ranged

				Spinner spinner = new Spinner( metawidget, SWT.NONE );

				// (use 'new', not '.valueOf', for JDK 1.4 compatibility)

				if ( byte.class.equals( clazz ) )
				{
					byte value = 0;
					byte minimum = Byte.MIN_VALUE;
					byte maximum = Byte.MAX_VALUE;

					if ( minimumValue != null && !"".equals( minimumValue ) )
					{
						minimum = Byte.parseByte( minimumValue );
						value = (byte) Math.max( value, minimum );
					}

					if ( maximumValue != null && !"".equals( maximumValue ) )
					{
						maximum = Byte.parseByte( maximumValue );
						value = (byte) Math.min( value, maximum );
					}

					setSpinnerModel( spinner, value, minimum, maximum, (byte) 1 );
				}
				else if ( short.class.equals( clazz ) )
				{
					short value = 0;
					short minimum = Short.MIN_VALUE;
					short maximum = Short.MAX_VALUE;

					if ( minimumValue != null && !"".equals( minimumValue ) )
					{
						minimum = Short.parseShort( minimumValue );
						value = (short) Math.max( value, minimum );
					}

					if ( maximumValue != null && !"".equals( maximumValue ) )
					{
						maximum = Short.parseShort( maximumValue );
						value = (short) Math.min( value, maximum );
					}

					setSpinnerModel( spinner, value, minimum, maximum, (short) 1 );
				}
				else if ( int.class.equals( clazz ) )
				{
					int value = 0;
					int minimum = Integer.MIN_VALUE;
					int maximum = Integer.MAX_VALUE;

					if ( minimumValue != null && !"".equals( minimumValue ) )
					{
						minimum = Integer.parseInt( minimumValue );
						value = Math.max( value, minimum );
					}

					if ( maximumValue != null && !"".equals( maximumValue ) )
					{
						maximum = Integer.parseInt( maximumValue );
						value = Math.min( value, maximum );
					}

					setSpinnerModel( spinner, value, minimum, maximum, 1 );
				}
				else if ( long.class.equals( clazz ) )
					return new Text( metawidget, SWT.NONE );

				else if ( float.class.equals( clazz ) )
					return new Text( metawidget, SWT.NONE );

				else if ( double.class.equals( clazz ) )
					return new Text( metawidget, SWT.NONE );

				return spinner;
			}

			// Strings

			if ( String.class.equals( clazz ) )
			{
				if ( TRUE.equals( attributes.get( MASKED ) ) )
					return new Text( metawidget, SWT.PASSWORD );

				/*
				if ( TRUE.equals( attributes.get( LARGE ) ) )
				{
					JTextArea textarea = new JTextArea();

					// Since we know we are dealing with Strings, we consider
					// word-wrapping a sensible default

					textarea.setLineWrap( true );
					textarea.setWrapStyleWord( true );

					// We also consider 2 rows a sensible default, so that the
					// JTextArea is always distinguishable from a Text

					textarea.setRows( 2 );
					return new JScrollPane( textarea );
				}
				*/

				return new Text( metawidget, SWT.NONE );
			}

			// Dates

			if ( Date.class.equals( clazz ) )
				return new Text( metawidget, SWT.NONE );

			// Numbers
			//
			// Note: we use a text field, not a JSpinner or JSlider, because
			// BeansBinding gets upset at doing 'setValue( null )' if the Integer
			// is null. We can still use JSpinner/JSliders for primitives, though.

			if ( Number.class.isAssignableFrom( clazz ) )
				return new Text( metawidget, SWT.NONE );

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) )
				return new Stub( metawidget, SWT.NONE );
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return new Text( metawidget, SWT.NONE );

		// Nested Metawidget

		return null;
	}

	//
	// Private methods
	//

	/**
	 * Sets the JSpinner model.
	 * <p>
	 * By default, a JSpinner calls <code>setColumns</code> upon <code>setModel</code>. For numbers
	 * like <code>Integer.MAX_VALUE</code> and <code>Double.MAX_VALUE</code>, this can be very large
	 * and mess up the layout. Here, we reset <code>setColumns</code> to 0.
	 * <p>
	 * Note it is very important we set the initial value of the <code>JSpinner</code> to the same
	 * type as the property it maps to (eg. float or double, int or long).
	 */

	private void setSpinnerModel( Spinner spinner, int selection, int minimum, int maximum, int increment )
	{
		spinner.setMinimum( minimum );
		spinner.setMaximum( maximum );
		spinner.setSelection( selection );
		spinner.setIncrement( increment );
	}
}
