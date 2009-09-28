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

package org.metawidget.gwt.client.ui.layout;

import org.metawidget.layout.iface.LayoutException;

/**
 * Configures a FlexTableLayout prior to use. Once instantiated, Layouts are immutable.
 *
 * @author Richard Kennard
 */

public class FlexTableLayoutConfig
{
	//
	// Private members
	//

	private int			mNumberOfColumns	= 1;

	private String		mTableStyleName;

	private String[]	mColumnStyleNames;

	private String		mSectionStyleName;

	private String		mFooterStyleName;

	//
	// Public methods
	//

	public int getNumberOfColumns()
	{
		return mNumberOfColumns;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public FlexTableLayoutConfig setNumberOfColumns( int numberOfColumns )
	{
		if ( numberOfColumns < 0 )
			throw LayoutException.newException( "numberOfColumns must be >= 0" );

		mNumberOfColumns = numberOfColumns;

		return this;
	}

	public String getTableStyleName()
	{
		return mTableStyleName;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public FlexTableLayoutConfig setTableStyleName( String tableStyleName )
	{
		// TODO: test this works

		mTableStyleName = tableStyleName;

		return this;
	}

	public String[] getColumnStyleNames()
	{
		return mColumnStyleNames;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public FlexTableLayoutConfig setColumnStyleNames( String[] columnStyleNames )
	{
		mColumnStyleNames = columnStyleNames;

		return this;
	}

	public String getSectionStyleName()
	{
		return mSectionStyleName;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public FlexTableLayoutConfig setSectionStyleName( String sectionStyleName )
	{
		mSectionStyleName = sectionStyleName;

		return this;
	}

	public String getFooterStyleName()
	{
		return mFooterStyleName;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public FlexTableLayoutConfig setFooterStyleName( String footerStyleName )
	{
		mFooterStyleName = footerStyleName;

		return this;
	}
}
