/**
 * Custom exception type for errors specific to the Kiwi task manager.
 *
 * Extends {@link Exception} to provide domain-specific error handling for invalid
 * user input, parsing failures, storage issues, and other application-specific
 * problems.
 *
 * @author zow1e
 * @see Exception
 */
package kiwi.helper;

/**
 * Custom exception for Kiwi application errors.
 */
public class KiwiException extends Exception {

    /**
     * Constructs a KiwiException with the specified error message.
     *
     * @param message the error message describing the problem (non-null)
     */
    public KiwiException(String message) {
        super(message);
    }
}
