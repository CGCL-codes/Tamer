    private byte[] getMediaData(Media media) {
        byte[] bytes = null;
        if (media.inMemory()) bytes = media.getByteData(); else {
            InputStream is = media.getStreamData();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1000];
            int byteread = 0;
            try {
                while ((byteread = is.read(buf)) != -1) baos.write(buf, 0, byteread);
            } catch (IOException e) {
                log.log(Level.SEVERE, e.getLocalizedMessage(), e);
                throw new IllegalStateException(e.getLocalizedMessage());
            }
            bytes = baos.toByteArray();
        }
        return bytes;
    }
