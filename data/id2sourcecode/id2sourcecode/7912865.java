    public void dumpResult() {
        try {
            InputStream renderedInputStream = getRenderedInputStream();
            int read;
            while ((read = renderedInputStream.read()) != -1) {
                System.out.write(read);
            }
        } catch (IOException ignore) {
        }
    }
