    private void initTestExtVolume(PVDatabase db) throws IOException {
        File voldir = File.createTempFile("pv_conversion_extvol", "");
        voldir.delete();
        voldir.mkdir();
        File d1 = new File(voldir, "testdir2");
        d1.mkdirs();
        File f1 = new File("testfiles", "test4.jpg");
        File df1 = new File(voldir, "test4.jpg");
        FileUtils.copyFile(f1, df1);
        File df2 = new File(voldir, "cropped_test4.jpg");
        FileUtils.copyFile(f1, df2);
        PVDatabase.LegacyVolume lvol = new PVDatabase.LegacyExtVolume("extvol_pv_convert_test_volume", voldir.getAbsolutePath(), 4);
        db.addLegacyVolume(lvol);
    }
