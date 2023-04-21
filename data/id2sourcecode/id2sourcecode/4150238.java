    private static void installFile(InputStream in, String instPath, String fileName, boolean update) throws IOException {
        String path = GlobalProps.getHomeDir() + File.separator + instPath;
        File instDir = new File(path);
        if (!instDir.isDirectory()) {
            instDir.mkdirs();
        }
        String inst = path + File.separator + fileName;
        File instFile = new File(inst);
        if (instFile.exists()) {
            if (update) {
                instFile.delete();
            } else {
                return;
            }
        }
        FileOutputStream fileOut = new FileOutputStream(instFile);
        int read = 0;
        byte[] buf = new byte[1024];
        while ((read = in.read(buf)) != -1) {
            fileOut.write(buf, 0, read);
        }
        in.close();
        fileOut.close();
    }
