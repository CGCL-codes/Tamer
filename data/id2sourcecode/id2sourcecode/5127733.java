    public void writeClientOutputIndex(final OctaneClientHandlerThread self) {
        System.out.println("INFO: OctaneClientHandler/writeClientOutputIndex : Reading index.html");
        System.out.println("INFO: OctaneClientHandler/writeClientOutputIndex : " + this.requestState);
        System.out.println("INFO: OctaneClientHandler/writeClientOutputIndex htdocs : " + this.getHtdocsDir());
        Throwable lastErrorReadHtml = null;
        try {
            readWriteHtmlDoc();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            lastErrorReadHtml = e;
        }
        if (lastErrorReadHtml != null) {
            this.writeDefaultIndexPage();
        }
    }
