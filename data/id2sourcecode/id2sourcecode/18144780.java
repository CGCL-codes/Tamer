    public void detail(int houseNum) throws IOException {
        if (!Desktop.isDesktopSupported()) {
            format("'print' is not supported on this platofrm.\n");
            return;
        }
        tmpFile = File.createTempFile("NDFBrowser", ".txt");
        tmpFile.deleteOnExit();
        out = new PrintWriter(tmpFile);
        super.detail(houseNum);
        out.flush();
        out.close();
        Desktop.getDesktop().print(tmpFile);
        tmpFile = null;
    }
