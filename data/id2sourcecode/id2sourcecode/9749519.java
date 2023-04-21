    }

    private void runCycle() {
        if (!shutdownRequested) notify(new CycleStartEvent(this));
        if (!shutdownRequested) for (currentDir = 0; currentDir < dirs.length; currentDir++) {
            File dir = PathNormalizer.normalize(dirs[currentDir]);
            File originalDir = dir;
            if (checkForDirectoryJump(dir)) continue;
            notify(new DirectoryLookupStartEvent(this, dir));
            if (shutdownRequested) return;
            long filesLookupTime = System.currentTimeMillis();
            File[] fls = dir.listFiles(filter);
            if (fls == null) {
                System.err.println("Warning: directory " + dir + " does not exist");
                fls = new File[0];
            }
            if (filesSortComparator != null) {
                if (verbose) System.out.println("Sorting files by  " + filesSortComparator);
                Arrays.sort(fls, filesSortComparator);
            }
            String[] files = new String[fls.length];
            for (int i = 0; i < files.length; i++) files[i] = fls[i].getName();
            String[] movedFiles = new String[files.length];
            int failedToMoveCount = 0;
            if (autoMove) {
                File autoMoveDir = getAutoMoveDirectory(dir);
                for (int j = 0; j < files.length; j++) {
                    File orig = new File(dir, files[j]);
                    File dest = new File(autoMoveDir, files[j]);
                    if (dest.exists()) {
                        if (verbose) System.out.println("[Automove] Attempting to delete existing " + dest.getAbsolutePath());
                        if (!dest.delete()) {
                            notify(new ExceptionSignal(new AutomoveDeleteException(orig, dest, "Could not delete " + dest.getAbsolutePath()), this));
                            failedToMoveCount++;
                            continue;
                        } else if (verbose) System.out.println("[Automove] Deleted " + dest.getAbsolutePath());
                    }
                    if (verbose) System.out.println("[Automove] Moving " + orig.getAbsolutePath() + " to " + autoMoveDir.getAbsolutePath() + File.separator);
                    autoMoveDir.mkdirs();
                    try {
                        boolean proceed;
                        if (bypassLockedFiles) {
                            RandomAccessFile raf = new RandomAccessFile(orig, "rw");
                            FileChannel channel = raf.getChannel();
                            if (channel.tryLock() == null) {
                                if (verbose) System.out.println("[Automove] File " + orig.getAbsolutePath() + " is locked, ignoring");
                                failedToMoveCount++;
                                proceed = false;
                            } else {
                                proceed = true;
                            }
                            channel.close();
                        } else proceed = true;
                        if (proceed) {
                            if (!orig.renameTo(dest)) {
                                notify(new ExceptionSignal(new AutomoveException(orig, dest, "Could not move " + orig.getName() + " to " + dest.getAbsolutePath()), this));
                                failedToMoveCount++;
                            } else {
                                notify(new FileMovedEvent(this, orig, dest));
                                movedFiles[j] = dest.getName();
                                if (j + 1 == files.length) dir = autoMoveDir;
                                if (verbose) System.out.println("[Automove] Moved " + orig.getAbsolutePath() + " to " + autoMoveDir.getAbsolutePath() + File.separator);
                            }
                        }
                    } catch (FileNotFoundException e) {
                        notify(new ExceptionSignal(new AutomoveException(orig, dest, "Could not verify lock on " + orig.getName()), this));
                        failedToMoveCount++;
                    } catch (IOException e) {
                        notify(new ExceptionSignal(new AutomoveException(orig, dest, "Tentative lock attempt failed on " + orig.getName()), this));
                        failedToMoveCount++;
                    }
                }
            }
            if (autoMove) {
                String[] tmp = new String[files.length - failedToMoveCount];
                int c = 0;
                for (int i = 0; i < movedFiles.length; i++) if (movedFiles[i] != null) tmp[c++] = movedFiles[i];
                files = tmp;
            }
            if (files.length > 0) {
                notify(new FileSetFoundEvent(this, dir, files));
            } else {
            }
            if (shutdownRequested) return;
            if (sendSingleFileEvent) {
                for (int j = 0; j < files.length; j++) {
                    File file;
                    file = new File(dir, files[j]);
                    notify(new FileFoundEvent(this, file));
                    if (shutdownRequested) return;
                }
                if (shutdownRequested) return;
            }
            if (isTimeBased()) {
                if (verbose) System.out.println("Computing new base time");
                if (timeBasedOnLastLookup) {
                    baseTime[currentDir] = filesLookupTime;
                } else {
                    for (int j = 0; j < files.length; j++) {
                        File file = new File(dir, files[j]);
                        long lastModifiedTime = file.lastModified();
                        if (lastModifiedTime > baseTime[currentDir]) {
                            baseTime[currentDir] = lastModifiedTime;
                        }
                    }
                    if (verbose) System.out.println("Basetime for " + dirs[currentDir] + " is " + baseTime[currentDir]);
                }
            }
            notify(new DirectoryLookupEndEvent(this, originalDir));
        }
