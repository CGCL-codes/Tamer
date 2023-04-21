    public void show_info(String filename) throws IOException {
        File f = new File(cwd, filename);
        String info;
        if (!f.exists()) throw new IllegalArgumentException("FileHandler.show_info(): " + "no such file or directory");
        if (f.isDirectory()) info = "Directory: "; else info = "File: ";
        info += filename + " ";
        info += (f.canRead() ? "read " : "    ") + (f.canWrite() ? "write " : "    ") + f.length() + " " + new java.util.Date(f.lastModified());
        infoarea.setText(info);
    }
