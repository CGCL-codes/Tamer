                public void transfer(Pipe pipe, Input input, Output output, boolean repeated) throws IOException {
                    output.writeInt64(number, input.readInt64(), repeated);
                }
