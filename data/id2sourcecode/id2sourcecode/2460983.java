    private void initTestVolume(PVDatabase db) throws IOException {
        File voldir = File.createTempFile("pv_conversion_testvol", "");
        voldir.delete();
        voldir.mkdir();
        File d1 = new File(voldir, "2006");
        File d2 = new File(d1, "200605");
        d2.mkdirs();
        File f1 = new File("testfiles", "test1.jpg");
        File df1 = new File(d2, "20060516_00002.jpg");
        FileUtils.copyFile(f1, df1);
        File df2 = new File(d2, "20060516_00003.jpg");
        FileUtils.copyFile(f1, df2);
        PVDatabase.LegacyVolume lvol = new PVDatabase.LegacyVolume("defaultVolume", voldir.getAbsolutePath());
        db.addLegacyVolume(lvol);
    }
