package org.schwering.irc.lib.ssl;

/**
 * Thrown if no SSL implementation is available.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 1.05
 * @version 1.00
 * @see SSLIRCConnection
 */
public class SSLNotSupportedException extends RuntimeException {
	private static final long serialVersionUID = -5108810948951810903L;
	
	/**
	 * Empty exception.
	 */
	public SSLNotSupportedException() {
		super();
	}
	
// ------------------------------

	/**
	 * Creates an exception with description.
	 * @param s The description.
	 */
	public SSLNotSupportedException(String s) {
		super(s);
	}
}
