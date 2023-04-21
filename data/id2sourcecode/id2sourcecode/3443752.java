    private void _ok() {
        _saveSettings();
        File jarOut = _jarFileSelector.getFileFromField();
        if (jarOut == null) {
            JOptionPane.showMessageDialog(JarOptionsDialog.this, "You must specify an output file", "Error: No File Specified", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (jarOut.exists()) {
            if (JOptionPane.showConfirmDialog(JarOptionsDialog.this, "Are you sure you want to overwrite the file '" + jarOut.getPath() + "'?", "Overwrite file?", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                return;
            }
        }
        setEnabled(false);
        _processingDialog = new ProcessingDialog(this, "Creating Jar File", "Processing, please wait.");
        _processingDialog.setVisible(true);
        SwingWorker worker = new SwingWorker() {

            boolean _success = false;

            HashSet<String> _exceptions = new HashSet<String>();

            private boolean jarAll(File dir, JarBuilder jarFile, final File outputFile) throws IOException {
                LOG.log("jarOthers(" + dir + " , " + jarFile + ")");
                java.io.FileFilter allFilter = new java.io.FileFilter() {

                    public boolean accept(File f) {
                        return !outputFile.equals(f);
                    }
                };
                File[] files = dir.listFiles(allFilter);
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        try {
                            if (files[i].isDirectory()) {
                                LOG.log("jarFile.addDirectoryRecursive(" + files[i] + ")");
                                jarFile.addDirectoryRecursive(files[i], files[i].getName(), allFilter);
                            } else {
                                LOG.log("jarFile.addFile(" + files[i] + ")");
                                jarFile.addFile(files[i], "", files[i].getName());
                            }
                        } catch (IOException ioe) {
                            _exceptions.add(ioe.getMessage());
                        }
                    }
                }
                return true;
            }

            private boolean jarBuildDirectory(File dir, JarBuilder jarFile) throws IOException {
                LOG.log("jarBuildDirectory(" + dir + " , " + jarFile + ")");
                java.io.FileFilter classFilter = new java.io.FileFilter() {

                    public boolean accept(File f) {
                        return f.isDirectory() || f.getName().endsWith(".class");
                    }
                };
                File[] files = dir.listFiles(classFilter);
                LOG.log("\tfiles = " + files);
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        LOG.log("\t\tfiles[" + i + "] = " + files[i]);
                        if (files[i] == null || !files[i].exists()) continue;
                        try {
                            if (files[i].isDirectory()) {
                                LOG.log("jarFile.addDirectoryRecursive(" + files[i] + ")");
                                jarFile.addDirectoryRecursive(files[i], files[i].getName(), classFilter);
                            } else {
                                LOG.log("jarFile.addFile(" + files[i] + ")");
                                jarFile.addFile(files[i], "", files[i].getName());
                            }
                        } catch (IOException ioe) {
                            _exceptions.add(ioe.getMessage());
                        }
                    }
                }
                return true;
            }

            private boolean jarSources(GlobalModel model, JarBuilder jar) {
                List<OpenDefinitionsDocument> srcs = model.getProjectDocuments();
                Iterator<OpenDefinitionsDocument> iter = srcs.iterator();
                while (iter.hasNext()) {
                    OpenDefinitionsDocument doc = iter.next();
                    if (doc.inProject() && !doc.isAuxiliaryFile()) {
                        try {
                            jar.addFile(doc.getFile(), packageNameToPath(doc.getPackageName()), doc.getFileName());
                        } catch (IOException ioe) {
                            _exceptions.add(ioe.getMessage());
                        }
                    }
                }
                return true;
            }

            private String packageNameToPath(String packageName) {
                return packageName.replaceAll("\\.", System.getProperty("file.separator").replaceAll("\\\\", "\\\\\\\\"));
            }

            public Object construct() {
                try {
                    File jarOut = _jarFileSelector.getFileFromField();
                    if (!jarOut.exists()) jarOut.createNewFile();
                    if ((_jarClasses.isSelected() && _jarSources.isSelected()) || _jarAll.isSelected()) {
                        LOG.log("(_jarClasses.isSelected() && _jarSources.isSelected()) || _jarAll.isSelected()");
                        JarBuilder mainJar = null;
                        if (_makeExecutable.isSelected() || _customManifest.isSelected()) {
                            ManifestWriter mw = new ManifestWriter();
                            if (_makeExecutable.isSelected()) mw.setMainClass(_mainClassField.getText()); else mw.setManifestContents(_customManifestText);
                            mainJar = new JarBuilder(jarOut, mw.getManifest());
                        } else {
                            mainJar = new JarBuilder(jarOut);
                        }
                        File binRoot = _model.getBuildDirectory();
                        if (binRoot == null || binRoot == FileOps.NULL_FILE || binRoot.toString().trim().length() == 0) binRoot = _model.getProjectRoot();
                        if (!_jarAll.isSelected()) jarBuildDirectory(binRoot, mainJar);
                        String prefix = _model.getBuildDirectory().getName();
                        if (prefix.length() < 3) prefix = "drjava_tempSourceJar";
                        File sourceJarFile = File.createTempFile(prefix, ".jar");
                        if (!_jarAll.isSelected()) {
                            JarBuilder sourceJar = new JarBuilder(sourceJarFile);
                            jarSources(_model, sourceJar);
                            sourceJar.close();
                            mainJar.addFile(sourceJarFile, "", "source.jar");
                        }
                        if (_jarAll.isSelected()) {
                            LOG.log("jarAll");
                            LOG.log("binRoot=" + binRoot);
                            LOG.log("root=" + _model.getProjectRoot());
                            LOG.log("FileOps.isAncestorOf(_model.getProjectRoot(),binRoot)=" + FileOps.isAncestorOf(_model.getProjectRoot(), binRoot));
                            LOG.log("mainJar=" + mainJar);
                            LOG.log("jarOut=" + jarOut);
                            jarAll(_model.getProjectRoot(), mainJar, jarOut);
                            if (!_model.getProjectRoot().equals(binRoot)) LOG.log("jarBuildDirectory");
                            jarBuildDirectory(binRoot, mainJar);
                        }
                        mainJar.close();
                        sourceJarFile.delete();
                    } else if (_jarClasses.isSelected()) {
                        JarBuilder jb;
                        if (_makeExecutable.isSelected() || _customManifest.isSelected()) {
                            ManifestWriter mw = new ManifestWriter();
                            if (_makeExecutable.isSelected()) mw.setMainClass(_mainClassField.getText()); else mw.setManifestContents(_customManifestText);
                            Manifest m = mw.getManifest();
                            if (m != null) jb = new JarBuilder(jarOut, m); else throw new IOException("Manifest is malformed");
                        } else {
                            jb = new JarBuilder(jarOut);
                        }
                        File binRoot = _model.getBuildDirectory();
                        if (binRoot == null || binRoot == FileOps.NULL_FILE || binRoot.toString().trim().length() == 0) binRoot = _model.getProjectRoot();
                        jarBuildDirectory(binRoot, jb);
                        jb.close();
                    } else {
                        JarBuilder jb = new JarBuilder(jarOut);
                        jarSources(_model, jb);
                        jb.close();
                    }
                    _success = true;
                } catch (Exception e) {
                    LOG.log("construct: " + e, e.getStackTrace());
                }
                return null;
            }

            public void finished() {
                _processingDialog.setVisible(false);
                _processingDialog.dispose();
                JarOptionsDialog.this.setEnabled(true);
                if (_success) {
                    if (_exceptions.size() > 0) {
                        ScrollableListDialog<String> dialog = new ScrollableListDialog.Builder<String>().setOwner(JarOptionsDialog.this).setTitle("Problems Creating Jar").setText("There were problems creating this jar file, but DrJava was probably able to recover.").setItems(new ArrayList<String>(_exceptions)).setMessageType(JOptionPane.ERROR_MESSAGE).build();
                        Utilities.setPopupLoc(dialog, JarOptionsDialog.this);
                        dialog.showDialog();
                    }
                    if ((_jarAll.isSelected() || _jarClasses.isSelected()) && _makeExecutable.isSelected()) {
                        Object[] options = { "OK", "Run" };
                        int res = JOptionPane.showOptionDialog(JarOptionsDialog.this, "Jar file successfully written to '" + _jarFileSelector.getFileFromField().getName() + "'", "Jar Creation Successful", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                        JarOptionsDialog.this.setVisible(false);
                        if (1 == res) {
                            SwingWorker jarRunner = new SwingWorker() {

                                public Object construct() {
                                    try {
                                        File cp = _jarFileSelector.getFileFromField();
                                        File wd = cp.getParentFile();
                                        Process p = JVMBuilder.DEFAULT.classPath(cp).directory(wd).start(_mainClassField.getText());
                                        ConcurrentUtil.copyProcessErr(p, System.err);
                                        ConcurrentUtil.copyProcessOut(p, System.out);
                                        p.waitFor();
                                        JOptionPane.showMessageDialog(JarOptionsDialog.this, "Execution of jar file terminated (exit value = " + p.exitValue() + ")", "Execution terminated.", JOptionPane.INFORMATION_MESSAGE);
                                    } catch (Exception e) {
                                        JOptionPane.showMessageDialog(JarOptionsDialog.this, "An error occured while running the jar file: \n" + e, "Error", JOptionPane.ERROR_MESSAGE);
                                    } finally {
                                        JarOptionsDialog.this.setVisible(false);
                                    }
                                    return null;
                                }
                            };
                            jarRunner.start();
                        }
                    } else {
                        JOptionPane.showMessageDialog(JarOptionsDialog.this, "Jar file successfully written to '" + _jarFileSelector.getFileFromField().getName() + "'", "Jar Creation Successful", JOptionPane.INFORMATION_MESSAGE);
                        JarOptionsDialog.this.setVisible(false);
                    }
                } else {
                    ManifestWriter mw = new ManifestWriter();
                    if (_makeExecutable.isSelected()) mw.setMainClass(_mainClassField.getText()); else mw.setManifestContents(_customManifestText);
                    Manifest m = mw.getManifest();
                    if (m != null) {
                        if (_exceptions.size() > 0) {
                            ScrollableListDialog<String> dialog = new ScrollableListDialog.Builder<String>().setOwner(JarOptionsDialog.this).setTitle("Error Creating Jar").setText("<html>An error occured while creating the jar file. This could be because the file<br>" + "that you are writing to or the file you are reading from could not be opened.</html>").setItems(new ArrayList<String>(_exceptions)).setMessageType(JOptionPane.ERROR_MESSAGE).build();
                            Utilities.setPopupLoc(dialog, JarOptionsDialog.this);
                            dialog.showDialog();
                        } else {
                            JOptionPane.showMessageDialog(JarOptionsDialog.this, "An error occured while creating the jar file. This could be because the file that you " + "are writing to or the file you are reading from could not be opened.", "Error Creating Jar", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        if (_exceptions.size() > 0) {
                            ScrollableListDialog<String> dialog = new ScrollableListDialog.Builder<String>().setOwner(JarOptionsDialog.this).setTitle("Error Creating Jar").setText("The supplied manifest does not conform to the 1.0 Manifest format specification").setItems(new ArrayList<String>(_exceptions)).setMessageType(JOptionPane.ERROR_MESSAGE).build();
                            Utilities.setPopupLoc(dialog, JarOptionsDialog.this);
                            dialog.showDialog();
                        } else {
                            JOptionPane.showMessageDialog(JarOptionsDialog.this, "The supplied manifest does not conform to the 1.0 Manifest format specification.", "Error Creating Jar", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    JarOptionsDialog.this.setVisible(false);
                }
                _model.refreshActiveDocument();
            }
        };
        worker.start();
    }
