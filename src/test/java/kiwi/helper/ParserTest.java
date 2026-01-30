package kiwi.helper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {
    @Test
    public void parse_todoValid_success() throws KiwiException {
        Parser p = Parser.parse("todo buy milk");
        assertEquals("todo", p.getType());
        assertEquals("buy milk", p.getArg(0));
    }

    @Test
    public void parse_todoEmpty_throwsException() {
        assertThrows(KiwiException.class, () -> Parser.parse("todo"));
    }

    @Test
    public void parse_deadlineValid_success() throws KiwiException {
        Parser p = Parser.parse("deadline read /by 2026-01-31 1800");
        assertEquals("deadline", p.getType());
        assertEquals("read", p.getArg(0));
        assertEquals("2026-01-31 1800", p.getArg(1));
    }

    @Test
    public void parse_deadlineMissingBy_throwsException() {
        assertThrows(KiwiException.class, () -> Parser.parse("deadline read"));
    }

    @Test
    public void parse_eventValid_success() throws KiwiException {
        Parser p = Parser.parse("event party /from 2026-01-31 1430 /to 1630");
        assertEquals("event", p.getType());
        assertEquals("party", p.getArg(0));
        assertEquals("2026-01-31 1430", p.getArg(1));
        assertEquals("1630", p.getArg(2));
    }

    @Test
    public void parse_invalidCommand_throwsException() {
        assertThrows(KiwiException.class, () -> Parser.parse("invalid"));
    }

    @Test
    public void parse_markInvalidNumber_throwsException() {
        assertThrows(KiwiException.class, () -> Parser.parse("mark abc"));
    }
}
