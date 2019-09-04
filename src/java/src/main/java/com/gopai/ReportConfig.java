package com.gopai;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReportConfig {

    List<Field> fields;

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "ReportConfig{" +
                "fields=" + fields +
                '}';
    }

    public static class Field {
        String type;
        boolean readonly;
        String name;
        Data data;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isReadonly() {
            return readonly;
        }

        public void setReadonly(boolean readonly) {
            this.readonly = readonly;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Field{" +
                    "type='" + type + '\'' +
                    ", readonly=" + readonly +
                    ", name='" + name + '\'' +
                    ", data=" + data +
                    '}';
        }

        public static class Data {
            @SerializedName("@response-type")
            String type;
            boolean forced;
            List<Select> values;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public boolean isForced() {
                return forced;
            }

            public void setForced(boolean forced) {
                this.forced = forced;
            }

            public List<Select> getValues() {
                return values;
            }

            public void setValues(List<Select> values) {
                this.values = values;
            }

            @Override
            public String toString() {
                return "Data{" +
                        "type='" + type + '\'' +
                        ", forced=" + forced +
                        ", values=" + values +
                        '}';
            }
        }

        public static class Select {
            String label;
            String key;

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            @Override
            public String toString() {
                return "Select{" +
                        "label='" + label + '\'' +
                        ", key='" + key + '\'' +
                        '}';
            }
        }
    }
}
