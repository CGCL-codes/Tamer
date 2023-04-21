    private void installFile(InputStream in, String instPath, String fileName, boolean update) throws IOException {
        instPath = instPath.replaceAll("/", File.separator);
        String path = GlobalProps.getHomeDir() + File.separator + instPath;
        File instDir = new File(path);
        if (!instDir.isDirectory()) {
            instDir.mkdirs();
        }
        byte[] buf = new byte[1024];
        String inst = path + File.separator + fileName;
        File instFile = new File(inst);
        if (update && instFile.exists()) {
            instFile.delete();
        }
        FileOutputStream fileOut = new FileOutputStream(instFile);
        int read = 0;
        while ((read = in.read(buf)) != -1) {
            fileOut.write(buf, 0, read);
        }
        in.close();
        fileOut.close();
    }
