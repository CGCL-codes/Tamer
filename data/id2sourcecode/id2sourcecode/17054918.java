    public static void unZipURLToDirectory(java.net.URL url, String sDirUnzip) throws IOException {
        FileOutputStream fw = null;
        BufferedWriter bw = null;
        File dir = new File(sDirUnzip);
        if (!dir.exists()) {
            dir.mkdir();
        }
        URLConnection connection = url.openConnection();
        ZipInputStream input = new ZipInputStream(connection.getInputStream());
        ZipEntry zipEntry = input.getNextEntry();
        while (zipEntry != null) {
            File file = new File(sDirUnzip + "/" + zipEntry.toString());
            if (zipEntry.isDirectory()) {
                file.mkdir();
            } else {
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                while (true) {
                    byte[] bytes = new byte[4096];
                    int read = input.read(bytes);
                    if (read == -1) {
                        break;
                    }
                    bout.write(bytes, 0, read);
                }
                CheckedInputStream cis = new CheckedInputStream(new ByteArrayInputStream(bout.toByteArray()), new CRC32());
                fw = new FileOutputStream(file);
                while (true) {
                    byte[] bytes = new byte[4096];
                    int read = cis.read(bytes);
                    if (read < 0) {
                        break;
                    }
                    fw.write(bytes, 0, read);
                }
                fw.close();
            }
            zipEntry = input.getNextEntry();
        }
        input.close();
    }
