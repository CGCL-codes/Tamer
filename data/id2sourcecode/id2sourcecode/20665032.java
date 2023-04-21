        public <T> Field<T> create(int number, java.lang.String name, final java.lang.reflect.Field f, IdStrategy strategy) {
            final boolean primitive = f.getType().isPrimitive();
            return new Field<T>(FieldType.DOUBLE, number, name) {

                {
                    f.setAccessible(true);
                }

                public void mergeFrom(Input input, T message) throws IOException {
                    try {
                        if (primitive) f.setDouble(message, input.readDouble()); else f.set(message, new Double(input.readDouble()));
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                public void writeTo(Output output, T message) throws IOException {
                    try {
                        if (primitive) output.writeDouble(number, f.getDouble(message), false); else {
                            Double value = (Double) f.get(message);
                            if (value != null) output.writeDouble(number, value.doubleValue(), false);
                        }
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                public void transfer(Pipe pipe, Input input, Output output, boolean repeated) throws IOException {
                    output.writeDouble(number, input.readDouble(), repeated);
                }
            };
        }
