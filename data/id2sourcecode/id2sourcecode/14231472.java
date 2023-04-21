    static void transferArray(Pipe pipe, Input input, Output output, int number, Pipe.Schema<?> pipeSchema, boolean mapped, IdStrategy strategy) throws IOException {
        strategy.transferArrayId(input, output, number, mapped);
        if (input.readFieldNumber(pipeSchema.wrappedSchema) != ID_ARRAY_LEN) throw new ProtostuffException("Corrupt input.");
        output.writeUInt32(ID_ARRAY_LEN, input.readUInt32(), false);
        if (input.readFieldNumber(pipeSchema.wrappedSchema) != ID_ARRAY_DIMENSION) throw new ProtostuffException("Corrupt input.");
        output.writeUInt32(ID_ARRAY_DIMENSION, input.readUInt32(), false);
        if (output instanceof StatefulOutput) {
            ((StatefulOutput) output).updateLast(strategy.ARRAY_PIPE_SCHEMA, pipeSchema);
        }
        Pipe.transferDirect(strategy.ARRAY_PIPE_SCHEMA, pipe, input, output);
    }
