    private void download(String srcDir, String destDir, String filename) {
        System.out.println("download : " + AMHMConst.AMHM_URL + srcDir + "/" + filename + " to " + destDir + "/" + filename);
        try {
            URL url = new URL(AMHMConst.AMHM_URL + srcDir + "/" + filename);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            File file = new File(destDir + "/");
            file.mkdirs();
            file = new File(destDir + "/" + filename);
            FileOutputStream os = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int numRead;
            long numWritten = 0;
            while ((numRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, numRead);
                numWritten += numRead;
            }
            is.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
