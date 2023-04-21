                        public void run() {
                            int i = 0;
                            long size = 0;
                            File newFile;
                            List<File> filesErr = new ArrayList<File>();
                            dialog.printlnAction(I18N.translate("Copy files"));
                            for (; i < importFiles.length; i++) {
                                if (stop) {
                                    break;
                                }
                                if (importFiles[i].isDirectory()) {
                                    continue;
                                }
                                dialog.println(I18N.translate("Copying") + " " + importFiles[i]);
                                newFile = new File(dir, importFiles[i].getName());
                                if (!newFile.exists()) {
                                    try {
                                        cz.hdf.util.Utils.copyFile(importFiles[i], newFile);
                                        filesListModel.addElement(newFile);
                                    } catch (IOException e) {
                                        dialog.println(I18N.translate("Can not copy file '{0}'.", importFiles[i].getName()));
                                        filesErr.add(importFiles[i]);
                                    }
                                } else {
                                    dialog.printlnError(I18N.translate("File '{0}' already exist. Skipping them.", newFile.getName()));
                                }
                                size += importFiles[i].length();
                                dialog.setPercent(new Long((size * 100) / totalSize).intValue());
                            }
                            cdWindow.cdTempRoastPanel.refreshDirectory();
                            window.changer.topFrame.setCursor(Cursor.getDefaultCursor());
                            dialog.close();
                            if (!filesErr.isEmpty()) {
                                String filesStr = "";
                                for (File fileErr : filesErr) {
                                    filesStr += fileErr.getName() + " ";
                                }
                                new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can not copy files '{0}'.", filesStr.trim()));
                            }
                        }
