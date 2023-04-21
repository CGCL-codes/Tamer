    public String create(File parent) throws CoreException {
        FileOutputStream out = null;
        InputStream in = null;
        try {
            String name = parent + File.separator + getPath().lastSegment();
            if (new File(name).exists()) return name;
            out = new FileOutputStream(name);
            in = getStream();
            byte[] buffer = new byte[4096];
            int read = 0;
            while ((read = in.read(buffer)) != -1) out.write(buffer, 0, read);
            return name;
        } catch (Exception e) {
            throw ZipPlugin.createException(e);
        } finally {
            Util.close(in);
            Util.close(out);
        }
    }
