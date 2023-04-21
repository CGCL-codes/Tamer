                        public void actionPerformed(final ActionEvent e) {
                            JFileChooser fileChooser = new JFileChooser(CommonUtils.getCurrentSaveDirectory());
                            fileChooser.setFileFilter(new FileFilter() {

                                @Override
                                public boolean accept(File f) {
                                    if (f.isDirectory()) {
                                        return true;
                                    }
                                    if (f.canRead() && f.isFile() && f.getName().toLowerCase().endsWith(".zip")) {
                                        return true;
                                    }
                                    return false;
                                }

                                @Override
                                public String getDescription() {
                                    return UIHelper.getText("editres.packagedescription");
                                }
                            });
                            int returnVal = fileChooser.showOpenDialog(thisDialog);
                            if (returnVal == JFileChooser.APPROVE_OPTION) {
                                CommonUtils.setCurrentSaveDirectory(fileChooser.getSelectedFile());
                                ArrayList<ResourceDataVO> resources = new ArrayList<ResourceDataVO>();
                                File zipFile = fileChooser.getSelectedFile();
                                Properties props = new Properties();
                                ZipInputStream packageStream = null;
                                try {
                                    packageStream = new ZipInputStream(new FileInputStream(zipFile));
                                    ZipEntry entry = null;
                                    while ((entry = packageStream.getNextEntry()) != null) {
                                        if (!entry.isDirectory()) {
                                            if (entry.getName().equals(Constants.CUSTOMRESOURCE_PACKAGEINFOFILENAME)) {
                                                props.load(packageStream);
                                            } else {
                                                ResourceDataVO resourceData = new ResourceDataVO();
                                                resourceData.setName(entry.getName());
                                                byte[] buffer = new byte[8192];
                                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                int read = -1;
                                                while ((read = packageStream.read(buffer)) != -1) {
                                                    baos.write(buffer, 0, read);
                                                }
                                                resourceData.setData(baos.toByteArray());
                                                resources.add(resourceData);
                                            }
                                        }
                                    }
                                    fileErrorLabel.setText("");
                                    ShowPackageHeaderDialog hDialog = new ShowPackageHeaderDialog(props, thisDialog);
                                    hDialog.setLocationRelativeTo(SwingUtilities.getRootPane(thisDialog));
                                    hDialog.setModal(true);
                                    hDialog.setVisible(true);
                                    if (hDialog.isConfirmed()) {
                                        String version = hDialog.getVersion();
                                        String defaultComment = hDialog.getDefaultComment();
                                        String application = hDialog.getApplication();
                                        for (ResourceDataVO next : resources) {
                                            next.setApplication(application);
                                            next.setComment(defaultComment);
                                            next.setVersion(version);
                                            next.setOrgId(orgId);
                                            long currentDate = System.currentTimeMillis();
                                            next.setCreated(currentDate);
                                            next.setModified(currentDate);
                                            next.setType(AdminUIUtils.autoSenseCustomFileType(next.getName()));
                                        }
                                        tablePanel.setData(resources);
                                        for (int i = 0; i < applicationComboBox.getItemCount(); i++) {
                                            if (((ComboBoxItem) applicationComboBox.getItemAt(i)).getValue().equals(application)) {
                                                applicationComboBox.setSelectedIndex(i);
                                                break;
                                            }
                                        }
                                        versionValueLabel.setText(version);
                                    }
                                } catch (FileNotFoundException e1) {
                                    LocalLog.getLogger().log(Level.SEVERE, "Error reading resource package file : " + zipFile.getAbsoluteFile(), e);
                                    fileErrorLabel.setText(UIHelper.getText("fileerr.notexist"));
                                } catch (IOException e1) {
                                    LocalLog.getLogger().log(Level.SEVERE, "Error reading resource package file : " + zipFile.getAbsoluteFile(), e);
                                    fileErrorLabel.setText(UIHelper.getText("fileerr.errorreading"));
                                } finally {
                                    if (packageStream != null) {
                                        try {
                                            packageStream.close();
                                        } catch (IOException e1) {
                                            LocalLog.getLogger().log(Level.SEVERE, "Error closing resource package file : " + zipFile.getAbsoluteFile(), e);
                                        }
                                    }
                                }
                            }
                        }
