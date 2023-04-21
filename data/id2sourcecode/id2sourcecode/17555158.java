    public void run() throws IOException {
        BufferedOutputStream outputFile = null;
        BufferedInputStream inputStream = null;
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        int connectLength = connection.getContentLength();
        if (size == -1) {
            size = connectLength;
        }
        outputFile = new BufferedOutputStream(new FileOutputStream(getNameOfFile()));
        inputStream = (BufferedInputStream) connection.getInputStream();
        byte[] bufer = new byte[MAX_SIZE_OF_BUFFER];
        while (inputStream.read(bufer) != -1) {
            outputFile.write(bufer);
        }
        if (outputFile != null) {
            outputFile.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }
