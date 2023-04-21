    public static InputStream cleanInputStream(InputStream inputStream) {
        Reader reader = new InputStreamReader(inputStream);
        StringWriter writer = new StringWriter();
        copy(reader, writer);
        String buffer = writer.getBuffer().toString();
        buffer = buffer.replaceAll("<http", "&lt;http");
        buffer = buffer.replace("<mailto", "&lt;mailto");
        buffer = buffer.replace("<ftp", "&lt;ftp");
        buffer = buffer.replace("<wiki", "&lt;wiki");
        return new ByteArrayInputStream(buffer.getBytes());
    }
