    public static void downloadFile(String sourceUrl, String localFileName) throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        if ((sourceUrl.toLowerCase().startsWith("http")) || (sourceUrl.toLowerCase().startsWith("ftp"))) {
            URL url = new URL(sourceUrl);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();
        } else {
            inputStream = new FileInputStream(sourceUrl);
        }
        File outputFile = new File(localFileName);
        if (outputFile.exists()) {
            outputFile.delete();
        }
        outputFile.getParentFile().mkdir();
        outputStream = new FileOutputStream(outputFile);
        IOHelper.copyContents(inputStream, outputStream);
    }
