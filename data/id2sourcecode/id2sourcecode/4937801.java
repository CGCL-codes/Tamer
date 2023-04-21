    public long receiveFileAndSaveTo(long length, File saveHere) {
        try {
            FileOutputStream source = new FileOutputStream(saveHere);
            long b = source.getChannel().transferFrom(channel, 0, length);
            source.flush();
            source.close();
            return b;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
