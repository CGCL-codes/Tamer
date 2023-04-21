    public static void copyThumbs(IFolder folder) {
        if (folder != null && folder.exists()) {
            IPath cache = ThumbFileUtils.getExplorerThumbContainer(Path.fromPortableString(folder.getName()));
            IPath tcache = ThumbFileUtils.getDefaultThumbContainer(folder.getFullPath());
            if (cache != null && tcache != null) {
                final File dir = cache.toFile();
                final File dir2 = tcache.toFile();
                final File[] thumbs = dir.listFiles();
                if (dir != null && dir.exists() && dir2 != null && dir2.exists() && thumbs != null && thumbs.length > 0) {
                    Job j = new Job("Copy Cache") {

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
                    };
                    j.setSystem(true);
                    j.schedule(500);
                }
            }
        }
    }
