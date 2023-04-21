    private byte[] readExamplePdf() throws IOException {
        InputStream stream = this.getClass().getResourceAsStream("/faxDE.pdf");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int count = -1;
        byte[] data = new byte[1024];
        while ((count = stream.read(data)) != -1) out.write(data, 0, count);
        return out.toByteArray();
    }
