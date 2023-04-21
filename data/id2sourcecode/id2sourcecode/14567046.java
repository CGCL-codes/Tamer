    protected static ByteBuffer loadFileIntoMemory(String filename) {
        try {
            InputStream is = Gosu.class.getResourceAsStream("/" + filename);
            if (is == null && new File(filename).exists()) {
                is = new FileInputStream(new File(filename));
            }
            BufferedInputStream input = new BufferedInputStream(is);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] bytes = new byte[4096];
            int read;
            while ((read = input.read(bytes, 0, bytes.length)) != -1) {
                output.write(bytes, 0, read);
            }
            input.close();
            ByteBuffer buffer = BufferUtils.newByteBuffer(output.size());
            buffer.put(output.toByteArray());
            buffer.rewind();
            return buffer;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
