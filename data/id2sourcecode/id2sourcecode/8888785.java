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
