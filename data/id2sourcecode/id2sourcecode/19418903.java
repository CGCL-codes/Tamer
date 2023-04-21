    public long getFileLength(String fName) throws IOException {
        long length = 0;
        if (isURL) {
            URL url = new URL(getFullFileNamePath(fName));
            URLConnection c = url.openConnection();
            length = c.getContentLength();
        } else {
            File f = new File(sysFn(getFullFileNamePath(fName)));
            length = f.length();
        }
        return length;
    }
