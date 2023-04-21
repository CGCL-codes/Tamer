    public void write(OutputStream out) throws IOException, MetsException, UnsupportedEncodingException {
        ZipOutputStream zip = new ZipOutputStream(out);
        zip.setComment("METS archive created by DSpaceSIP");
        zip.setLevel(compression);
        zip.setMethod(ZipOutputStream.DEFLATED);
        ZipEntry me = new ZipEntry(MANIFEST_FILE);
        zip.putNextEntry(me);
        finishManifest(zip);
        zip.closeEntry();
        for (Map.Entry<String, PackageFile> e : zipFiles.entrySet()) {
            PackageFile pf = e.getValue();
            ZipEntry ze = new ZipEntry(pf.zipPath);
            ze.setTime(pf.absPath.lastModified());
            zip.putNextEntry(ze);
            copyStream(new FileInputStream(pf.absPath), zip);
            zip.closeEntry();
        }
        zip.close();
        zipFiles = null;
    }
