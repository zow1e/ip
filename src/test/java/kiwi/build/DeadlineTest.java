package kiwi.build;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.junit.jupiter.api.Assertions.*;

public class DeadlineTest {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    @Test
    public void constructor_validDateTime_setsCorrectly() {
        Deadline dl = new Deadline("test", "2026-01-31 1800");
        assertEquals("test", dl.getDescription());
        assertFalse(dl.isDone);  // via super
        LocalDateTime expected = LocalDateTime.parse("2026-01-31 1800", FORMATTER);
        assertEquals(expected, dl.getDateTime());
    }

    @Test
    public void toString_notDone_correctFormat() {
        Deadline dl = new Deadline("submit iP", "2026-01-31 2359");
        assertEquals("[D][ ] submit iP (by: Jan 31 2026 2359)", dl.toString());
    }

    @Test
    public void toString_done_correctFormat() {
        Deadline dl = new Deadline("read book", "2026-02-01 1200");
        dl.markTask();
        assertEquals("[D][X] read book (by: Feb 1 2026 1200)", dl.toString());
    }

    @Test
    public void getDateTimeString_null_returnsEmpty() {
        Task base = new Task("no date");
        assertEquals("", base.getDateTimeString());
    }

    @Test
    public void getDateTimeString_validFormat_correctString() {
        Deadline dl = new Deadline("meeting", "2026-01-30 1411");
        assertEquals("Jan 30 2026 1411", dl.getDateTimeString());
    }
}
