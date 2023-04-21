        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f, IdStrategy strategy) {
            return new Field<T>(FieldType.FIXED64, number, name) {

                {
                    f.setAccessible(true);
                }

                public void mergeFrom(Input input, T message) throws IOException {
                    try {
                        f.set(message, new Date(input.readFixed64()));
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                public void writeTo(Output output, T message) throws IOException {
                    try {
                        Date value = (Date) f.get(message);
                        if (value != null) output.writeFixed64(number, value.getTime(), false);
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                public void transfer(Pipe pipe, Input input, Output output, boolean repeated) throws IOException {
                    output.writeFixed64(number, input.readFixed64(), repeated);
                }
            };
        }
