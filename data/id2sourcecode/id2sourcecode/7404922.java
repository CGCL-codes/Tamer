    public static void main(String[] args) {
        FileDialog fd = new FileDialog(new Frame(), "Find a .zip file", FileDialog.LOAD);
        ZipFile ca;
        Enumeration entries;
        OutputStream out;
        InputStream in;
        File out_file, out_dir;
        ZipEntry ce;
        byte[] buffer = new byte[2048];
        int bytes_read;
        fd.show();
        if (fd.getFile() != null) {
            try {
                ca = new ZipFile(new File(fd.getDirectory(), fd.getFile()));
                entries = ca.entries();
                out_dir = new File(fd.getDirectory(), fd.getFile() + " folder");
                out_dir.mkdir();
                while (entries.hasMoreElements()) {
                    ce = (ZipEntry) entries.nextElement();
                    out_file = new File(out_dir, ce.getName());
                    FileUtils.createParents(out_file);
                    out = new FileOutputStream(out_file);
                    in = ca.getInputStream(ce);
                    while ((bytes_read = in.read(buffer)) != -1) out.write(buffer, 0, bytes_read);
                    in.close();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
