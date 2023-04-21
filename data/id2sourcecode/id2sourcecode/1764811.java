    public MigrateResult migrate(DigitalObject digitalObject, URI inputFormat, URI outputFormat, List<Parameter> parameters) {
        FloppyHelperResult fat_imgen_result = null;
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
            fat_imgen_result = fat_imgen.openImageAndGetFiles(inputFile);
            if (fat_imgen_result.resultIsZip) {
                zippedResult = fat_imgen_result.getZipResult();
            } else {
                return this.returnWithErrorMessage(fat_imgen_result.getMessage(), null);
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
            fat_imgen_result = fat_imgen.createImageAndInjectFiles(extractedFiles);
            if (!fat_imgen_result.resultIsZip) {
                imageFile = fat_imgen_result.getResultFile();
            } else {
                return this.returnWithErrorMessage(fat_imgen_result.getError(), null);
            }
        } else {
            List<File> tmpList = new ArrayList<File>();
            tmpList.add(inputFile);
            fat_imgen_result = fat_imgen.createImageAndInjectFiles(tmpList);
            imageFile = fat_imgen_result.getResultFile();
            if (imageFile == null) {
                return this.returnWithErrorMessage(PROCESS_ERROR, null);
            }
        }
        DigitalObject resultDigObj = new DigitalObject.Builder(Content.byReference(imageFile)).format(outputFormat).title(imageFile.getName()).build();
        ServiceReport report = new ServiceReport(Type.INFO, Status.SUCCESS, PROCESS_OUT);
        log.info("Created Service report...");
        return new MigrateResult(resultDigObj, report);
    }
