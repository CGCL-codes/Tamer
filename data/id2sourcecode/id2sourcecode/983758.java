    public boolean accept(File pathname) {
        return reader != null && reader.accept(pathname) || writer != null && writer.accept(pathname);
    }
