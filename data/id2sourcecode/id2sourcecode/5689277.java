    public InputStream getFile(String filename) throws IOException {
        String localFileName = (java.io.File.separatorChar != '/') ? filename.replace('/', java.io.File.separatorChar) : filename;
        for (int i = 0; i < dirs.length; i++) {
            if (urlzips[i] != null) {
                ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(urlzips[i]));
                ZipEntry ze;
                String fullname = zipDirs[i] != null ? zipDirs[i] + filename : filename;
                while ((ze = zis.getNextEntry()) != null) {
                    if (ze.getName().equals(fullname)) {
                        return zis;
                    }
                    zis.closeEntry();
                }
                continue;
            }
            if (bases[i] != null) {
                try {
                    URL url = new URL(bases[i], filename);
                    URLConnection conn = url.openConnection();
                    conn.setAllowUserInteraction(true);
                    return conn.getInputStream();
                } catch (SecurityException ex) {
                    GlobalOptions.err.println("Warning: SecurityException" + " while accessing " + bases[i] + filename);
                    ex.printStackTrace(GlobalOptions.err);
                } catch (FileNotFoundException ex) {
                }
                continue;
            }
            if (dirs[i] == null) continue;
            if (zips[i] != null) {
                String fullname = zipDirs[i] != null ? zipDirs[i] + filename : filename;
                ZipEntry ze = zips[i].getEntry(fullname);
                if (ze != null) return zips[i].getInputStream(ze);
            } else {
                try {
                    File f = new File(dirs[i], localFileName);
                    if (f.exists()) return new FileInputStream(f);
                } catch (SecurityException ex) {
                    GlobalOptions.err.println("Warning: SecurityException" + " while accessing " + dirs[i] + localFileName);
                }
            }
        }
        throw new FileNotFoundException(filename);
    }
