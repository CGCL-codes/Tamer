    private String readFileAsString() throws IOException {
        FileInputStream in = new FileInputStream(filePath);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copy(in, out);
        return new String(out.toByteArray(), charset);
    }
