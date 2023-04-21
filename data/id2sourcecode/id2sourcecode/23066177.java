    public FileParse(String fileStr, String type) throws MalformedURLException, IOException {
        this.inType = type;
        System.out.println("Input type = " + type);
        System.out.println("Input name = " + fileStr);
        if (type.equals("File")) {
            this.inFile = new File(fileStr);
            this.fileSize = (int) inFile.length();
            System.out.println("File: " + inFile);
            System.out.println("Bytes: " + fileSize);
        }
        if (type.equals("URL")) {
            url = new URL(fileStr);
            this.fileSize = 0;
            urlconn = url.openConnection();
        }
    }
