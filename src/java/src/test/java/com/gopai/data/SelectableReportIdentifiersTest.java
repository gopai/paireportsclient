package com.gopai.data;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SelectableReportIdentifiersTest {


    private SelectableReportIdentifiers selectableReportIdentifiers;
    private ReportIdentifier firstOption;
    private ReportIdentifier secondOption;
    private ReportIdentifier thirdOption;

    @Before
    public void setUp() throws Exception {
        firstOption = makeRandomReportConfig();
        secondOption = makeRandomReportConfig();
        thirdOption = makeRandomReportConfig();
        selectableReportIdentifiers = new SelectableReportIdentifiers(Arrays.asList(firstOption, secondOption, thirdOption));
    }

    static ReportIdentifier makeRandomReportConfig() {
        ReportIdentifier reportConfig = new ReportIdentifier();
        reportConfig.setName("A Good Name");
        reportConfig.setExternalName("GetGoodNameReport");
        reportConfig.setReportGUID(UUID.randomUUID().toString());
        return reportConfig;
    }

    @Test
    public void givenListOfOptions_SelectNone_SelectedShouldBeEmptyList() {
        assertThat(selectableReportIdentifiers.getSelectedOptions(), is(Collections.emptyList()));
    }

    @Test
    public void givenListOfOptions_SelectSecond_SelectedShouldContainSecondOption() {
        selectableReportIdentifiers.select(Collections.singletonList(1));

        assertThat(selectableReportIdentifiers.getSelectedOptions().size(), is(1));
    }

    @Test
    public void givenListOfOptions_SelectFirst_SelectedShouldContainSecondOption() {
        selectableReportIdentifiers.select(Collections.singletonList(0));
        HashSet<Integer> set = new HashSet<>();
        set.add(1);

        assertThat(selectableReportIdentifiers.getSelectedOptions().size(), is(1));
        assertThat(selectableReportIdentifiers.getSelected().size(), is(set.size()));
    }
}
