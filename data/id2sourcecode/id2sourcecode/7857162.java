    public void copy(File[] files, File destDir, boolean confirm, boolean askBeforeOverwrite) {
        if (files != null && destDir != null) {
            boolean copyIt = true;
            if (confirm) {
                String message = "";
                if (files.length == 1) {
                    File file = files[0];
                    message = messageFormats[4].format(new String[] { file.getName(), getDisplayName(destDir) });
                } else {
                    message = messageFormats[5].format(new String[] { String.valueOf(files.length), getDisplayName(destDir) });
                }
                copyIt = MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), Messages.getString("FileServices.copy_dialog_title"), message);
            }
            if (copyIt) {
                for (File file : files) {
                    try {
                        if (file.isFile()) {
                            boolean reallyCopyIt = true;
                            File destinationFile = new File(destDir, file.getName());
                            if (destinationFile.exists()) {
                                if (askBeforeOverwrite) {
                                    String message = messageFormats[2].format(new String[] { file.getName(), getDisplayName(destDir) });
                                    reallyCopyIt = MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), Messages.getString("FileServices.overwrite_dialog_title"), message);
                                }
                            }
                            if (reallyCopyIt) {
                                log.debug("copy : " + file.getPath() + " -> " + destDir.getPath());
                                FileUtils.copyFileToDirectory(file, destDir);
                            }
                        } else if (file.isDirectory()) {
                            File destinationFile = new File(destDir, file.getName());
                            if (destinationFile.exists()) {
                                boolean reallyCopyIt = true;
                                if (askBeforeOverwrite) {
                                    String message = messageFormats[3].format(new String[] { file.getName(), getDisplayName(destDir) });
                                    reallyCopyIt = MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), Messages.getString("FileServices.overwrite_dialog_title"), message);
                                }
                                if (reallyCopyIt) {
                                    copy(file.listFiles(), destinationFile, false, false);
                                }
                            } else {
                                log.debug("copy : " + file.getPath() + " -> " + destDir.getPath());
                                FileUtils.copyDirectoryToDirectory(file, destDir);
                            }
                        }
                    } catch (IOException e) {
                        log.error("Error moving file", e);
                    }
                }
            }
        }
    }
