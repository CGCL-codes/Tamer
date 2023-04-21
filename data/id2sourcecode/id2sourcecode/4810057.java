    public static File webget(String urlString, String filename) throws MalformedURLException, IOException, FileNotFoundException {
        InputStream in = null;
        FileOutputStream out = null;
        URL url = new URL(urlString);
        File destFile = null;
        if (filename == null) {
            String inputStr = url.getFile();
            destFile = new File(inputStr.replaceAll(".*[\\/](.*)$", "$1"));
        } else if (new File(filename).isDirectory()) {
            String inputStr = url.getFile();
            destFile = new File(filename + "/" + inputStr.replaceAll(".*[\\/](.*)$", "$1"));
        } else {
            destFile = new File(filename);
        }
        URLConnection conn = url.openConnection();
        long len = conn.getContentLength();
        String contentLen;
        if (len < 0) {
            contentLen = "Unknown Length";
        } else {
            contentLen = len + " byte" + ((len > 1) ? "s" : "");
        }
        System.out.println("Downloading " + destFile.getName() + " " + contentLen);
        in = url.openStream();
        out = new FileOutputStream(destFile);
        copyStream(in, out, len);
        System.out.println("Download completed.");
        return destFile;
    }
