package com.gopai.cli;

import com.gopai.cli.Arguments;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ArgumentsTest {

    @Test
    public void givenUserFlagExpectNullValue() {
        String[] args = new String[]{"-u"};

        Arguments indexes = new Arguments(args);

        assertThat(indexes.getUsername(), nullValue());
        assertThat(indexes.missingArguments(), is(true));
    }

    @Test
    public void givenUserExpectOnlyUser() {
        String[] args = new String[]{"-u", "user"};

        Arguments indexes = new Arguments(args);

        assertThat(indexes.getUsername(), is("user"));
        assertThat(indexes.missingArguments(), is(true));
    }

    @Test
    public void givenPasswordExpectOnlyPassword() {
        String[] args = new String[]{"-p", "password"};

        Arguments indexes = new Arguments(args);

        assertThat(indexes.getPassword(), is("password"));
        assertThat(indexes.missingArguments(), is(true));
    }

    @Test
    public void givenGUIDExpectOnlyGUID() {
        String[] args = new String[]{"-guid", "GUID"};

        Arguments indexes = new Arguments(args);

        assertThat(indexes.getGuid(), is("GUID"));
        assertThat(indexes.missingArguments(), is(true));
    }

    @Test
    public void givenPathExpectOnlyPath() {
        String[] args = new String[]{"-path", "PATH"};

        Arguments indexes = new Arguments(args);

        assertThat(indexes.getPath(), is("PATH"));
        assertThat(indexes.missingArguments(), is(true));
    }

    @Test
    public void givenOutputFlagExpectFlagFlipped() {
        String[] args = new String[]{"-o"};

        Arguments indexes = new Arguments(args);

        assertThat(indexes.getSendToOut(), is(true));
        assertThat(indexes.missingArguments(), is(true));
    }

    @Test
    public void givenAllArgumentsWithOutputExpectAllValues() {
        String[] args = new String[]{"-u", "user", "-p", "password", "-guid", "GUID", "-o"};

        Arguments indexes = new Arguments(args);

        assertThat(indexes.getUsername(), is("user"));
        assertThat(indexes.getPassword(), is("password"));
        assertThat(indexes.getGuid(), is("GUID"));
        assertThat(indexes.getPath(), nullValue());
        assertThat(indexes.getSendToOut(), is(true));
        assertThat(indexes.missingArguments(), is(false));
    }


    @Test
    public void givenAllArgumentsWithPathExpectAllValues() {
        String[] args = new String[]{"-u", "user", "-p", "password", "-guid", "GUID", "-path", "PATH"};

        Arguments indexes = new Arguments(args);

        assertThat(indexes.getUsername(), is("user"));
        assertThat(indexes.getPassword(), is("password"));
        assertThat(indexes.getGuid(), is("GUID"));
        assertThat(indexes.getPath(), is("PATH"));
        assertThat(indexes.getSendToOut(), is(false));
        assertThat(indexes.missingArguments(), is(false));
    }

    @Test
    public void givenInteractiveFlagExpectTrue() {
        String[] args = new String[]{"-i"};

        Arguments indexes = new Arguments(args);

        assertThat(indexes.getInteractiveMode(), is(true));
        assertThat(indexes.missingArguments(), is(false));
    }
}