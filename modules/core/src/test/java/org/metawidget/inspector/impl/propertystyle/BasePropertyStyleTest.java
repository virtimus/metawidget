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

package org.metawidget.inspector.impl.propertystyle;

import java.util.Date;

import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyleConfig;
import org.metawidget.util.MetawidgetTestUtils;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class BasePropertyStyleTest
	extends TestCase {

	//
	// Public methods
	//

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( BasePropertyStyleConfig.class, new BasePropertyStyleConfig() {
			// Subclass
		} );
	}

	public void testExcludedBaseType() {

		// Default excludeBaseType

		BasePropertyStyle propertyStyle = new JavaBeanPropertyStyle();
		assertTrue( true == propertyStyle.isExcludedBaseType( Date.class ) );
		assertTrue( true == propertyStyle.isExcludedBaseType( JTextField.class ) );
		assertTrue( false == propertyStyle.isExcludedBaseType( Element.class ) );

		// Null excludeBaseType

		JavaBeanPropertyStyleConfig config = new JavaBeanPropertyStyleConfig();
		config.setExcludeBaseType( null );
		propertyStyle = new JavaBeanPropertyStyle( config );

		assertTrue( false == propertyStyle.isExcludedBaseType( Date.class ) );
		assertTrue( false == propertyStyle.isExcludedBaseType( JTextField.class ) );
		assertTrue( false == propertyStyle.isExcludedBaseType( Element.class ) );
	}
}