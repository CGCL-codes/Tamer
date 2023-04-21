    public void putFile(File f) throws IOException {
        if (f != null) {
            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                FileChannel fic = fis.getChannel();
                putFileChannel(fic, fic.size());
                fic.close();
            } else {
                putLong(0);
            }
        } else {
            putLong(0);
        }
    }
