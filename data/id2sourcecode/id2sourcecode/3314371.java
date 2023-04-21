    public void addFile(String fname, File file) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            zip.putNextEntry(new ZipEntry(fname));
            copy(in, zip);
            zip.closeEntry();
        } catch (IOException e) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }
