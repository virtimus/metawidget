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

package org.metawidget.vaadin.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.util.CollectionUtils;

import com.vaadin.ui.TextArea;

/**
 * @author Richard Kennard
 */

public class SwingWidgetBuilderTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetBuilder()
		throws Exception {

		VaadinWidgetBuilder widgetBuilder = new VaadinWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// JSlider

		/*
		attributes.put( TYPE, int.class.getName() );
		attributes.put( MINIMUM_VALUE, "2" );
		attributes.put( MAXIMUM_VALUE, "99" );

		JSlider slider = (JSlider) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( 2, slider.getMinimum() );
		assertEquals( 2, slider.getValue() );
		assertEquals( 99, slider.getMaximum() );

		try {
			attributes.put( MINIMUM_VALUE, "1.5" );
			widgetBuilder.buildWidget( PROPERTY, attributes, null );
			assertTrue( false );
		} catch ( NumberFormatException e ) {
			assertEquals( "For input string: \"1.5\"", e.getMessage() );
		}
		*/

		// TextArea

		attributes.put( TYPE, String.class.getName() );
		attributes.put( LARGE, TRUE );

		TextArea textarea = (TextArea) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( !textarea.isReadOnly() );

		// JSpinner

		// bytes

		/*
		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "2" );
		attributes.put( TYPE, byte.class.getName() );

		JSpinner spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( ( (byte) 2 ) == (Byte) ( (SpinnerNumberModel) spinner.getModel() ).getMinimum() );
		assertTrue( ( (byte) 2 ) == (Byte) spinner.getValue() );

		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "99" );
		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( ( (byte) 99 ) == (Byte) ( (SpinnerNumberModel) spinner.getModel() ).getMaximum() );

		// shorts

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "3" );
		attributes.put( TYPE, short.class.getName() );

		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( ( (short) 3 ) == (Short) ( (SpinnerNumberModel) spinner.getModel() ).getMinimum() );
		assertTrue( ( (short) 3 ) == (Short) spinner.getValue() );

		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "98" );
		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( ( (short) 98 ) == (Short) ( (SpinnerNumberModel) spinner.getModel() ).getMaximum() );

		// ints

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "4" );
		attributes.put( TYPE, int.class.getName() );

		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( 4 == (Integer) ( (SpinnerNumberModel) spinner.getModel() ).getMinimum() );
		assertTrue( 4 == (Integer) spinner.getValue() );

		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "97" );
		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( 97 == (Integer) ( (SpinnerNumberModel) spinner.getModel() ).getMaximum() );

		// longs

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "5" );
		attributes.put( TYPE, long.class.getName() );

		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( ( (long) 5 ) == (Long) ( (SpinnerNumberModel) spinner.getModel() ).getMinimum() );
		assertTrue( ( (long) 5 ) == (Long) spinner.getValue() );

		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "96" );
		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( ( (long) 96 ) == (Long) ( (SpinnerNumberModel) spinner.getModel() ).getMaximum() );

		// floats

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "1.6" );
		attributes.put( TYPE, float.class.getName() );

		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( 1.6f, spinner.getValue() );
		assertEquals( 0.1f, ( (SpinnerNumberModel) spinner.getModel() ).getStepSize() );

		attributes.put( MAXIMUM_FRACTIONAL_DIGITS, "3" );
		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( 0.001f, ( (SpinnerNumberModel) spinner.getModel() ).getStepSize() );

		attributes.put( MINIMUM_VALUE, "-1.6" );
		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( 0f, spinner.getValue() );

		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "-1" );
		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( -1f, spinner.getValue() );

		// doubles

		attributes.put( TYPE, double.class.getName() );
		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "1.6" );
		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( 1.6d, spinner.getValue() );
		assertEquals( 1000, Math.round( ( (Double) ( (SpinnerNumberModel) spinner.getModel() ).getStepSize() ) * 1000000 ) );

		attributes.put( MINIMUM_VALUE, "-1.6" );
		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( 0d, spinner.getValue() );

		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "-1" );
		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( -1d, spinner.getValue() );
		*/
	}
}
