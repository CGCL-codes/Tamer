    private String readZIPEntry(ZipInputStream zipInputStream, int size) throws IOException {
        int bytesRead;
        BufferedInputStream buffer = new BufferedInputStream(zipInputStream);
        ByteArrayOutputStream content = (size > 0) ? (new ByteArrayOutputStream(size)) : (new ByteArrayOutputStream());
        byte data[] = new byte[BUFFER_SIZE];
        while ((bytesRead = buffer.read(data, 0, BUFFER_SIZE)) != -1) content.write(data, 0, bytesRead);
        return content.toString("UTF-8");
    }
