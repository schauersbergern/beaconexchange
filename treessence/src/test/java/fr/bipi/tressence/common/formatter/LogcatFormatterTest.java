package fr.bipi.tressence.common.formatter;

import org.junit.Before;
import org.junit.Test;

import java.util.TimeZone;

import fr.bipi.tressence.common.os.OsInfoProvider;
import fr.bipi.tressence.common.time.TimeStamper;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class LogcatFormatterTest {

    private LogcatFormatter formatter;

    @Before
    public void before() {
        formatter = LogcatFormatter.INSTANCE;
        formatter.setTimeStamper(new TimeStamper("MM-dd HH:mm:ss:SSS", TimeZone.getTimeZone("GMT+2")));
        formatter.setOsInfoProvider(new OsInfoProvider() {

            private int counter = 0;
            private long base = 1571991195000L;

            @Override
            public long currentTimeMillis() {
                return base + (counter++ * 1000);
            }

            @Override
            public long getCurrentThreadId() {
                return 1;
            }
        });
    }

    @Test
    public void format() {
        assertThat(formatter.format(1, "tag", "message"),
                   is("10-25 10:13:15:000 tag(1) : message\n"));
        assertThat(formatter.format(2, "tag", "message"),
                   is("10-25 10:13:16:000 V/tag(1) : message\n"));
        assertThat(formatter.format(3, "tag", "message"),
                   is("10-25 10:13:17:000 D/tag(1) : message\n"));
        assertThat(formatter.format(4, "tag", "message"),
                   is("10-25 10:13:18:000 I/tag(1) : message\n"));
        assertThat(formatter.format(5, "tag", "message"),
                   is("10-25 10:13:19:000 W/tag(1) : message\n"));
        assertThat(formatter.format(6, "tag", "message"),
                   is("10-25 10:13:20:000 E/tag(1) : message\n"));
        assertThat(formatter.format(7, "tag", "message"),
                   is("10-25 10:13:21:000 WTF/tag(1) : message\n"));
        assertThat(formatter.format(8, "tag", "message"),
                   is("10-25 10:13:22:000 tag(1) : message\n"));
    }
}
