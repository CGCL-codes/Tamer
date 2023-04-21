    protected static File copyTestDocument() throws Exception {
        File file = File.createTempFile("zodiak-test-", null);
        InputStream in = OpenDocumentFileTest.class.getResourceAsStream(SAMPLE_TEXT_DOC);
        OutputStream out = new FileOutputStream(file);
        int b;
        while ((b = in.read()) != -1) out.write(b);
        in.close();
        out.close();
        return file;
    }
