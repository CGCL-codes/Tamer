    public final byte[] getLocalByteFileNIO(File file) {
        FileInputStream fis = null;
        FileChannel fFileChannel = null;
        try {
            byte result[] = new byte[(int) file.length()];
            fis = new FileInputStream(file);
            fFileChannel = fis.getChannel();
            int length = (int) file.length();
            ByteBuffer buf = getByteBuffer(nio_segment_size);
            buf.rewind();
            int numRead = 0;
            int total = 0;
            while ((numRead >= 0) && (total < length)) {
                numRead = fFileChannel.read(buf);
                if (numRead > 0) {
                    buf.flip();
                    buf.get(result, total, numRead);
                    buf.flip();
                }
                total += numRead;
                buf.rewind();
            }
            return result;
        } catch (Exception e) {
            Debug.debug(e);
        } finally {
            try {
                if (fFileChannel != null) fFileChannel.close();
                if (fis != null) fis.close();
            } catch (Exception e) {
                Debug.debug(e);
            }
        }
        return null;
    }
