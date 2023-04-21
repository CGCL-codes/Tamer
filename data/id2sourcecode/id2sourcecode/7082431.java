    public synchronized long transferFromStream(InputStream in) {
        byte[] buf = Util.takeThreadLocalBuf();
        try {
            long position = moveToEndPosition();
            file.writeInt(-1);
            int length = 0;
            int bytesRead;
            while ((bytesRead = in.read(buf)) != -1) {
                file.write(buf, 0, bytesRead);
                length += bytesRead;
            }
            file.seek(position);
            file.writeInt(length);
            atEnd = false;
            return position;
        } catch (IOException e) {
            throw new RuntimeException("Unable to read from byte cache", e);
        } finally {
            Util.releaseThreadLocalBuf(buf);
        }
    }
