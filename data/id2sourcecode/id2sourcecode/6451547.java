    public byte[] getAll() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            is = new BufferedInputStream(is);
            byte buffer[] = new byte[1024];
            int read;
            while ((read = is.read(buffer)) > 0) {
                baos.write(buffer, 0, read);
            }
            return baos.toByteArray();
        } finally {
            try {
                if (null != is) is.close();
            } catch (IOException e) {
            }
        }
    }
