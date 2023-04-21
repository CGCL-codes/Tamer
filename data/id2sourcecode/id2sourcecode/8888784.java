    public void actionPerformed(AnActionEvent e) {
        try {
            JFileChooser jfc = new JFileChooser();
            jfc.setAcceptAllFileFilterUsed(false);
            jfc.addChoosableFileFilter(new FileTypeFilter(StdFileTypes.XML, "comparator.fileChooser.snapshot.filter.xml.description"));
            jfc.addChoosableFileFilter(new FileTypeFilter(StdFileTypes.ARCHIVE, "comparator.fileChooser.snapshot.filter.zip.description"));
            jfc.setDialogTitle(APIComparatorBundle.message("comparator.fileChooser.snapshot.save.title"));
            jfc.setDialogType(JFileChooser.SAVE_DIALOG);
            if (jfc.showSaveDialog(tree) == JFileChooser.APPROVE_OPTION) {
                File file = jfc.getSelectedFile();
                final FileType fileType = ((FileTypeFilter) jfc.getFileFilter()).getFileType();
                if (file != null) {
                    if (!file.exists()) {
                        String fileName = file.getName();
                        int dotIndex = fileName.lastIndexOf('.');
                        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
                            String extension = fileName.substring(0, dotIndex + 1);
                            if (extension.toLowerCase().equals(fileType.getDefaultExtension().toLowerCase())) {
                                fileName = fileName.substring(0, dotIndex);
                            }
                        }
                        file = new File(file.getParentFile(), fileName + "." + fileType.getDefaultExtension());
                    }
                    if (file.isDirectory() || (file.exists() && !file.canWrite())) {
                        Messages.showWarningDialog(APIComparatorBundle.message("comparator.fileChooser.snapshot.save.warning.message", file.getPath()), APIComparatorBundle.message("comparator.fileChooser.snapshot.save.title"));
                    } else {
                        boolean canWrite = file.createNewFile();
                        if (!canWrite) {
                            canWrite = Messages.showYesNoDialog(APIComparatorBundle.message("comparator.fileChooser.snapshot.save.fileexists.message", file.getPath()), APIComparatorBundle.message("comparator.fileChooser.snapshot.save.title"), Messages.getQuestionIcon()) == 0;
                            if (canWrite) {
                                file.delete();
                            }
                        }
                        if (canWrite) {
                            final File snapshotFile = file;
                            ProgressManager.getInstance().runProcessWithProgressSynchronously(new Runnable() {

                                public void run() {
                                    try {
                                        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(snapshotFile));
                                        try {
                                            ProgressManager progressManager = ProgressManager.getInstance();
                                            ProgressIndicator indicator = progressManager.getProgressIndicator();
                                            if (fileType.equals(StdFileTypes.ARCHIVE)) {
                                                outputStream = new ZipOutputStream(outputStream);
                                                ((ZipOutputStream) outputStream).putNextEntry(new ZipEntry("snapshot.xml"));
                                            }
                                            TreeItemModel model = (TreeItemModel) tree.getModel();
                                            TreeItem item = (TreeItem) model.getRoot();
                                            indicator.setText(APIComparatorBundle.message("comparator.createsnapshot.progress.preparing"));
                                            final Element state = item.getState();
                                            if (indicator.isCanceled()) {
                                                throw new ProcessCanceledException();
                                            }
                                            indicator.setText(APIComparatorBundle.message("comparator.createsnapshot.progress.saving", snapshotFile.getPath()));
                                            JDOMUtil.writeDocument(new Document(state), outputStream, "\n");
                                        } finally {
                                            outputStream.close();
                                        }
                                    } catch (Exception e) {
                                        Plugin.LOG.error(e);
                                        throw new ProcessCanceledException();
                                    }
                                }
                            }, APIComparatorBundle.message("comparator.createsnapshot.progress.title"), true, DataKeys.PROJECT.getData(e.getDataContext()));
                            WindowManager windowManager = WindowManager.getInstance();
                            Project project = DataKeys.PROJECT.getData(e.getDataContext());
                            if (project != null) {
                                StatusBar statusBar = windowManager.getStatusBar(project);
                                statusBar.setInfo(APIComparatorBundle.message("comparator.fileChooser.snapshot.save.success.message", file.getPath()));
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Plugin.LOG.error(ex);
        }
    }
