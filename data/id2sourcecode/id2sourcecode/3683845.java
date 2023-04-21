    public void readToken(FileToken token, String absoluteFileName) {
        try {
            FileChannel fileChannel = new RandomAccessFile(absoluteFileName, "r").getChannel();
            Long newLength = config.getMaxTokenLength();
            if (config.getMaxTokenLength() + token.getOffset() > token.getFileLength()) {
                newLength = token.getFileLength() - token.getOffset();
            }
            MappedByteBuffer targetBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, token.getOffset(), newLength);
            token.setData(new byte[newLength.intValue()]);
            token.setLength(newLength);
            targetBuffer.get(token.getData());
            fileChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
