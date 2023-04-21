    private InputStream getConfigFileInputStream(String zipFilePath) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipEntry zipEntry;
        byte[] buffer = new byte[1024];
        int read;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            if (zipEntry.getName().equals("META-INF" + File.separator + "config.xml")) {
                while ((read = zipInputStream.read(buffer)) > 0) {
                    byteArrayOutputStream.write(buffer, 0, read);
                }
            }
        }
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }
