    public Resource createNew(String newName, InputStream in, Long length, String contentType) throws IOException {
        File f = new File(folder.getPath() + File.separator + newName);
        if (!f.exists()) {
            String p = f.getPath().substring(0, f.getPath().lastIndexOf(File.separator));
            File fe = new File(p);
            fe.mkdirs();
            f.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(f);
        byte[] buf = new byte[256];
        int read = -1;
        while ((read = in.read()) != -1) {
            fos.write(read);
        }
        TempFileResourceImpl tr = new TempFileResourceImpl(f, f.getPath(), isAutoPub);
        return tr;
    }
