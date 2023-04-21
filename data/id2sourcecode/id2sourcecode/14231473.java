    static void transferClass(Pipe pipe, Input input, Output output, int number, Pipe.Schema<?> pipeSchema, boolean mapped, boolean array, IdStrategy strategy) throws IOException {
        strategy.transferClassId(input, output, number, mapped, array);
        if (array) {
            if (input.readFieldNumber(pipeSchema.wrappedSchema) != ID_ARRAY_DIMENSION) throw new ProtostuffException("Corrupt input.");
            output.writeUInt32(ID_ARRAY_DIMENSION, input.readUInt32(), false);
        }
    }
