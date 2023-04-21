    public static ByteBuffer loadMediaIntoMemory(String media) {
        try {
            InputStream is = new Medias().getClass().getResourceAsStream(media);
            if (is == null) {
                if (new File(media).exists()) {
                    is = new FileInputStream(new File(media));
                } else if (new File("." + media).exists()) {
                    is = new FileInputStream(new File("." + media));
                } else {
                    throw new FileNotFoundException(media);
                }
            }
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[4 * 1024];
            int read;
            while ((read = bis.read(bytes, 0, bytes.length)) != -1) {
                if (read > 0) {
                    baos.write(bytes, 0, read);
                }
            }
            bis.close();
            baos.close();
            ByteBuffer buffer = BufferUtils.newByteBuffer(baos.size());
            buffer.put(baos.toByteArray());
            buffer.rewind();
            lastError = "";
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
            lastError = e.getMessage();
            return null;
        }
    }
