    public int read(InputStream is) {
        int writeCurPos = getWriteIndex();
        while (true) {
            try {
                int len = is.read(buffer, getWriteIndex(), writeableBytes());
                if (len <= 0) {
                    break;
                }
                write_index += len;
            } catch (IOException e) {
                break;
            }
        }
        return getWriteIndex() - writeCurPos;
    }
