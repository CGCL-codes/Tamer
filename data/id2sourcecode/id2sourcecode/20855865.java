    private void writeDataElement(DataTypes dataType, byte[] data) throws IOException {
        writeDataElementHeader(dataType, data.length);
        bb.clear();
        bb.put(data);
        if (data.length % 8 != 0) {
            int numberOfPadBytes = 8 - (data.length % 8);
            for (int i = 0; i < numberOfPadBytes; i++) {
                bb.put((byte) 0);
            }
        }
        bb.flip();
        fileWriter.getChannel().write(bb);
    }
