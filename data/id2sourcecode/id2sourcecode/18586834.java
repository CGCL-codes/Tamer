    @Test
    public void testMigrateExtractFilesFromFloppy() {
        System.out.println("********************************************");
        System.out.println("* Testing: Extract Files from Floppy Image *");
        System.out.println("********************************************");
        DigitalObjectContent content = Content.byReference(RESULT_FILE);
        DigitalObject input = new DigitalObject.Builder(content).format(format.createExtensionUri("ima")).title(RESULT_FILE.getName()).build();
        MigrateResult migrateResult = FLOPPY_IMAGE_HELPER.migrate(input, format.createExtensionUri("ima"), format.createExtensionUri("zip"), null);
        ServiceReport report = migrateResult.getReport();
        System.out.println(report);
        assertTrue("Resulting DigitalObject should NOT be NULL!!!", migrateResult.getDigitalObject() != null);
        DigitalObject resultDigObj = migrateResult.getDigitalObject();
        File resultFile = new File(OUT_DIR, resultDigObj.getTitle());
        FileUtils.writeInputStreamToFile(resultDigObj.getContent().read(), resultFile);
        DigitalObjectContent resultContent = resultDigObj.getContent();
        ZipUtils.checkAndUnzipTo(resultFile, OUT_DIR, resultContent.getChecksum());
    }
