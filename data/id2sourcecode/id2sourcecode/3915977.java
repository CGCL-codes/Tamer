    public void closePage() throws IOException {
        if (pageStream == null) {
            writeWarning("Page " + currentPage + " already closed. " + "Call openPage() to start a new one.");
            return;
        }
        writeGraphicsRestore();
        writeGraphicsRestore();
        os.close(pageStream);
        pageStream = null;
        processDelayed();
    }
