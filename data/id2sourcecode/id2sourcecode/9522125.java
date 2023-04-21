            public boolean accept(File src) {
                try {
                    final String absPath = src.getAbsolutePath().replace('\\', '/');
                    final String uri = absPath.substring(srcPrefix.length());
                    final File dest = new File(remoteAreaBase, uri);
                    if (src.isDirectory()) {
                        dest.mkdirs();
                        final AtomicInteger fileCnt = new AtomicInteger(0);
                        final AtomicInteger dirCnt = new AtomicInteger(0);
                        final AtomicInteger otherCnt = new AtomicInteger(0);
                        src.listFiles(new FileFilter() {

                            public boolean accept(File pathname) {
                                if (pathname.isDirectory()) {
                                    dirCnt.incrementAndGet();
                                } else if (pathname.isFile()) {
                                    fileCnt.incrementAndGet();
                                } else {
                                    otherCnt.incrementAndGet();
                                }
                                return false;
                            }
                        });
                        if (dirCnt.intValue() == 0) {
                            listOfDirs.println(uri + "/ " + dirCnt + " " + fileCnt + " " + otherCnt);
                            listOfDirs.flush();
                        }
                        dirCount++;
                        progressObject.setTransferredDirs(dirCount);
                    } else {
                        dest.getParentFile().mkdirs();
                        FileUtils.copyFile(src, dest);
                        listOfFiles.println(uri + " " + src.length());
                        listOfFiles.flush();
                        fileCount++;
                        progressObject.setTransferredFiles(fileCount);
                        byteCount += src.length();
                        progressObject.setTransferredBytes(byteCount);
                        if (byteCount > nextpropsave) {
                            saveProgress(remoteContentProperties, progressObject, "--- INCOMPLETE --- " + comment);
                            nextpropsave = byteCount + PROPSAVE_BYTE_RATE;
                        }
                    }
                } catch (IOException e) {
                    throw new IllegalArgumentException("error while storing " + src, e);
                }
                return false;
            }
