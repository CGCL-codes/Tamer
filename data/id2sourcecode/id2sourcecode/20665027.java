        public void transfer(Pipe pipe, Input input, Output output, int number, boolean repeated) throws IOException {
            output.writeFloat(number, input.readFloat(), repeated);
        }
