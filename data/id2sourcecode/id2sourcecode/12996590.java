        protected void transfer(Pipe pipe, Input input, Output output) throws IOException {
            for (int number = input.readFieldNumber(wrappedSchema); ; number = input.readFieldNumber(wrappedSchema)) {
                switch(number) {
                    case 0:
                        return;
                    case 1:
                        output.writeInt32(number, input.readInt32(), false);
                        break;
                    case 2:
                        input.transferByteRangeTo(output, true, number, false);
                        break;
                    case 3:
                        output.writeObject(number, pipe, Baz.getPipeSchema(), false);
                        break;
                    case 4:
                        output.writeEnum(number, input.readEnum(), false);
                        break;
                    case 5:
                        input.transferByteRangeTo(output, false, number, false);
                        break;
                    case 6:
                        output.writeBool(number, input.readBool(), false);
                        break;
                    case 7:
                        output.writeFloat(number, input.readFloat(), false);
                        break;
                    case 8:
                        output.writeDouble(number, input.readDouble(), false);
                        break;
                    case 9:
                        output.writeInt64(number, input.readInt64(), false);
                        break;
                    default:
                        input.handleUnknownField(number, wrappedSchema);
                }
            }
        }
