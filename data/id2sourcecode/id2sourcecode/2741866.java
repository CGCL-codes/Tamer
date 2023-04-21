    private static void writeFile(InputStream is, String filename, String username, Context ct) throws IOException {
        String dataDir = ct.getFileStreamPath("").getAbsolutePath() + "/../" + username + "/";
        byte[] buffer = new byte[1024];
        int read;
        File file;
        String directory;
        filename = dataDir + filename;
        Log.i(I, "filename: " + filename);
        if (filename.lastIndexOf('/') != filename.length() - 1) {
            directory = filename.substring(0, filename.lastIndexOf('/'));
            file = new File(directory);
            if (!file.exists()) file.mkdirs();
        } else {
            Log.i(I, "TRIFFT NICHT ZU: if (filename.lastIndexOf('/') != filename.length() - 1) ---> ELSE aufgerufen - return.");
            return;
        }
        OutputStream os = null;
        os = new FileOutputStream(filename);
        Log.i(I, "try to write file ...");
        while ((read = is.read(buffer)) > 0) {
            os.write(buffer, 0, read);
        }
        os.close();
        is.close();
        if (file.exists()) {
            sIsSuccess = true;
            Log.i(I, "file successful! written in: " + file.getAbsolutePath());
        }
    }
