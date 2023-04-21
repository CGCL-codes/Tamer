    public FileParse(String fileStr, String type) throws MalformedURLException, IOException {
        this.inType = type;
        if (type.equals("File")) {
            this.inFile = new File(fileStr);
            this.fileSize = (int) inFile.length();
        }
        if (type.equals("URL")) {
            url = new URL(fileStr);
            this.fileSize = 0;
            urlconn = url.openConnection();
        }
    }
