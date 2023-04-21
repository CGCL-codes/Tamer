    private void testMigrate(URI inputFormat, URI outputFormat, List<Parameter> parameters) {
        String extension = format.getFirstExtension(inputFormat);
        DigitalObject digObj = createDigitalObject(extension);
        MigrateResult mr = extractor.migrate(digObj, inputFormat, outputFormat, parameters);
        ServiceReport sr = mr.getReport();
        if (sr.getType() == Type.ERROR) {
            System.err.println("FAILED: " + sr);
        } else {
            System.out.println("Got Report: " + sr);
            DigitalObject doOut = mr.getDigitalObject();
            assertTrue("Resulting digital object is null.", doOut != null);
            File formatFolder = FileUtils.createFolderInWorkFolder(testOutFolder, extension);
            File result = FileUtils.writeInputStreamToFile(doOut.getContent().read(), formatFolder, "xcdlMigrateTest_" + extension + ".xcdl");
            System.out.println("Resulting file size: " + result.length() + " KB.");
            System.out.println("Resulting file path: " + result.getAbsolutePath());
            System.out.println("Result: " + doOut);
            System.out.println("Result.content: " + doOut.getContent());
        }
    }
