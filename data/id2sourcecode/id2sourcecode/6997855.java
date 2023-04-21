    private void writeFile(InputStream is, String filename) throws IOException {
        dataDir = getFileStreamPath("").getAbsolutePath() + "/../neu-erstellter-ordner/";
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
            return;
        }
        OutputStream os = null;
        os = new FileOutputStream(filename);
        while ((read = is.read(buffer)) > 0) {
            Log.i(I, "try to write file ...");
            os.write(buffer, 0, read);
        }
        os.close();
        is.close();
    }
