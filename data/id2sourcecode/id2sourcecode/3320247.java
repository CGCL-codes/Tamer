    @Override
    public MethodResult runMethod(String method, String userID, Vector<Byte> args) {
        ByteStreamReader reader = new ByteStreamReader(args);
        MethodResult result = new MethodResult();
        if (method.equals("add")) {
            synchronized (mutex) {
                writer.writeInteger(add(reader.readInteger(), reader.readInteger()));
                result.status = MessageHeader.SUCCESS_STATUS;
                result.result = writer.toVector();
                writer.reset();
            }
            return result;
        } else if (method.equals("print")) {
            String res = reader.readUTF8String();
            print(res);
            result.status = MessageHeader.SUCCESS_STATUS;
            return result;
        } else if (method.equals("event_test")) {
            Event evt = new Event();
            evt.unmarshall(reader);
            event_test(evt);
            result.status = MessageHeader.SUCCESS_STATUS;
            return result;
        }
        result.status = MessageHeader.OPERATION_NOT_EXIST_STATUS;
        return result;
    }
