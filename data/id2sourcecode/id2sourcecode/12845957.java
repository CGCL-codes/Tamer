                        @Override
                        protected IStatus run(IProgressMonitor monitor) {
                            for (int i = 0; i < thumbs.length; i++) {
                                if (IO.isValidImageFile(thumbs[i].getName())) {
                                    try {
                                        FileUtils.copyFileToDirectory(thumbs[i], dir2);
                                        thumbs[i].delete();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            return Status.OK_STATUS;
                        }
