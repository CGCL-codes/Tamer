        public void transfer(Pipe pipe, Input input, Output output, int number, boolean repeated) throws IOException {
            output.writeInt32(number, input.readInt32(), repeated);
        }
