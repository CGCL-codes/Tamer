    static void transferObject(Pipe.Schema<Object> pipeSchema, Pipe pipe, Input input, Output output, IdStrategy strategy) throws IOException {
        final int number = input.readFieldNumber(pipeSchema.wrappedSchema);
        switch(number) {
            case ID_BOOL:
                BOOL.transfer(pipe, input, output, number, false);
                break;
            case ID_BYTE:
                BYTE.transfer(pipe, input, output, number, false);
                break;
            case ID_CHAR:
                CHAR.transfer(pipe, input, output, number, false);
                break;
            case ID_SHORT:
                SHORT.transfer(pipe, input, output, number, false);
                break;
            case ID_INT32:
                INT32.transfer(pipe, input, output, number, false);
                break;
            case ID_INT64:
                INT64.transfer(pipe, input, output, number, false);
                break;
            case ID_FLOAT:
                FLOAT.transfer(pipe, input, output, number, false);
                break;
            case ID_DOUBLE:
                DOUBLE.transfer(pipe, input, output, number, false);
                break;
            case ID_STRING:
                STRING.transfer(pipe, input, output, number, false);
                break;
            case ID_BYTES:
                BYTES.transfer(pipe, input, output, number, false);
                break;
            case ID_BYTE_ARRAY:
                BYTE_ARRAY.transfer(pipe, input, output, number, false);
                break;
            case ID_BIGDECIMAL:
                BIGDECIMAL.transfer(pipe, input, output, number, false);
                break;
            case ID_BIGINTEGER:
                BIGINTEGER.transfer(pipe, input, output, number, false);
                break;
            case ID_DATE:
                DATE.transfer(pipe, input, output, number, false);
                break;
            case ID_ARRAY:
                transferArray(pipe, input, output, number, pipeSchema, false, strategy);
                return;
            case ID_OBJECT:
                output.writeUInt32(number, input.readUInt32(), false);
                break;
            case ID_ARRAY_MAPPED:
                transferArray(pipe, input, output, number, pipeSchema, true, strategy);
                return;
            case ID_CLASS:
                transferClass(pipe, input, output, number, pipeSchema, false, false, strategy);
                break;
            case ID_CLASS_MAPPED:
                transferClass(pipe, input, output, number, pipeSchema, true, false, strategy);
                break;
            case ID_CLASS_ARRAY:
                transferClass(pipe, input, output, number, pipeSchema, false, true, strategy);
                break;
            case ID_CLASS_ARRAY_MAPPED:
                transferClass(pipe, input, output, number, pipeSchema, true, true, strategy);
                break;
            case ID_ENUM:
                strategy.transferEnumId(input, output, number);
                if (input.readFieldNumber(pipeSchema.wrappedSchema) != ID_ENUM_VALUE) throw new ProtostuffException("Corrupt input.");
                EnumIO.transfer(pipe, input, output, 1, false);
                break;
            case ID_ENUM_SET:
                strategy.transferEnumId(input, output, number);
                if (output instanceof StatefulOutput) {
                    ((StatefulOutput) output).updateLast(strategy.COLLECTION_PIPE_SCHEMA, pipeSchema);
                }
                Pipe.transferDirect(strategy.COLLECTION_PIPE_SCHEMA, pipe, input, output);
                return;
            case ID_ENUM_MAP:
                strategy.transferEnumId(input, output, number);
                if (output instanceof StatefulOutput) {
                    ((StatefulOutput) output).updateLast(strategy.MAP_PIPE_SCHEMA, pipeSchema);
                }
                Pipe.transferDirect(strategy.MAP_PIPE_SCHEMA, pipe, input, output);
                return;
            case ID_COLLECTION:
                strategy.transferCollectionId(input, output, number);
                if (output instanceof StatefulOutput) {
                    ((StatefulOutput) output).updateLast(strategy.COLLECTION_PIPE_SCHEMA, pipeSchema);
                }
                Pipe.transferDirect(strategy.COLLECTION_PIPE_SCHEMA, pipe, input, output);
                return;
            case ID_MAP:
                strategy.transferMapId(input, output, number);
                if (output instanceof StatefulOutput) {
                    ((StatefulOutput) output).updateLast(strategy.MAP_PIPE_SCHEMA, pipeSchema);
                }
                Pipe.transferDirect(strategy.MAP_PIPE_SCHEMA, pipe, input, output);
                return;
            case ID_POLYMORPHIC_COLLECTION:
                if (0 != input.readUInt32()) throw new ProtostuffException("Corrupt input.");
                output.writeUInt32(number, 0, false);
                if (output instanceof StatefulOutput) {
                    ((StatefulOutput) output).updateLast(strategy.POLYMORPHIC_COLLECTION_PIPE_SCHEMA, pipeSchema);
                }
                Pipe.transferDirect(strategy.POLYMORPHIC_COLLECTION_PIPE_SCHEMA, pipe, input, output);
                return;
            case ID_POLYMORPHIC_MAP:
                if (0 != input.readUInt32()) throw new ProtostuffException("Corrupt input.");
                output.writeUInt32(number, 0, false);
                if (output instanceof StatefulOutput) {
                    ((StatefulOutput) output).updateLast(strategy.POLYMORPHIC_MAP_PIPE_SCHEMA, pipeSchema);
                }
                Pipe.transferDirect(strategy.POLYMORPHIC_MAP_PIPE_SCHEMA, pipe, input, output);
                return;
            case ID_DELEGATE:
                {
                    final Delegate<Object> delegate = strategy.transferDelegateId(input, output, number);
                    if (1 != input.readFieldNumber(pipeSchema.wrappedSchema)) throw new ProtostuffException("Corrupt input.");
                    delegate.transfer(pipe, input, output, 1, false);
                    break;
                }
            case ID_THROWABLE:
                PolymorphicThrowableSchema.transferObject(pipeSchema, pipe, input, output, strategy, number);
                return;
            case ID_POJO:
                final Pipe.Schema<Object> derivedPipeSchema = strategy.transferPojoId(input, output, number).getPipeSchema();
                if (output instanceof StatefulOutput) {
                    ((StatefulOutput) output).updateLast(derivedPipeSchema, pipeSchema);
                }
                Pipe.transferDirect(derivedPipeSchema, pipe, input, output);
                return;
            default:
                throw new ProtostuffException("Corrupt input.  Unknown field number: " + number);
        }
        if (input.readFieldNumber(pipeSchema.wrappedSchema) != 0) throw new ProtostuffException("Corrupt input.");
    }
