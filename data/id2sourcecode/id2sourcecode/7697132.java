    public void readContent(String src, String result) throws IOException {
        PdfReader reader = new PdfReader(src);
        FileOutputStream out = new FileOutputStream(result);
        out.write(reader.getPageContent(1));
        out.flush();
        out.close();
    }
