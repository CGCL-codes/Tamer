    public void dumpPackage(String device, GEnterprisePackage pkg) throws IOException, ConfigurationException {
        String ddir = this.dumpDirectory + File.separator + device + File.separator;
        File dumpdir = new File(ddir);
        if (!dumpdir.exists()) dumpdir.mkdirs();
        File dfile = File.createTempFile("pkg-", ".zip", dumpdir);
        FileOutputStream out = new FileOutputStream(dfile);
        ZipOutputStream zipout = new ZipOutputStream(out);
        ZipEntry ze = new ZipEntry("pkg-" + (int) System.currentTimeMillis() + ".xml");
        zipout.putNextEntry(ze);
        zipout.setLevel(7);
        pkg.getPackageFactory().writePackage(pkg, zipout);
        zipout.closeEntry();
        zipout.close();
        out.close();
    }
