    public static byte[] getContentAsBytes(URI uri) throws IOException {
        InputStream is = null;
        try {
            is = new BufferedInputStream(getInputStream(uri));
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            int b = 0;
            while ((b = is.read()) != -1) baos.write(b);
            return baos.toByteArray();
        } finally {
            if (is != null) is.close();
        }
    }
