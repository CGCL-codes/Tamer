    private String getPageText(WikiPage page, String fileName) throws IOException {
        File file = new File(fileName);
        Date createdDate = new Date(file.lastModified());
        page.setCreatedTime(createdDate);
        page.setLastModified(createdDate);
        FileInputStream reader = new FileInputStream(file);
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        Utils.copyInput2Output(reader, writer);
        return new String(writer.toByteArray(), "UTF-8");
    }
