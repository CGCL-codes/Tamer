    public static void downloadFile(String httpUrl, File targetFile) throws Exception {
        URL url;
        InputStream inputStream = null;
        DataInputStream dataInputStream;
        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(targetFile));
        try {
            url = new URL(httpUrl);
            inputStream = url.openStream();
            dataInputStream = new DataInputStream(new BufferedInputStream(inputStream));
            byte[] bytes = new byte[4096];
            int bytesRead = dataInputStream.read(bytes);
            while (bytesRead > 0) {
                dataOutputStream.write(bytes, 0, bytesRead);
                bytesRead = dataInputStream.read(bytes);
            }
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ioe) {
            }
        }
    }
