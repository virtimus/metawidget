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

package org.metawidget.inspector.impl.propertystyle.javabean;

import java.text.MessageFormat;

import org.metawidget.inspector.impl.BaseTraitStyleConfig;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a JavaBeanPropertyStyle prior to use. Once instantiated, PropertyStyles are immutable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JavaBeanPropertyStyleConfig
	extends BaseTraitStyleConfig {

	//
	// Private statics
	//

	private static String[]	DEFAULT_EXCLUDE_NAME	= new String[] { "propertyChangeListeners", "vetoableChangeListeners" };

	//
	// Private members
	//

	private boolean			mNullExcludeName;

	private boolean			mSupportPublicFields;

	private MessageFormat	mPrivateFieldConvention;

	private ClassLoader		mAdditionalClassLoader;

	//
	// Public methods
	//

	/**
	 * Overridden to exclude the names 'propertyChangeListeners' and 'vetoableChangeListeners' by
	 * default.
	 */

	@Override
	public JavaBeanPropertyStyleConfig setExcludeName( String... excludeName ) {

		super.setExcludeName( excludeName );
		mNullExcludeName = ( excludeName == null );

		// Fluent interface

		return this;
	}

	/**
	 * Sets whether to recognize public fields as properties. False by default, as public fields are
	 * not part of the JavaBean specification and most frameworks need getters/setters.
	 *
	 * @return this, as part of a fluent interface
	 */

	public JavaBeanPropertyStyleConfig setSupportPublicFields( boolean supportPublicFields ) {

		mSupportPublicFields = supportPublicFields;

		return this;
	}

	/**
	 * Sets the naming convention used to identify the private field, given the property
	 * name.
	 * <p>
	 * The JavaBean specification does not establish a relationship between getters/setters and
	 * their private fields. This is because some getters/setters will not be simple one-to-one
	 * mappings. For example, a <code>getAge</code> method may calculate itself based off a
	 * <code>mDateOfBirth</code> field, rather than an <code>mAge</code> field.
	 * <p>
	 * However, it is a common requirement to want to <em>annotate</em> the private field rather
	 * than its getter/setter. Frameworks like JPA allow this because they can populate the private
	 * field directly. This does not work well for Metawidget because most UI frameworks, including
	 * binding and validation frameworks, rely on public getters/setters.
	 * <p>
	 * To support the best of both worlds, <code>JavaBeanPropertyStyle</code> can attempt to map a
	 * getter/setter to its private field if given the naming convention to use. The naming
	 * convention is specified as a <code>MessageFormat</code>. Some examples:
	 * <p>
	 * <ul>
	 * <li>{0} = dateOfBirth, surname</li>
	 * <li>'m'{1} = mDateOfBirth, mSurname</li>
	 * <li>'m_'{0} = m_dateOfBirth, m_surname</li>
	 * </ul>
	 * <p>
	 * This mapping will fail silently in cases where there is no private field. It will also fail
	 * silently if the private field name is misspelt, so be careful!
	 *
	 * @return this, as part of a fluent interface
	 */

	public JavaBeanPropertyStyleConfig setPrivateFieldConvention( MessageFormat privateFieldConvention ) {

		mPrivateFieldConvention = privateFieldConvention;

		return this;
	}

	/**
	 * Sets an additional ClassLoader to use to resolve classes.
	 * <p>
	 * This can be useful if using <code>JavaBeanPropertyStyle</code> outside of Metawidget. For
	 * example, you use a <code>JavaBeanPropertyStyle</code> in your EJB layer, but pass it a type
	 * String that refers to a class from the WAR layer. In order to resolve that type, the EJB
	 * layer must use the WAR layer's ClassLoader.
	 *
	 * @return this, as part of a fluent interface
	 */

	public JavaBeanPropertyStyleConfig setAdditionalClassLoader( ClassLoader additionalClassLoader ) {

		mAdditionalClassLoader = additionalClassLoader;

		return this;
	}

	@Override
	public boolean equals( Object that ) {

		if ( this == that ) {
			return true;
		}

		if ( !ObjectUtils.nullSafeClassEquals( this, that ) ) {
			return false;
		}

		if ( mNullExcludeName != ( (JavaBeanPropertyStyleConfig) that ).mNullExcludeName ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mPrivateFieldConvention, ( (JavaBeanPropertyStyleConfig) that ).mPrivateFieldConvention ) ) {
			return false;
		}

		if ( mSupportPublicFields != ( (JavaBeanPropertyStyleConfig) that ).mSupportPublicFields ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mAdditionalClassLoader, ( (JavaBeanPropertyStyleConfig) that ).mAdditionalClassLoader ) ) {
			return false;
		}

		return super.equals( that );
	}

	@Override
	public int hashCode() {

		int hashCode = super.hashCode();
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mNullExcludeName );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mPrivateFieldConvention );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mSupportPublicFields );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mAdditionalClassLoader );

		return hashCode;
	}

	//
	// Protected methods
	//

	@Override
	protected String[] getExcludeName() {

		String[] excludeName = super.getExcludeName();

		if ( excludeName == null && !mNullExcludeName ) {
			return DEFAULT_EXCLUDE_NAME;
		}

		return excludeName;
	}

	protected boolean isSupportPublicFields() {

		return mSupportPublicFields;
	}

	protected MessageFormat getPrivateFieldConvention() {

		return mPrivateFieldConvention;
	}

	protected ClassLoader getAdditionalClassLoader() {

		return mAdditionalClassLoader;
	}
}
