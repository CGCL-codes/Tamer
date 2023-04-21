    static void transferObject(Pipe.Schema<Object> pipeSchema, Pipe pipe, Input input, Output output, IdStrategy strategy) throws IOException {
        final int number = input.readFieldNumber(pipeSchema.wrappedSchema);
        switch(number) {
            case ID_EMPTY_SET:
                output.writeUInt32(number, input.readUInt32(), false);
                break;
            case ID_EMPTY_LIST:
                output.writeUInt32(number, input.readUInt32(), false);
                break;
            case ID_SINGLETON_SET:
            case ID_SINGLETON_LIST:
                {
                    output.writeUInt32(number, input.readUInt32(), false);
                    final int next = input.readFieldNumber(pipeSchema.wrappedSchema);
                    if (next == 0) {
                        return;
                    }
                    if (next != 1) throw new ProtostuffException("Corrupt input.");
                    output.writeObject(1, pipe, strategy.OBJECT_PIPE_SCHEMA, false);
                    break;
                }
            case ID_SET_FROM_MAP:
                output.writeObject(number, pipe, strategy.POLYMORPHIC_MAP_PIPE_SCHEMA, false);
                break;
            case ID_COPIES_LIST:
                {
                    output.writeUInt32(number, input.readUInt32(), false);
                    if (1 != input.readFieldNumber(pipeSchema.wrappedSchema)) throw new ProtostuffException("Corrupt input.");
                    output.writeUInt32(1, input.readUInt32(), false);
                    final int next = input.readFieldNumber(pipeSchema.wrappedSchema);
                    if (next == 0) {
                        return;
                    }
                    if (next != 2) throw new ProtostuffException("Corrupt input.");
                    output.writeObject(2, pipe, strategy.OBJECT_PIPE_SCHEMA, false);
                    break;
                }
            case ID_UNMODIFIABLE_COLLECTION:
            case ID_UNMODIFIABLE_SET:
            case ID_UNMODIFIABLE_SORTED_SET:
            case ID_UNMODIFIABLE_LIST:
            case ID_UNMODIFIABLE_RANDOM_ACCESS_LIST:
                output.writeObject(number, pipe, strategy.POLYMORPHIC_COLLECTION_PIPE_SCHEMA, false);
                break;
            case ID_SYNCHRONIZED_COLLECTION:
            case ID_SYNCHRONIZED_SET:
            case ID_SYNCHRONIZED_SORTED_SET:
            case ID_SYNCHRONIZED_LIST:
            case ID_SYNCHRONIZED_RANDOM_ACCESS_LIST:
                output.writeObject(number, pipe, strategy.POLYMORPHIC_COLLECTION_PIPE_SCHEMA, false);
                break;
            case ID_CHECKED_COLLECTION:
            case ID_CHECKED_SET:
            case ID_CHECKED_SORTED_SET:
            case ID_CHECKED_LIST:
            case ID_CHECKED_RANDOM_ACCESS_LIST:
                output.writeObject(number, pipe, strategy.POLYMORPHIC_COLLECTION_PIPE_SCHEMA, false);
                if (1 != input.readFieldNumber(pipeSchema.wrappedSchema)) throw new ProtostuffException("Corrupt input.");
                output.writeObject(1, pipe, strategy.CLASS_PIPE_SCHEMA, false);
                break;
            case ID_ENUM_SET:
                strategy.transferEnumId(input, output, number);
                if (output instanceof StatefulOutput) {
                    ((StatefulOutput) output).updateLast(strategy.COLLECTION_PIPE_SCHEMA, pipeSchema);
                }
                Pipe.transferDirect(strategy.COLLECTION_PIPE_SCHEMA, pipe, input, output);
                return;
            case ID_COLLECTION:
                strategy.transferCollectionId(input, output, number);
                if (output instanceof StatefulOutput) {
                    ((StatefulOutput) output).updateLast(strategy.COLLECTION_PIPE_SCHEMA, pipeSchema);
                }
                Pipe.transferDirect(strategy.COLLECTION_PIPE_SCHEMA, pipe, input, output);
                return;
            default:
                throw new ProtostuffException("Corrupt input.");
        }
        if (0 != input.readFieldNumber(pipeSchema.wrappedSchema)) throw new ProtostuffException("Corrupt input.");
    }
