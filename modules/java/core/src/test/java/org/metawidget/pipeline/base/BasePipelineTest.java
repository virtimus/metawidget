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

package org.metawidget.pipeline.base;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import junit.framework.TestCase;

import org.metawidget.config.iface.ConfigReader;
import org.metawidget.config.impl.BaseConfigReader;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessor;
import org.metawidget.inspectionresultprocessor.sort.ComesAfterInspectionResultProcessor;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.layout.iface.Layout;
import org.metawidget.pipeline.w3c.W3CPipeline;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.widgetbuilder.iface.AdvancedWidgetBuilder;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetprocessor.iface.AdvancedWidgetProcessor;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.w3c.dom.Document;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class BasePipelineTest
	extends TestCase {

	//
	// Public methods
	//

	public void testBuildCompoundWidget()
		throws Exception {

		W3CPipeline<JComponent, JComponent, JComponent> pipeline = new MockPipeline();

		// Bad entity

		Document document = XmlUtils.documentFromString( "<inspection-result><property type=\"foo\"/></inspection-result>" );

		try {
			pipeline.buildWidgets( document.getDocumentElement() );
			fail();
		} catch ( Exception e ) {
			assertEquals( "Top-level element name should be entity, not property", e.getMessage() );
		}

		// Bad child

		document = XmlUtils.documentFromString( "<inspection-result><entity type=\"foo\"><bar/></entity></inspection-result>" );

		try {
			pipeline.buildCompoundWidget( XmlUtils.getFirstChildElement( document.getDocumentElement() ) );
			fail();
		} catch ( Exception e ) {
			assertEquals( "Child element #1 should be property or action, not bar", e.getMessage() );
		}

		// Missing name attribute

		document = XmlUtils.documentFromString( "<inspection-result><entity type=\"foo\"><property/></entity></inspection-result>" );

		try {
			pipeline.buildCompoundWidget( XmlUtils.getFirstChildElement( document.getDocumentElement() ) );
			fail();
		} catch ( Exception e ) {
			assertEquals( "Child element #1 has no @name", e.getMessage() );
		}
	}

	public void testInitNestedPipeline()
		throws Exception {

		W3CPipeline<JComponent, JComponent, JComponent> pipeline = new MockPipeline();
		W3CPipeline<JComponent, JComponent, JComponent> nestedPipeline = new MockPipeline();

		PropertyTypeInspector inspector = new PropertyTypeInspector();

		ConfigReader configReader = new BaseConfigReader() {

			@Override
			public Object configure( String resource, Object toConfigure, String... names ) {

				return null;
			}

			@Override
			public Object configure( InputStream stream, Object toConfigure, String... names ) {

				return null;
			}
		};
		ComesAfterInspectionResultProcessor<JComponent> inspectionResultProcessor = new ComesAfterInspectionResultProcessor<JComponent>();
		WidgetBuilder<JComponent, JComponent> widgetBuilder = new WidgetBuilder<JComponent, JComponent>() {

			public JComponent buildWidget( String elementName, Map<String, String> attributes, JComponent metawidget ) {

				return null;
			}
		};
		WidgetProcessor<JComponent, JComponent> widgetProcessor = new WidgetProcessor<JComponent, JComponent>() {

			public JComponent processWidget( JComponent widget, String elementName, Map<String, String> attributes, JComponent metawidget ) {

				return null;
			}
		};
		Layout<JComponent, JComponent, JComponent> layout = new Layout<JComponent, JComponent, JComponent>() {

			public void layoutWidget( JComponent widget, String elementName, Map<String, String> attributes, JComponent container, JComponent metawidget ) {

				// Do nothing
			}
		};

		pipeline.setConfigReader( configReader );
		pipeline.setInspector( inspector );
		pipeline.addInspectionResultProcessor( inspectionResultProcessor );
		pipeline.setWidgetBuilder( widgetBuilder );
		pipeline.addWidgetProcessor( widgetProcessor );
		pipeline.setLayout( layout );
		pipeline.initNestedPipeline( nestedPipeline, null );

		// Test elements are initialized

		assertTrue( nestedPipeline.getConfigReader() == configReader );
		assertTrue( nestedPipeline.getInspector() == inspector );
		assertTrue( nestedPipeline.getWidgetBuilder() == widgetBuilder );
		assertTrue( nestedPipeline.getLayout() == layout );

		// Test defensive copy

		assertEquals( nestedPipeline.getInspectionResultProcessors(), pipeline.getInspectionResultProcessors() );
		assertTrue( nestedPipeline.getInspectionResultProcessors() != pipeline.getInspectionResultProcessors() );
		pipeline.getInspectionResultProcessors().clear();
		assertTrue( pipeline.getInspectionResultProcessors().isEmpty() );
		assertTrue( !nestedPipeline.getInspectionResultProcessors().isEmpty() );

		// Test defensive copy

		assertEquals( nestedPipeline.getWidgetProcessors(), pipeline.getWidgetProcessors() );
		assertTrue( nestedPipeline.getWidgetProcessors() != pipeline.getWidgetProcessors() );
		pipeline.getWidgetProcessors().clear();
		assertTrue( pipeline.getWidgetProcessors().isEmpty() );
		assertTrue( !nestedPipeline.getWidgetProcessors().isEmpty() );

		// Test read only

		assertTrue( !nestedPipeline.isReadOnly() );
		pipeline.setReadOnly( true );
		pipeline.initNestedPipeline( nestedPipeline, null );
		assertTrue( nestedPipeline.isReadOnly() );
		nestedPipeline.setReadOnly( false );

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( READ_ONLY, TRUE );
		pipeline.initNestedPipeline( nestedPipeline, attributes );
		assertTrue( nestedPipeline.isReadOnly() );

		// Maximum inspection depth

		pipeline.setMaximumInspectionDepth( 100 );
		pipeline.initNestedPipeline( nestedPipeline, null );
		assertEquals( 99, nestedPipeline.getMaximumInspectionDepth() );
	}

	public void testAdvancedStartEndBuild()
		throws Exception {

		final List<String> events = CollectionUtils.newArrayList();
		W3CPipeline<JComponent, JComponent, JComponent> pipeline = new MockPipeline();

		pipeline.setWidgetBuilder( new AdvancedWidgetBuilder<JComponent, JComponent>() {

			public void onStartBuild( JComponent metawidget ) {

				events.add( "WidgetBuilder::onStartBuild" );
			}

			public JComponent buildWidget( String elementName, Map<String, String> attributes, JComponent metawidget ) {

				events.add( "WidgetBuilder::buildWidget" );
				return new JPanel();
			}

			public void onEndBuild( JComponent metawidget ) {

				events.add( "WidgetBuilder::onEndBuild" );
			}
		} );
		pipeline.addWidgetProcessor( new AdvancedWidgetProcessor<JComponent, JComponent>() {

			public void onStartBuild( JComponent metawidget ) {

				events.add( "WidgetProcessor::onStartBuild" );
			}

			public JComponent processWidget( JComponent widget, String elementName, Map<String, String> attributes, JComponent metawidget ) {

				events.add( "WidgetProcessor::processWidget" );
				return widget;
			}

			public void onEndBuild( JComponent metawidget ) {

				events.add( "WidgetProcessor::onEndBuild" );
			}
		} );
		pipeline.setLayout( new AdvancedLayout<JComponent, JComponent, JComponent>() {

			public void onStartBuild( JComponent metawidget ) {

				events.add( "Layout::onStartBuild" );
			}

			public void startContainerLayout( JComponent container, JComponent metawidget ) {

				events.add( "Layout::startContainerLayout" );
			}

			public void layoutWidget( JComponent widget, String elementName, Map<String, String> attributes, JComponent container, JComponent metawidget ) {

				events.add( "Layout::layoutWidget" );
			}

			public void endContainerLayout( JComponent container, JComponent metawidget ) {

				events.add( "Layout::endContainerLayout" );
			}

			public void onEndBuild( JComponent metawidget ) {

				events.add( "Layout::onEndBuild" );
			}
		} );

		pipeline.buildWidgets( XmlUtils.documentFromString( "<inspection-result><entity/></inspection-result>" ).getDocumentElement() );

		assertEquals( "WidgetBuilder::onStartBuild", events.get( 0 ) );
		assertEquals( "WidgetProcessor::onStartBuild", events.get( 1 ) );
		assertEquals( "Layout::onStartBuild", events.get( 2 ) );
		assertEquals( "Layout::startContainerLayout", events.get( 3 ) );
		assertEquals( "WidgetBuilder::buildWidget", events.get( 4 ) );
		assertEquals( "WidgetProcessor::processWidget", events.get( 5 ) );
		assertEquals( "Layout::layoutWidget", events.get( 6 ) );
		assertEquals( "Layout::endContainerLayout", events.get( 7 ) );
		assertEquals( "Layout::onEndBuild", events.get( 8 ) );
		assertEquals( "WidgetProcessor::onEndBuild", events.get( 9 ) );
		assertEquals( "WidgetBuilder::onEndBuild", events.get( 10 ) );
		assertEquals( 11, events.size() );
	}

	public void testGetInspectionResultProcessor() {

		MyInspectionResultProcessor myInspectionResultProcessor = new MyInspectionResultProcessor();

		@SuppressWarnings( "unchecked" )
		InspectionResultProcessor<JComponent>[] inspectionResultProcessors = new InspectionResultProcessor[] {
				new ComesAfterInspectionResultProcessor<JComponent>(),
				myInspectionResultProcessor
		};

		MockPipeline pipeline = new MockPipeline();
		pipeline.setInspectionResultProcessors( inspectionResultProcessors );

		assertEquals( myInspectionResultProcessor, pipeline.getInspectionResultProcessor( MyInspectionResultProcessor.class ) );
	}

	//
	// Inner class
	//

	/* package private */static class MockPipeline
		extends W3CPipeline<JComponent, JComponent, JComponent> {

		@Override
		protected String getDefaultConfiguration() {

			return null;
		}

		@Override
		protected Map<String, String> getAdditionalAttributes( JComponent widget ) {

			return null;
		}

		@Override
		protected JComponent buildNestedMetawidget( Map<String, String> attributes )
			throws Exception {

			return null;
		}

		@Override
		protected JComponent getPipelineOwner() {

			return null;
		}
	}

	/* package private */static class MyInspectionResultProcessor
		implements InspectionResultProcessor<JComponent> {

		//
		// Public methods
		//

		public String processInspectionResult( String inspectionResult, JComponent metawidget, Object toInspect, String type, String... names ) {

			return null;
		}
	}
}
