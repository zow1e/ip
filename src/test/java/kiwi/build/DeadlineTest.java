package kiwi.build;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DeadlineTest {

    @Test
    public void constructor_validDateTime_setsCorrectly() {
        Deadline d = new Deadline("Buy groceries", "2026-02-04 1800");
        assertEquals("[D][ ] Buy groceries (by: Feb 4 2026 1800)", d.toString());
    }

    @Test
    public void getDateTimeString_validFormat_correctString() {
        Deadline d = new Deadline("Buy groceries", "2026-02-04 1800");
        assertEquals("Feb 4 2026 1800", d.getDateTimeString());
    }

    @Test
    public void getDateTimeString_null_returnsEmpty() {
        Deadline d = new Deadline("Buy groceries", "2026-02-04 1800");
        assertTrue(d.getDateTime() != null);
    }

    @Test
    public void toString_done_correctFormat() {
        Deadline d = new Deadline("Buy groceries", "2026-02-04 1800");
        d.markTask();
        assertEquals("[D][X] Buy groceries (by: Feb 4 2026 1800)", d.toString());
    }

    @Test
    public void toString_notDone_correctFormat() {
        Deadline d = new Deadline("Buy groceries", "2026-02-04 1800");
        assertEquals("[D][ ] Buy groceries (by: Feb 4 2026 1800)", d.toString());
    }
}
