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

package android.text;

/**
 * Dummy implementation for unit testing.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class InputFilter {

	//
	// Inner class
	//

	public static class LengthFilter
		extends InputFilter {

		//
		// Private members
		//

		private int	mLength;

		//
		// Constructor
		//

		public LengthFilter( int length ) {

			mLength = length;
		}

		//
		// Public methods
		//

		public int getLength() {

			return mLength;
		}
	}
}
