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
