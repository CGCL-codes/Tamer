    protected void init(String path) {
        in = null;
        try {
            in = new RandomAccessFile(path, "r");
            channel = in.getChannel();
            channel.position(0);
        } catch (Exception e) {
            Console.printError("unable to read \"%s\"", path);
            return;
        }
        x = y = z = 0;
        switch(hdr.datatype) {
            case DT_SIGNED_SHORT:
                buffer = ByteBuffer.allocate(2);
                break;
            case DT_SIGNED_INT:
                buffer = ByteBuffer.allocate(4);
                break;
            case DT_UNSIGNED_CHAR:
                buffer = ByteBuffer.allocate(1);
                break;
            default:
                throw new JismError("unsupported data type");
        }
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.position(0);
    }
