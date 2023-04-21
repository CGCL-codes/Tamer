    public void putLongFile(File f) throws IOException {
        if (f != null) {
            FileInputStream fis = new FileInputStream(f);
            FileChannel fic = fis.getChannel();
            putLongFileChannel(fic, fic.size());
            fic.close();
        } else {
            putLong(0);
        }
    }
