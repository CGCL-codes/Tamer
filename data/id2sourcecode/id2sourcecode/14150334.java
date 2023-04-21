    public MigrateResult migrate(DigitalObject digitalObject, URI inputFormat, URI outputFormat, List<Parameter> parameters) {
        String inFormat = formatReg.getFirstExtension(inputFormat).toUpperCase();
        DigitalObjectContent content = digitalObject.getContent();
        Checksum checksum = content.getChecksum();
        String fileName = digitalObject.getTitle();
        if (fileName == null) {
            inputExt = formatReg.getFirstExtension(inputFormat);
            fileName = FileUtils.randomizeFileName(DEFAULT_INPUT_NAME + "." + inputExt);
        } else {
            fileName = FileUtils.randomizeFileName(fileName);
        }
        File inputFile = FileUtils.writeInputStreamToFile(digitalObject.getContent().read(), TEMP_FOLDER, fileName);
        if ((inFormat.endsWith("IMA")) || inFormat.endsWith("IMG")) {
            ZipResult zippedResult = FloppyImageHelperUnix.extractFilesFromFloppyImage(inputFile);
            DigitalObject resultDigObj = DigitalObjectUtils.createZipTypeDigitalObject(zippedResult.getZipFile(), zippedResult.getZipFile().getName(), true, true, false);
            ServiceReport report = new ServiceReport(Type.INFO, Status.SUCCESS, PROCESS_OUT);
            return new MigrateResult(resultDigObj, report);
        }
        List<File> files = null;
        if (inFormat.endsWith("ZIP")) {
            if (checksum != null) {
                files = ZipUtils.checkAndUnzipTo(inputFile, TEMP_FOLDER, checksum);
            } else files = ZipUtils.unzipTo(inputFile, TEMP_FOLDER);
        } else {
            files = new ArrayList<File>();
            files.add(inputFile);
        }
        File floppy = FloppyImageHelperUnix.createFloppyImageWithFiles(files);
        if (floppy == null) return this.returnWithErrorMessage(PROCESS_ERROR, null);
        DigitalObject resultDigObj = new DigitalObject.Builder(Content.byReference(floppy)).format(outputFormat).title(floppy.getName()).build();
        ServiceReport report = new ServiceReport(Type.INFO, Status.SUCCESS, PROCESS_OUT);
        return new MigrateResult(resultDigObj, report);
    }
