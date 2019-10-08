package com.gopai.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SelectableReportIdentifiers {
    private final List<ReportIdentifier> configs;
    private final HashSet<Integer> selected = new HashSet<>();

    public SelectableReportIdentifiers(List<ReportIdentifier> configs) {
        this.configs = configs;
    }

    public void select(List<Integer> toSelect) {
        selected.addAll(toSelect);
    }

    public boolean isSelected(int index) {
        return selected.contains(index);
    }

    public HashSet<Integer> getSelected() {
        return selected;
    }

    public List<ReportIdentifier> getOptions() {
        return configs;
    }

    public List<ReportIdentifier> getSelectedOptions() {
        ArrayList<ReportIdentifier> results = new ArrayList<>();
        for (int i = 0; i < configs.size(); i++) {
            ReportIdentifier config = configs.get(i);
            if (selected.contains(i)) {
                results.add(config);
            }
        }
        return results;
    }
}
