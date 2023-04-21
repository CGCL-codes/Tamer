    private void writeDataElementHeader(DataTypes dataType, int numberOfBytes) throws IOException {
        bb.clear();
        bb.putInt(dataType.getId());
        bb.putInt(numberOfBytes);
        bb.flip();
        fileWriter.getChannel().write(bb);
    }
