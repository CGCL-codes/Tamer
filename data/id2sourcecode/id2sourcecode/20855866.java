    private void writeDataElement(DataTypes dataType, int[] data) throws IOException {
        int numberOfBytes = dataType.getSize() * data.length;
        writeDataElementHeader(dataType, numberOfBytes);
        bb.clear();
        bb.asIntBuffer().put(data);
        bb.position(bb.position() + numberOfBytes);
        if (data.length % 2 != 0) {
            bb.putInt(0);
        }
        bb.flip();
        fileWriter.getChannel().write(bb);
    }
