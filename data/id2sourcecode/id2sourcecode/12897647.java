    private FileCreationResult createOutputFiles(String outputDir, String parseResult) throws InterruptedException {
        int newFileCounter = 0;
        int skippedFilesCounter = 0;
        char newline = parseResult.indexOf('\r') != -1 ? '\r' : '\n';
        int pos = parseResult.indexOf(FILE_HEADER);
        while (pos != -1) {
            boolean skipFile = false;
            int nextPos = parseResult.indexOf(FILE_HEADER, pos + 1);
            nextPos = nextPos == -1 ? parseResult.length() : nextPos;
            String fileContent = parseResult.substring(pos, nextPos);
            int endHeaderPos = fileContent.indexOf(newline);
            if (endHeaderPos == -1) {
                JApplicationGen.log("Bad template: expected newline after header.");
                return new FileCreationResult(0, 0);
            }
            String header = fileContent.substring(0, endHeaderPos).trim();
            fileContent = fileContent.substring(endHeaderPos).trim();
            if ("".equals(fileContent)) {
                JApplicationGen.log("Warning! - Empty file - probably a bad template.");
            }
            int fileNamePos = header.indexOf(':');
            if (fileNamePos == -1 || fileNamePos == (header.length() - 1)) {
                JApplicationGen.log("Bad template: header doesn't specify an output file.");
                return new FileCreationResult(0, 0);
            }
            String fileName = header.substring(header.indexOf(':') + 1).trim();
            File origFile = new File(outputDir + '/' + fileName);
            File tempFile = new File(outputDir + '/' + fileName + "_temp");
            try {
                if (newline == '\r') {
                    fileContent += "\r\n";
                } else {
                    fileContent += "\r";
                }
                FileUtils.createFile(tempFile, fileContent);
                if (origFile.exists()) {
                    if (overwrite != Boolean.FALSE) {
                        String diff = null;
                        try {
                            diff = new Diff(origFile, tempFile).performDiff();
                            int choice = 999;
                            if (diff != null) {
                                if (overwrite == null) {
                                    while (choice == 999 || JApplicationGen.DIALOGUE_OPTIONS[choice] == JApplicationGen.OPTION_VIEW_DIFF) {
                                        choice = JOptionPane.showOptionDialog(null, "The file " + origFile + " differs from the existing copy.\n" + "Do you want to overwrite this file?", "File already exists!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, JApplicationGen.DIALOGUE_OPTIONS, JApplicationGen.OPTION_VIEW_DIFF);
                                        if (JApplicationGen.DIALOGUE_OPTIONS[choice] == JApplicationGen.OPTION_VIEW_DIFF) {
                                            new HtmlContentPopUp(null, "Diff report:", true, diff, false).show();
                                        }
                                    }
                                    if (choice == JOptionPane.CLOSED_OPTION || JApplicationGen.DIALOGUE_OPTIONS[choice] == JApplicationGen.OPTION_NO) {
                                        skipFile = true;
                                    } else if (JApplicationGen.DIALOGUE_OPTIONS[choice] == JApplicationGen.OPTION_NO_ALL) {
                                        overwrite = Boolean.FALSE;
                                        skipFile = true;
                                    } else if (JApplicationGen.DIALOGUE_OPTIONS[choice] == JApplicationGen.OPTION_YES_ALL) {
                                        overwrite = Boolean.TRUE;
                                    }
                                }
                            } else {
                                skipFile = true;
                            }
                        } catch (IOException e) {
                            JApplicationGen.log("Error! Can't perform diff - file '" + origFile.getPath() + "' will not be overwritten: " + e);
                            skipFile = true;
                        }
                    } else {
                        skipFile = true;
                    }
                }
            } catch (IOException e) {
                JApplicationGen.log("Error! Can't create temp file: " + tempFile.getPath() + " - " + e);
                tempFile = null;
            }
            if (skipFile) {
                JApplicationGen.log("Skipping file " + origFile);
                FileUtils.deleteFile(tempFile);
                skippedFilesCounter++;
            } else if (tempFile != null) {
                newFileCounter++;
                if (origFile.exists()) {
                    JApplicationGen.log("Backing up and overwriting " + origFile);
                    File backup = new File(origFile.getPath() + ".backup");
                    FileUtils.deleteFile(backup);
                    origFile.renameTo(backup);
                    FileUtils.deleteFile(origFile);
                } else {
                    JApplicationGen.log("Creating new file " + origFile);
                }
                tempFile.renameTo(origFile);
            }
            parseResult = parseResult.substring(nextPos);
            pos = parseResult.indexOf(FILE_HEADER);
        }
        return new FileCreationResult(newFileCounter, skippedFilesCounter);
    }
