    byte[] loadBytesFromStream(InputStream in) throws IOException {
        int chunkSize = 4096;
        int count;
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        byte[] b = new byte[chunkSize];
        try {
            while ((count = in.read(b, 0, chunkSize)) > 0) bo.write(b, 0, count);
            return bo.toByteArray();
        } finally {
            bo.close();
            bo = null;
        }
    }
