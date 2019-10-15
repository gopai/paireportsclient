package com.gopai.pair.sdk.v1;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * The configuration values for a given Report.
 */
public class ReportConfig {

    public List<Field> fields;

    /**
     * @return list of {@link Field}s
     */
    public List<Field> getFields() {
        return fields;
    }

    /**
     * Returns the specified {@link Field}
     *
     * @param fieldIndex Index of the Field to be selected.
     * @return {@link Field}
     */
    public Field getField(int fieldIndex) {
        return fields.get(fieldIndex);
    }

    /**
     * @param fields list of {@link Field}s
     */
    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "ReportConfig{" +
                "fields=" + fields +
                "}";
    }

    /**
     * A field (or column) of a given report.
     * What type the field is, whether the field is read only, the name of the field, and the {@link Data}
     */
    public static class Field {
        String type;
        boolean readonly;
        String name;
        Data data;

        /**
         * @return the type of the field.
         */
        public String getType() {
            return type;
        }

        /**
         * @param type The string description of the field Type.
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * @return if the fields is read only.
         */
        public boolean isReadonly() {
            return readonly;
        }

        /**
         * @param readonly whether the field is Read only.
         */
        public void setReadonly(boolean readonly) {
            this.readonly = readonly;
        }

        /**
         * @return The name of the field.
         */
        public String getName() {
            return name;
        }

        /**
         * @param name Set the Name of the field.
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * returns the {@link Data} for the field.
         *
         * @return {@link Data}
         */
        public Data getData() {
            return data;
        }

        /**
         * @param data {@link Data}
         */
        public void setData(Data data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "\n    Field{" +
                    "type='" + type + '\'' +
                    ", readonly=" + readonly +
                    ", name='" + name + '\'' +
                    ", data=" + data +
                    "}";
        }

        /**
         * The type of filtering that can be applied to {@link Field}
         * The type (Whether it is a selectable value, date, text filter, etc), whether it is forced,
         * and the {@link Value}s when applicable.
         */
        public static class Data {
            @SerializedName("@response-type")
            String type;
            boolean forced;
            List<Value> values;

            /**
             * returns the type of Data (Whether it is a selectable value, date, text filter, etc).
             *
             * @return The Type
             */
            public String getType() {
                return type;
            }

            /**
             * @param type The string Type
             */
            public void setType(String type) {
                this.type = type;
            }

            /**
             * whether the data is forced.
             *
             * @return forced
             */
            public boolean isForced() {
                return forced;
            }

            /**
             * @param forced whether the data is forced.
             */
            public void setForced(boolean forced) {
                this.forced = forced;
            }

            /**
             * Returns a specific {@link Value} based on index.
             *
             * @param index The index of the Value you want returned.
             * @return {@link Value}
             */
            public Value getValue(int index) {
                return values.get(index);
            }

            /**
             * Returns the list of {@link Value}s
             *
             * @return list of {@link Value}s
             */
            public List<Value> getValues() {
                return values;
            }

            /**
             * @param values the list of {@link Value}s.
             */
            public void setValues(List<Value> values) {
                this.values = values;
            }

            @Override
            public String toString() {
                return "\n        Data{" +
                        "type='" + type + '\'' +
                        ", forced=" + forced +
                        ", values=" + values +
                        "}";
            }
        }

        /**
         * A Value that can be selected for a specific {@link Field}'s {@link Data}.
         * Available when the specific {@link Field} is a selection filter or other that has preset values.
         */
        public static class Value {
            String label;
            String key;

            /**
             * The value that is User visible from the report.
             *
             * @return label
             */
            public String getLabel() {
                return label;
            }

            /**
             * @param label the label of the value.
             */
            public void setLabel(String label) {
                this.label = label;
            }

            /**
             * The actual key for the selected value.
             *
             * @return key
             */
            public String getKey() {
                return key;
            }

            /**
             * @param key the key of the value.
             */
            public void setKey(String key) {
                this.key = key;
            }

            @Override
            public String toString() {
                return "\n            Select{" +
                        "label='" + label + '\'' +
                        ", key='" + key + '\'' +
                        "}";
            }
        }
    }
}
