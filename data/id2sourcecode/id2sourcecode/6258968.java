            public void widgetSelected(final SelectionEvent e) {
                if (isExecuting.get()) {
                    sLogger.logAppend(" (can't stop now)");
                    return;
                }
                String calculateFileOrDirPath = trim(calculateBrowseStyledText.getText());
                String string = trim(calculateStringStyledText.getText());
                boolean calcAString = isNotEmpty(string);
                if (isEmpty(calculateFileOrDirPath) && isEmpty(string)) {
                    sLogger.logWarn("You did not choose a file or enter a string");
                    return;
                }
                final File calculateFileOrDirFile = new File(calculateFileOrDirPath);
                if (isNotEmpty(calculateFileOrDirPath) && !calculateFileOrDirFile.exists()) {
                    sLogger.logWarn("The file you chose does not exist");
                    return;
                }
                SystemOutLogger.debug("Checksum Type:" + getChecksumCalculator());
                caculateResultStyledText.setText("");
                CalculateAChecksum worker = null;
                if (calcAString) {
                    worker = new CalculateAChecksum(apmd5, getChecksumCalculator(), string);
                } else {
                    File saveFile = null;
                    if (createFileButton.getSelection()) {
                        FileDialog saveDialog = new FileDialog(shell, SWT.SAVE);
                        String savePath = saveDialog.open();
                        if (isEmpty(savePath)) {
                            sLogger.logWarn("You did not choose a file to output the checksum(s)");
                            return;
                        }
                        saveFile = new File(savePath);
                        if (saveFile.exists()) {
                            MessageBox mb = new MessageBox(shell, SWT.YES | SWT.NO);
                            mb.setText("File Already Exists");
                            mb.setMessage("File " + saveFile.getName() + " already exists\n Overwrite?");
                            if (mb.open() == SWT.NO) {
                                sLogger.logWarn("Chose not to overwrite the file. Did not create checksum(s)");
                                return;
                            }
                        }
                    }
                    worker = new CalculateAChecksum(apmd5, getChecksumCalculator(), calculateFileOrDirFile, saveFile, recurseDirectoriesButton.getSelection());
                }
                isExecuting.set(true);
                cancelButton.setVisible(true);
                threadPool.submit(progressBarWorker);
                threadPool.submit(worker);
            }
