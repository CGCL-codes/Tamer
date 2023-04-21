        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f, IdStrategy strategy) {
            final boolean primitive = f.getType().isPrimitive();
            return new Field<T>(FieldType.UINT32, number, name) {

                {
                    f.setAccessible(true);
                }

                public void mergeFrom(Input input, T message) throws IOException {
                    try {
                        if (primitive) f.setChar(message, (char) input.readUInt32()); else f.set(message, Character.valueOf((char) input.readUInt32()));
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                public void writeTo(Output output, T message) throws IOException {
                    try {
                        if (primitive) output.writeUInt32(number, f.getChar(message), false); else {
                            Character value = (Character) f.get(message);
                            if (value != null) output.writeUInt32(number, value.charValue(), false);
                        }
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                public void transfer(Pipe pipe, Input input, Output output, boolean repeated) throws IOException {
                    output.writeUInt32(number, input.readUInt32(), repeated);
                }
            };
        }
