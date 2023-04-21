    @Test
    public void testGetStudyFields() throws Exception {
        final String UNZIP_FOLDER = "src/test/resources/outputfiles/getstudyfields/";
        final String ZIP_FILE = FOLDER_TEST_COPYTO_PRIVATE + "GETFIELDS.zip";
        FileUtils.copyFile(new File("src/test/resources/inputfiles/isatab1.zip"), new File(ZIP_FILE));
        IsaTabUploader itu = new IsaTabUploader();
        itu.setCopyToPrivateFolder(FOLDER_TEST_COPYTO_PRIVATE);
        itu.setCopyToPublicFolder(FOLDER_TEST_COPYTO_PUBLIC);
        itu.setUnzipFolder(UNZIP_FOLDER);
        Map<String, String> result = itu.getStudyFields("GETFIELDS", new String[] { "Study Identifier", "Study PubMed ID" });
        assertEquals("Number of values found in file.", 2, result.size());
        assertEquals("Study Identifier value test", "BII-S-6", result.get("Study Identifier"));
        assertEquals("Study PubMed ID value test", "17203948", result.get("Study PubMed ID"));
        result = itu.getStudyFields(new File(ZIP_FILE), new String[] { "Study Submission Date", "Study File Name" });
        assertEquals("Number of values found in file 2.", 2, result.size());
        assertEquals("Study Submission Date", "12/10/2004", result.get("Study Submission Date"));
        assertEquals("Study File Name", "s_BII-S-6.txt", result.get("Study File Name"));
    }
