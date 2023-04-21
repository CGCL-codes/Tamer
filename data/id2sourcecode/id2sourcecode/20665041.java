        public <T> Field<T> create(int number, java.lang.String name, final java.lang.reflect.Field f, IdStrategy strategy) {
            final boolean primitive = f.getType().isPrimitive();
            return new Field<T>(FieldType.BOOL, number, name) {

                {
                    f.setAccessible(true);
                }

                public void mergeFrom(Input input, T message) throws IOException {
                    try {
                        if (primitive) f.setBoolean(message, input.readBool()); else f.set(message, input.readBool() ? Boolean.TRUE : Boolean.FALSE);
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                public void writeTo(Output output, T message) throws IOException {
                    try {
                        if (primitive) output.writeBool(number, f.getBoolean(message), false); else {
                            Boolean value = (Boolean) f.get(message);
                            if (value != null) output.writeBool(number, value.booleanValue(), false);
                        }
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                public void transfer(Pipe pipe, Input input, Output output, boolean repeated) throws IOException {
                    output.writeBool(number, input.readBool(), repeated);
                }
            };
        }
