    public InputStream openInputStream(String path) throws IOException {
        InputStream in = null;
        if (resource == null || resource.length() == 0) {
            throw new FileNotFoundException("path=" + path + "resource=" + resource);
        } else if (resource.indexOf("://") != -1) {
            URL temp_url = new URL(resource);
            {
                in = new org.fao.waicent.util.InputStreamWithLength(temp_url.openStream(), temp_url.openConnection().getContentLength());
            }
        } else if (path != null && path.indexOf("://") != -1 || (path != null && path.startsWith("file:"))) {
            URL temp_url = new URL(new URL(path), resource);
            {
                in = new org.fao.waicent.util.InputStreamWithLength(temp_url.openStream(), temp_url.openConnection().getContentLength());
            }
        } else {
            String ff = getAbsoluteFilename(path);
            {
                in = new org.fao.waicent.util.InputStreamWithLength(new FileInputStream(ff), new File(ff).length());
            }
        }
        if (resource.endsWith(".zip") || resource.endsWith(".ZIP")) {
            ZipInputStream zin = new ZipInputStream(in);
            zin.getNextEntry();
            in = zin;
        }
        return in;
    }
