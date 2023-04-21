    public MigrateResult migrate(DigitalObject digitalObject, URI inputFormat, URI outputFormat, List<Parameter> parameters) {
        FloppyHelperResult vfdResult = null;
        String inFormat = format.getFirstExtension(inputFormat).toUpperCase();
        List<File> extractedFiles = null;
        String fileName = digitalObject.getTitle();
        DigitalObjectContent content = digitalObject.getContent();
        Checksum checksum = content.getChecksum();
        if (fileName == null) {
            fileName = DEFAULT_INPUT_NAME + "." + inFormat;
        }
        File inputFile = FileUtils.writeInputStreamToFile(digitalObject.getContent().read(), TEMP_FOLDER, fileName);
        File imageFile = null;
        ZipResult zippedResult = null;
        if ((inFormat.endsWith("IMA")) || inFormat.endsWith("IMG")) {
            vfdResult = vfd.openImageAndGetFiles(inputFile);
            if (vfdResult.resultIsZip) {
                zippedResult = vfdResult.getZipResult();
            } else {
                return this.returnWithErrorMessage(vfdResult.getMessage(), null);
            }
            DigitalObject resultDigObj = DigitalObjectUtils.createZipTypeDigitalObject(zippedResult.getZipFile(), zippedResult.getZipFile().getName(), true, true, false);
            ServiceReport report = new ServiceReport(Type.INFO, Status.SUCCESS, PROCESS_OUT);
            log.info("Created Service report...");
            return new MigrateResult(resultDigObj, report);
        }
        if (inFormat.endsWith("ZIP")) {
            if (checksum != null) {
                extractedFiles = ZipUtils.checkAndUnzipTo(inputFile, EXTRACTED_FILES, checksum);
            } else {
                extractedFiles = ZipUtils.unzipTo(inputFile, EXTRACTED_FILES);
            }
            vfdResult = vfd.createImageAndInjectFiles(extractedFiles);
            if (!vfdResult.resultIsZip) {
                imageFile = vfdResult.getResultFile();
            } else {
                return this.returnWithErrorMessage(vfdResult.getMessage(), null);
            }
        } else {
            List<File> tmpList = new ArrayList<File>();
            tmpList.add(inputFile);
            vfdResult = vfd.createImageAndInjectFiles(tmpList);
            imageFile = vfdResult.getResultFile();
            if (imageFile == null) {
                return this.returnWithErrorMessage(PROCESS_ERROR, null);
            }
        }
        DigitalObject resultDigObj = new DigitalObject.Builder(Content.byReference(imageFile)).format(outputFormat).title(imageFile.getName()).build();
        ServiceReport report = new ServiceReport(Type.INFO, Status.SUCCESS, PROCESS_OUT);
        log.info("Created Service report...");
        return new MigrateResult(resultDigObj, report);
    }
