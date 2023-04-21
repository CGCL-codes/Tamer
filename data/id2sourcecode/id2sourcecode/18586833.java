    @Test
    public void testMigrateAndCreateImage() {
        System.out.println("****************************************************");
        System.out.println("* Testing: Create Floppy Image and Inject Files... *");
        System.out.println("****************************************************");
        FileUtils.deleteAllFilesInFolder(OUT_DIR);
        ZipResult zipResult = ZipUtils.createZipAndCheck(FILES_TO_INJECT, OUT_DIR, "test.zip", false);
        File zipFile = zipResult.getZipFile();
        DigitalObject input = DigitalObjectUtils.createZipTypeDigitalObject(zipFile, zipFile.getName(), true, true, true);
        MigrateResult migrateResult = FLOPPY_IMAGE_HELPER.migrate(input, format.createExtensionUri("zip"), format.createExtensionUri("ima"), null);
        ServiceReport report = migrateResult.getReport();
        System.out.println(report);
        assertTrue("Resulting DigitalObject should NOT be NULL!!!", migrateResult.getDigitalObject() != null);
        DigitalObject resultDigObj = migrateResult.getDigitalObject();
        RESULT_FILE = new File(OUT_DIR, resultDigObj.getTitle());
        FileUtils.writeInputStreamToFile(resultDigObj.getContent().read(), RESULT_FILE);
    }
