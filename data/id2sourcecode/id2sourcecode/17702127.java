    public static ZipHash from(InputStream stream) throws IOException {
        ZipHash zipHash = new ZipHash();
        ZipInputStream zipInputStream = new ZipInputStream(stream);
        for (ZipEntry next = zipInputStream.getNextEntry(); next != null; next = zipInputStream.getNextEntry()) {
            if (!next.isDirectory()) {
                byte buffer[] = new byte[BUFFER_SIZE];
                ByteArrayOutputStream data = new ByteArrayOutputStream();
                int read;
                while ((read = zipInputStream.read(buffer)) != -1) {
                    data.write(buffer, 0, read);
                }
                zipHash.put(next.getName(), data.toByteArray());
            }
        }
        zipInputStream.close();
        return zipHash;
    }
