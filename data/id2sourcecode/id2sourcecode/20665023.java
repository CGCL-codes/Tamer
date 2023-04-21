        public <T> Field<T> create(int number, java.lang.String name, final java.lang.reflect.Field f, IdStrategy strategy) {
            final boolean primitive = f.getType().isPrimitive();
            return new Field<T>(FieldType.FLOAT, number, name) {

                {
                    f.setAccessible(true);
                }

                public void mergeFrom(Input input, T message) throws IOException {
                    try {
                        if (primitive) f.setFloat(message, input.readFloat()); else f.set(message, new Float(input.readFloat()));
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                public void writeTo(Output output, T message) throws IOException {
                    try {
                        if (primitive) output.writeFloat(number, f.getFloat(message), false); else {
                            Float value = (Float) f.get(message);
                            if (value != null) output.writeFloat(number, value.floatValue(), false);
                        }
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                public void transfer(Pipe pipe, Input input, Output output, boolean repeated) throws IOException {
                    output.writeFloat(number, input.readFloat(), repeated);
                }
            };
        }
