    private void finishDoubleArray() throws IOException {
        int pos = (int) fileWriter.getFilePointer();
        fileWriter.seek(128 + 4);
        bb.clear();
        bb.putInt(pos - 136);
        bb.flip();
        fileWriter.getChannel().write(bb);
        fileWriter.seek(128 + 8 + 16 + 12);
        bb.clear();
        bb.putInt(cols);
        bb.flip();
        fileWriter.getChannel().write(bb);
        int numberOfDataBytes = (int) (pos - dataStartPosition);
        fileWriter.seek(dataStartPosition - 4);
        bb.clear();
        bb.putInt(numberOfDataBytes);
        bb.flip();
        fileWriter.getChannel().write(bb);
        fileWriter.close();
    }
