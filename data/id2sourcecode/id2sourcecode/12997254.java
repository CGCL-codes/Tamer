    public static File downloadFileAsTmp(String fileURL, String tmpFilePrefix) throws MalformedURLException, FileNotFoundException, IOException {
        File tmpFile = null;
        InputStream inStream = null;
        OutputStream outStream = null;
        try {
            tmpFile = File.createTempFile(tmpFilePrefix, null, new File(UpdaterConstants.TMP_FILE_FOLDER));
            final URL url = new URL(fileURL);
            final URLConnection connection = url.openConnection();
            inStream = new BufferedInputStream(connection.getInputStream());
            outStream = new BufferedOutputStream(new FileOutputStream(tmpFile));
            byte[] buffer = new byte[32768];
            int bufferSize;
            while ((bufferSize = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bufferSize);
            }
        } finally {
            if (outStream != null) outStream.close();
            if (inStream != null) inStream.close();
        }
        return tmpFile;
    }
