    public void retrieve(ResourceId resourceId, final File destRootDir, final TransferProgress progressObject) throws IOException {
        final File cacheBase = resourceCacheBase(resourceId);
        final File srcRootDir = new File(cacheBase, "content");
        final int srcPrefixLength = srcRootDir.getAbsolutePath().length() + 1;
        final PushingDirectoryTraversal t = new PushingDirectoryTraversal(new FileFilter() {

            int fileCount = 0;

            int dirCount = 0;

            int byteCount = 0;

            public boolean accept(File src) {
                try {
                    final String uri = src.getAbsolutePath().substring(srcPrefixLength);
                    final File dest = new File(destRootDir, uri);
                    progressObject.addFile(dest);
                    if (src.isDirectory()) {
                        dest.mkdirs();
                        dirCount++;
                        progressObject.setTransferredDirs(dirCount);
                    } else {
                        dest.getParentFile().mkdirs();
                        FileUtils.copyFile(src, dest);
                        fileCount++;
                        progressObject.setTransferredFiles(fileCount);
                        byteCount += src.length();
                        progressObject.setTransferredBytes(byteCount);
                    }
                } catch (IOException e) {
                    throw new IllegalArgumentException("error while retrieving " + src, e);
                }
                return false;
            }
        });
        t.setWantDirs(true);
        t.setWantFiles(true);
        t.setSorted(false);
        t.traverse(srcRootDir);
    }
