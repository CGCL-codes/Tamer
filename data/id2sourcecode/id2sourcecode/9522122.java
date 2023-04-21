    public Collection<File> retrieve(JobDependency jobDependency, final File localAreaDir, final TransferProgress progressObject) throws IOException {
        final List<File> retrievedFiles = new ArrayList<File>();
        final String area = jobDependency.parseArea().toString();
        final File remoteJobDir = new File(remoteCatalogRootDir, BuildRepository.executionBase(jobDependency));
        final File remoteAreaBase = new File(remoteJobDir, area);
        LOGGER.fine(String.format("LocalJobTransfer.retrieve %s", localAreaDir));
        final int remotePrefixLength = remoteAreaBase.getAbsolutePath().length() + 1;
        final PushingDirectoryTraversal t = new PushingDirectoryTraversal(new FileFilter() {

            int fileCount = 0;

            int dirCount = 0;

            int byteCount = 0;

            public boolean accept(File src) {
                try {
                    final String uri = src.getAbsolutePath().substring(remotePrefixLength);
                    final File dest = new File(localAreaDir, uri);
                    retrievedFiles.add(dest);
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
        t.traverse(remoteAreaBase);
        return retrievedFiles;
    }
