/**
 * Custom exception type for errors specific to the Kiwi task manager.
 * 
 * Extends {@link.Exception} to provide domain-specific error handling for invalid
 * user input, parsing failures, storage issues, and other application-specific
 * problems.
 * 
 * Used throughout the Kiwi application to wrap and communicate errors in a
 * consistent manner, providing clear feedback to users via error messages.
 * 
 * Example usage:
 * if (!validCommand) {
 *     throw new KiwiException("Invalid command format. Use 'list' to see commands.");
 * }
 * 
 * try {
 *     Task task = parser.parse("todo invalid date");
 * } catch (KiwiException e) {
 *     ui.showError(e.getMessage());
 * }
 * 
 * @author [zow1e]
 * @see Exception
 * @see java.time.format.DateTimeParseException
 */

package kiwi.helper;

public class KiwiException extends Exception {

    /**
     * Constructs a KiwiException with the specified error message.
     * 
     * Passes the message to the parent {@link Exception} constructor for standard
     * exception handling and stack trace generation.
     * 
     * @param message the error message describing the problem (non-null)
     */
    public KiwiException(String message) {
        super(message);
    }
}
