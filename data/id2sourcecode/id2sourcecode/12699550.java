    public void start() throws Exception {
        writer.setHeader(reader.getHeader());
        run();
        reader.close();
        writer.close();
    }
