    @Test
    public void testChangeStudyFields() throws Exception {
        final String UNZIP_FOLDER = "src/test/resources/outputfiles/changestudyfields/";
        final String ZIP_FILE = FOLDER_TEST_COPYTO_PRIVATE + "STUDY1.zip";
        FileUtils.copyFile(new File("src/test/resources/inputfiles/isatab1.zip"), new File(ZIP_FILE));
        IsaTabUploader itu = new IsaTabUploader();
        itu.setCopyToPrivateFolder(FOLDER_TEST_COPYTO_PRIVATE);
        itu.setCopyToPublicFolder(FOLDER_TEST_COPYTO_PUBLIC);
        itu.setUnzipFolder(UNZIP_FOLDER);
        HashMap<String, String> replacement = new HashMap<String, String>();
        replacement.put("Study Public Release Date", "09/09/2010");
        itu.changeStudyFields("STUDY1", replacement);
        FileUtils.deleteDirectory(new File(UNZIP_FOLDER));
        Zipper.unzip2(ZIP_FILE, UNZIP_FOLDER);
        String investigationfile = FileUtil.file2String(UNZIP_FOLDER + "i_Investigation.txt");
        assertTrue("ChangeStudyFields test. Investigation file under " + UNZIP_FOLDER + " should have a Study Public Release Date of 09/09/2010", investigationfile.indexOf("Study Public Release Date\t\"09/09/2010\"") != -1);
    }
