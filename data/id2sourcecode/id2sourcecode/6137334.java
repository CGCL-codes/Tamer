    private void writeResourceImage(FileDumper fd, String path, String resource) throws IOException {
        this.console.print(this.console.r("writing-resource-entry") + ": " + resource + " => " + path);
        fd.next(path);
        InputStream is = (this.getClass()).getResourceAsStream(resource);
        if (is == null) throw new IOException("Cannot find resource \"" + resource + "\"");
        byte[] buffer = new byte[1024];
        int count;
        while ((count = is.read(buffer, 0, buffer.length)) > 0) fd.write(buffer, 0, count);
    }
