package com.gopai.cli;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class TerminalUtilsTest {

    @Test
    public void testYesNoDialog() {
        ByteArrayInputStream yesInput = new ByteArrayInputStream("Y\n".getBytes());
        ByteArrayInputStream noInput = new ByteArrayInputStream("N\n".getBytes());
        boolean givenAYes = false;
        boolean givenANo = false;
        try {
            System.setIn(yesInput);
            givenAYes = TerminalUtils.startYesNoDialog(new BufferedReader(new InputStreamReader(System.in)), "Hello There");
            System.setIn(noInput);
            givenANo = !TerminalUtils.startYesNoDialog(new BufferedReader(new InputStreamReader(System.in)), "Hello There");
        } catch (IOException e) {
            fail();
        }
        assertThat(givenAYes, is(true));
        assertThat(givenANo, is(true));
    }

    @Test
    public void testDirectorySelection() {
        String badPath = "C:\\Users\\BadPathThatShouldntExist\n";
        String goodPath = "C:\\Users\n";
        ByteArrayInputStream notAPathInput = new ByteArrayInputStream((badPath + goodPath).getBytes());
        String returnedPath = "";
        try {
            System.setIn(notAPathInput);
            returnedPath = TerminalUtils.startDirectorySelectionDialog(new BufferedReader(new InputStreamReader(System.in)));
        } catch (IOException e) {
            fail();
        }
        assertThat(returnedPath, is("C:\\Users"));
    }
}