    public ModifyResult modify(DigitalObject digitalObject, URI inputFormat, List<Parameter> parameters) {
        String inFormat = formatRegistry.getFirstExtension(inputFormat).toUpperCase();
        String fileName = digitalObject.getTitle();
        if (fileName == null) {
            INPUT_EXT = formatRegistry.getExtensions(inputFormat).iterator().next();
            fileName = DEFAULT_INPUT_NAME + "." + INPUT_EXT;
        }
        if (!inFormat.equalsIgnoreCase("IMA") && !inFormat.equalsIgnoreCase("IMG")) {
            log.error("ERROR: Input file ' " + fileName + "' is NOT an '.ima' or '.img' file or is no floppy image at all." + "\nThis service is able to deal with ima/img files only!!!" + "\nSorry, returning with error!");
            return this.returnWithErrorMessage("ERROR: Input file ' " + fileName + "' is NOT an '.ima' or '.img' file or is no floppy image at all." + "\nThis service is able to deal with ima/img files only!!!" + "\nSorry, returning with error!", null);
        }
        File originalImageFile = FileUtils.writeInputStreamToFile(digitalObject.getContent().read(), TEMP_FOLDER, fileName);
        FloppyHelperResult vfdResult = vfd.addFilesToFloppyImage(originalImageFile);
        File modifiedImage = vfdResult.getResultFile();
        DigitalObject result = null;
        if (modifiedImage != null) {
            result = new DigitalObject.Builder(Content.byReference(modifiedImage)).title(modifiedImage.getName()).format(formatRegistry.createExtensionUri(FileUtils.getExtensionFromFile(modifiedImage))).build();
        } else {
            return this.returnWithErrorMessage("Received NO result file from service. Something went terribly wrong somewhere!", null);
        }
        if (vfdResult.getState() == VirtualFloppyDriveResult.SUCCESS) {
            ServiceReport report = new ServiceReport(Type.INFO, Status.SUCCESS, vfdResult.getMessage());
            log.info("Created Service report...");
            return new ModifyResult(result, report);
        } else {
            return this.returnWithErrorMessage(vfdResult.getMessage(), null);
        }
    }
