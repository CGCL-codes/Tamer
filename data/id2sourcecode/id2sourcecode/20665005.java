        public <T> Field<T> create(int number, java.lang.String name, final java.lang.reflect.Field f, IdStrategy strategy) {
            final boolean primitive = f.getType().isPrimitive();
            return new Field<T>(FieldType.INT32, number, name) {

                {
                    f.setAccessible(true);
                }

                public void mergeFrom(Input input, T message) throws IOException {
                    try {
                        if (primitive) f.setInt(message, input.readInt32()); else f.set(message, Integer.valueOf(input.readInt32()));
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                public void writeTo(Output output, T message) throws IOException {
                    try {
                        if (primitive) output.writeInt32(number, f.getInt(message), false); else {
                            Integer value = (Integer) f.get(message);
                            if (value != null) output.writeInt32(number, value.intValue(), false);
                        }
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                public void transfer(Pipe pipe, Input input, Output output, boolean repeated) throws IOException {
                    output.writeInt32(number, input.readInt32(), repeated);
                }
            };
        }
