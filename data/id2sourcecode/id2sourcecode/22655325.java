    protected void zipDir(File dir, ZipOutputStream zOut, String vPath) throws IOException {
        if (addedDirs.get(vPath) != null) {
            return;
        }
        addedDirs.put(vPath, vPath);
        ZipEntry ze = new ZipEntry(vPath);
        if (dir != null && dir.exists()) {
            ze.setTime(dir.lastModified());
        } else {
            ze.setTime(System.currentTimeMillis());
        }
        ze.setSize(0);
        ze.setMethod(ZipEntry.STORED);
        ze.setCrc(EMPTY_CRC);
        zOut.putNextEntry(ze);
    }
