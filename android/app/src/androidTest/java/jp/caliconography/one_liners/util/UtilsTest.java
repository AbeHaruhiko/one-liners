package jp.caliconography.one_liners.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UtilsTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void NullToEmptyにnullを渡すと空文字が返る() throws Exception {
        assertThat(Utils.nullToEmpty(null), is(""));
    }

    @Test
    public void NullToEmptyにnull以外を渡すとその文字列が返る() throws Exception {
        String expect = "nullでない文字列";
        assertThat(Utils.nullToEmpty(expect), is(expect));
    }
}