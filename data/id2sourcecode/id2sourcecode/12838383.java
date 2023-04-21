    public OutputStream openOutputStream(String path) throws IOException {
        OutputStream out = null;
        if (resource == null || resource.length() == 0) {
            throw new FileNotFoundException("path=" + path + "resource=" + resource);
        } else if (resource.indexOf("://") != -1 || (path != null && path.indexOf("://") != -1)) {
            throw new IOException("Can't write to a URL: path=" + path + "resource=" + resource);
        } else {
            String abs = getAbsoluteFilename(path);
            File file = new File(abs);
            if (file.getParent() != null) {
                File parent = new File(file.getParent());
                parent.mkdirs();
            }
            out = new FileOutputStream(file);
        }
        if (resource.endsWith(".zip") || resource.endsWith(".ZIP")) {
            ZipOutputStream zout = new ZipOutputStream(out);
            zout.putNextEntry(new ZipEntry(name));
            out = zout;
        }
        return out;
    }
