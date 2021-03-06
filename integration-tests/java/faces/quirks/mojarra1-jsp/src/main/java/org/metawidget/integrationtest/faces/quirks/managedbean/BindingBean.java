// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.integrationtest.faces.quirks.managedbean;

import java.io.Serializable;
import java.util.Map;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.faces.component.widgetprocessor.ReadableIdProcessor;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiRequired;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class BindingBean {

	//
	// Private members
	//

	private Foo	mFoo	= new Foo();

	//
	// Public methods
	//

	@UiHidden
	public UIMetawidget getMetawidget1() {

		// First-time init
		//
		// JSF spec: "When a component instance is first created (typically by virtue of being
		// referenced by a UIComponentELTag in a JSP page), the JSF implementation will retrieve the
		// ValueExpression for the name binding, and call getValue() on it. If this call returns a
		// non-null UIComponent value (because the JavaBean programmatically instantiated and
		// configured a component already), that instance will be added to the component tree that
		// is being created"

		UIMetawidget metawidget = new HtmlMetawidget();
		initMetawidget1( metawidget );
		return metawidget;
	}

	public void setMetawidget1( UIMetawidget metawidget ) {

		if ( metawidget.getWidgetProcessors().size() != 8 ) {
			throw new RuntimeException( "Saw " + metawidget.getWidgetProcessors().size() + " WidgetProcessors by default" );
		}

		// POST-back init
		//
		// JSF spec: "When a component tree is recreated during the Restore View phase of
		// the request processing lifecycle, for each component that has a ValueExpression
		// associated with the name 'binding', setValue() will be called on it, passing the
		// recreated component instance"

		initMetawidget1( metawidget );
	}

	@UiHidden
	public UIMetawidget getMetawidget2() {

		// First-time init

		UIMetawidget metawidget = new HtmlMetawidget();
		initMetawidget2( metawidget );
		return metawidget;
	}

	/**
	 * @param metawidget
	 *            not used
	 */

	public void setMetawidget2( UIMetawidget metawidget ) {

		// Do nothing
	}

	@UiHidden
	public UIMetawidget getMetawidget3() {

		// First-time init

		UIMetawidget metawidget = new HtmlMetawidget();
		initMetawidget3( metawidget );
		return metawidget;
	}

	/**
	 * @param metawidget
	 *            not used
	 */

	public void setMetawidget3( UIMetawidget metawidget ) {

		// Do nothing
	}

	public Foo getFoo() {

		return mFoo;
	}

	@UiAction
	@UiComesAfter( "foo" )
	public void save() {

		// Do nothing
	}

	//
	// Private methods
	//

	private void initMetawidget1( UIMetawidget metawidget ) {

		metawidget.removeWidgetProcessor( metawidget.getWidgetProcessor( ReadableIdProcessor.class ) );
		metawidget.addWidgetProcessor( new WidgetProcessor<UIComponent, UIMetawidget>() {

			public UIComponent processWidget( UIComponent widget, String elementName, Map<String, String> attributes, UIMetawidget parentMetawidget ) {

				return setId( widget, parentMetawidget );
			}
		} );
	}

	private void initMetawidget2( UIMetawidget metawidget ) {

		metawidget.setValue( new Foo() );
	}

	private void initMetawidget3( UIMetawidget metawidget ) {

		metawidget.setValue( Foo.class );
	}

	/**
	 * Example of using an anonymous inner class (the WidgetProcessor) tied to an outer class
	 * method.
	 */

	/* package private */UIComponent setId( UIComponent widget, UIMetawidget metawidget ) {

		// Id may have been set automatically, especially after POSTback, but should never be set by
		// ReadableIdProcessor

		if ( widget.getId() != null && widget.getId().startsWith( "binding" ) ) {
			throw new RuntimeException( "Id is '" + widget.getId() + "'. ReadableIdProcessor still active?" );
		}

		if ( widget instanceof HtmlInputText || widget instanceof HtmlInputTextarea ) {
			widget.setId( "child" + metawidget.getChildren().size() );
		} else if ( widget instanceof UIMetawidget || widget instanceof UICommand || widget instanceof UIStub ){
			widget.setId( FacesUtils.createUniqueId() );
		} else {
			throw new RuntimeException( "Unexpected widget of " + widget.getClass() );
		}

		return widget;
	}

	//
	// Inner class
	//

	public static class Foo
		implements Serializable {

		//
		// Private members
		//

		private String	mBar	= "Bar Value";

		private String	mBaz	= "Baz Value";

		//
		// Public methods
		//

		@UiRequired
		public String getBar() {

			return mBar;
		}

		public void setBar( String bar ) {

			mBar = bar;
		}

		@UiComesAfter( "bar" )
		@UiLarge
		public String getBaz() {

			return mBaz;
		}

		public void setBaz( String baz ) {

			mBaz = baz;
		}
	}
}
