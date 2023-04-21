    public FloppyHelperResult openImageAndGetFiles(File imageFile) {
        if (!toolInstalledProperly()) {
            return this.returnWithError(process_error.toString());
        }
        ProcessRunner cmd = new ProcessRunner();
        cmd.setStartingDir(TEMP_FOLDER);
        cmd.setCommand(uLinkDriveLetter());
        cmd.run();
        process_error.append(cmd.getProcessErrorAsString());
        process_output.append(cmd.getProcessOutputAsString());
        String driveLetter = getDriveLetter();
        if (driveLetter == null) {
            process_error.append("Could not assign drive letter to virtual floppy drive, all letters are in use!");
            return this.returnWithError(process_error.toString());
        }
        cmd.setCommand(linkDriveLetter(driveLetter));
        cmd.run();
        process_error.append(cmd.getProcessErrorAsString());
        process_output.append(cmd.getProcessOutputAsString());
        cmd.setCommand(openImage(imageFile));
        cmd.run();
        process_error.append(cmd.getProcessErrorAsString());
        process_output.append(cmd.getProcessOutputAsString());
        File floppy = new File(driveLetter);
        log.info("Floppy Path name: " + floppy.getAbsolutePath());
        File[] filesOnFloppy = floppy.listFiles();
        for (File file : filesOnFloppy) {
            File dest = new File(EXTRACTED_FILES_DIR, file.getName());
            log.info("Copy file: " + file.getAbsolutePath() + " to : " + dest.getAbsolutePath());
            FileUtils.copyFileTo(file, dest);
        }
        ZipResult zip = ZipUtils.createZipAndCheck(EXTRACTED_FILES_DIR, TEMP_FOLDER, FileUtils.randomizeFileName("extracedFiles.zip"), false);
        cmd.setCommand(closeImage());
        cmd.run();
        process_error.append(cmd.getProcessErrorAsString());
        process_output.append(cmd.getProcessOutputAsString());
        cmd.setCommand(uLinkDriveLetter());
        cmd.run();
        process_error.append(cmd.getProcessErrorAsString());
        process_output.append(cmd.getProcessOutputAsString());
        return this.returnWithSuccess(process_output.toString(), null, zip);
    }
