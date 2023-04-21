    protected ByteArrayOutputStream getStream(DataHolder aDataReceiver) throws IOException {
        ByteArrayOutputStream stream = (ByteArrayOutputStream) connections.get(aDataReceiver.getChannel());
        if (stream == null) {
            stream = new ByteArrayOutputStream();
            updateStream(aDataReceiver.getChannel(), stream);
        }
        stream.write(aDataReceiver.getData());
        return stream;
    }
