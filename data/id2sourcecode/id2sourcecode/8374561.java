    public static void receive_file(Client client, String filePath, InputStream is) throws IOException {
        File rootDir = getTempReceiveDir(client);
        File targetFile = new File(rootDir, filePath);
        targetFile.createNewFile();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetFile);
            int len;
            byte[] buffer = new byte[131072];
            while ((len = is.read(buffer)) != -1) fos.write(buffer, 0, len);
        } catch (IOException e) {
            throw e;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
