    public FloppyHelperResult createImageAndInjectFiles(List<File> filesToInject) {
        if (!toolInstalledProperly()) {
            return this.returnWithError(process_error.toString());
        }
        if (FileUtils.filesTooLargeForMedium(filesToInject, FLOPPY_SIZE)) {
            process_error.append("Sorry! File compilation too large to be written to a Floppy (1.44 MB).");
            log.error("Sorry! File compilation too large to be written to a Floppy (1.44 MB).");
            return this.returnWithError(process_error.toString());
        }
        ProcessRunner cmd = new ProcessRunner();
        cmd.setStartingDir(TEMP_FOLDER);
        cmd.setCommand(createNewImage());
        cmd.run();
        process_error.append(cmd.getProcessErrorAsString());
        process_output.append(cmd.getProcessOutputAsString());
        cmd.setCommand(formatFloppyImage());
        cmd.run();
        process_error.append(cmd.getProcessErrorAsString());
        process_output.append(cmd.getProcessOutputAsString());
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
        File floppy = new File(driveLetter);
        for (File currentFile : filesToInject) {
            File target = new File(floppy, currentFile.getName());
            FileUtils.copyFileTo(currentFile, target);
        }
        cmd.setCommand(saveImageTo(DEFAULT_FLOPPY_IMAGE_NAME));
        cmd.run();
        process_error.append(cmd.getProcessErrorAsString());
        process_output.append(cmd.getProcessOutputAsString());
        cmd.setCommand(closeImage());
        cmd.run();
        process_error.append(cmd.getProcessErrorAsString());
        process_output.append(cmd.getProcessOutputAsString());
        cmd.setCommand(uLinkDriveLetter());
        cmd.run();
        process_error.append(cmd.getProcessErrorAsString());
        process_output.append(cmd.getProcessOutputAsString());
        File newfloppyImage = new File(TEMP_FOLDER, DEFAULT_FLOPPY_IMAGE_NAME);
        return this.returnWithSuccess(process_output.toString(), newfloppyImage, null);
    }
