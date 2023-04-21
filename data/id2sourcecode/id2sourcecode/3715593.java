    private boolean installPackages(Collection<Package> firstinstallList) throws PackageManagerException {
        boolean ret = false;
        final ArrayList<Package> installList = new ArrayList<Package>();
        for (final Package pkg : firstinstallList) if (pkg.isPackageManager()) installList.add((Package) DeepObjectCopy.clone(pkg, this)); else installList.add(0, (Package) DeepObjectCopy.clone(pkg, this));
        firstinstallList.removeAll(firstinstallList);
        final List<File> filesToDelete = new ArrayList<File>();
        final List<File> directoriesToDelete = new ArrayList<File>();
        List<Package> PackagesForDatabase = new ArrayList<Package>();
        final List<InstallationLogEntry> log = new ArrayList<InstallationLogEntry>();
        int sizeOfS = 0;
        lock.writeLock().lock();
        try {
            final TreeSet<File> s = new TreeSet<File>();
            final int listlength = installList.size();
            int listactuallystands = 0;
            int actuallyProgress = getActprogress();
            for (final Package pkg : installList) {
                pkg.install(new File(testinstallDir), log, archivesDir, this);
                final List<File> dirsForPackage = new ArrayList<File>();
                final List<File> filesForPackage = new ArrayList<File>();
                for (final File fi : pkg.getDirectoryList()) if (!fi.getPath().replace(testinstallDir, " ").equalsIgnoreCase(" ")) {
                    s.add(new File(fi.getPath().replace(testinstallDir, " ").trim()));
                    if (fi.getPath().replace(new File(testinstallDir).getPath(), "").length() != 0) dirsForPackage.add(new File(fi.getPath().replace(testinstallDir, "")));
                }
                for (final File fi : pkg.getFileList()) if (!fi.getPath().replace(testinstallDir, " ").equalsIgnoreCase(" ")) {
                    s.add(new File(fi.getPath().replace(testinstallDir, " ").trim()));
                    filesForPackage.add(new File(fi.getPath().replace(testinstallDir, "")));
                }
                pkg.setDirectoryList(dirsForPackage);
                pkg.setFileList(filesForPackage);
                PackagesForDatabase.add(pkg);
                listactuallystands++;
                setActprogress(actuallyProgress + new Double(listactuallystands / listlength * 20).intValue());
            }
            actuallyProgress = getActprogress();
            sizeOfS = s.size();
            final Iterator<File> it = s.iterator();
            File iteratorFile = null;
            boolean secondTime = false;
            int iteratorFiles = 0;
            while (it.hasNext() || secondTime) {
                if (secondTime) secondTime = false; else iteratorFile = it.next();
                if (testinstallDir.equalsIgnoreCase(iteratorFile.getPath())) iteratorFile = it.next();
                if (testinstallDir.equalsIgnoreCase(iteratorFile.getPath() + File.separator)) iteratorFile = it.next();
                File newFile = new File(installDir + iteratorFile.getPath());
                File oldFile = new File(testinstallDir + iteratorFile.getPath());
                if (!newFile.isDirectory() && oldFile.isDirectory()) {
                    if (!oldFile.renameTo(newFile)) {
                        addWarning(PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("packageManager.installPackages.problem1", "No entry found for packageManager.installPackages.problem1") + " " + newFile.getName() + " " + PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("packageManager.installPackages.problem2", "No entry found for packageManager.installPackages.problem2"));
                        logger.error(PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("packageManager.installPackages.problem1", "No entry found for packageManager.installPackages.problem1") + " " + newFile.getName() + " " + PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("packageManager.installPackages.problem2", "No entry found for packageManager.installPackages.problem2"));
                    }
                    boolean next = true;
                    while (it.hasNext() && next) {
                        final File iteratorFileNext = it.next();
                        if (iteratorFileNext.getPath().length() >= iteratorFile.getPath().length()) {
                            if (iteratorFile.getPath().equalsIgnoreCase(iteratorFileNext.getPath().substring(0, iteratorFile.getPath().length()))) {
                                if (new File(installDir + iteratorFileNext).isDirectory()) directoriesToDelete.add(new File(testinstallDir + iteratorFileNext)); else if (new File(installDir + iteratorFileNext).isFile()) filesToDelete.add(new File(testinstallDir + iteratorFileNext)); else {
                                    next = false;
                                    iteratorFile = iteratorFileNext;
                                }
                            } else {
                                next = false;
                                iteratorFile = iteratorFileNext;
                            }
                        } else {
                            next = false;
                            iteratorFile = iteratorFileNext;
                        }
                    }
                    if (testinstallDir.equalsIgnoreCase(iteratorFile.getPath()) && it.hasNext()) iteratorFile = it.next();
                    if (testinstallDir.equalsIgnoreCase(iteratorFile.getPath() + File.separator) && it.hasNext()) iteratorFile = it.next();
                    newFile = new File(installDir + iteratorFile.getPath());
                    oldFile = new File(testinstallDir + iteratorFile.getPath());
                }
                if (oldFile.isDirectory()) {
                    if (newFile.isDirectory()) directoriesToDelete.add(oldFile); else if (!newFile.isDirectory()) secondTime = true; else {
                        logger.error(PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("packageManager.installPackages.problem1", "No entry found for packageManager.installPackages.problem1") + " " + newFile.getName() + " " + PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("packageManager.installPackages.problem2", "No entry found for packageManager.installPackages.problem2"));
                        addWarning(PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("packageManager.installPackages.problem1", "No entry found for packageManager.installPackages.problem1") + " " + newFile.getName() + " " + PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("packageManager.installPackages.problem2", "No entry found for packageManager.installPackages.problem2"));
                    }
                } else if (oldFile.isFile()) {
                    if (newFile.isFile()) filesToDelete.add(oldFile); else if (!newFile.isFile()) {
                        if (!oldFile.renameTo(newFile)) {
                            addWarning(PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("packageManager.installPackages.problem1", "No entry found for packageManager.installPackages.problem1") + " " + newFile.getName() + " " + PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("packageManager.installPackages.problem2", "No entry found for packageManager.installPackages.problem2"));
                            logger.error(PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("packageManager.installPackages.problem1", "No entry found for packageManager.installPackages.problem1") + " " + newFile.getName() + " " + PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("packageManager.installPackages.problem2", "No entry found for packageManager.installPackages.problem2"));
                        }
                    } else {
                        addWarning(PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("packageManager.installPackages.problem1", "No entry found for packageManager.installPackages.problem1") + " " + newFile.getName() + " " + PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("packageManager.installPackages.problem2", "No entry found for packageManager.installPackages.problem2"));
                        logger.error(PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("packageManager.installPackages.problem1", "No entry found for packageManager.installPackages.problem1") + " " + newFile.getName() + " " + PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("packageManager.installPackages.problem2", "No entry found for packageManager.installPackages.problem2"));
                    }
                } else if (newFile.isFile()) {
                } else if (newFile.isDirectory()) {
                } else if (testinstallDir.equalsIgnoreCase(iteratorFile.getPath())) {
                } else if (testinstallDir.equalsIgnoreCase(iteratorFile.getPath() + File.separator)) {
                } else {
                    logger.error(PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("packageManager.installPackages.problem1", "No entry found for packageManager.installPackages.problem1") + " " + newFile.getName() + " " + PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("packageManager.installPackages.problem2", "No entry found for packageManager.installPackages.problem2"));
                    addWarning(PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("packageManager.installPackages.problem1", "No entry found for packageManager.installPackages.problem1") + " " + newFile.getName() + " " + PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("packageManager.installPackages.problem2", "No entry found for packageManager.installPackages.problem2"));
                }
                iteratorFiles++;
                setActprogress(actuallyProgress + new Double(iteratorFiles / sizeOfS * 18).intValue());
            }
            for (final Package pack : PackagesForDatabase) {
                installedPackages.addPackage(pack);
                installedPackages.save();
            }
            ret = true;
        } catch (final PackageManagerException t) {
            t.printStackTrace();
            for (final Package pkg : PackagesForDatabase) for (final File file : pkg.getFileList()) filesToDelete.add(new File(testinstallDir + file.getPath()));
            for (final Package pkg : PackagesForDatabase) for (final File file : pkg.getDirectoryList()) filesToDelete.add(new File(testinstallDir + file.getPath()));
            rollbackInstallation(log);
            PackagesForDatabase = new ArrayList<Package>();
            logger.error(t);
            addWarning(t.toString());
        } catch (final IOException e) {
            e.printStackTrace();
            logger.error(e);
            addWarning(e.toString());
        } finally {
            lock.writeLock().unlock();
        }
        if (filesToDelete != null) if (filesToDelete.size() > 0) for (int n = filesToDelete.size() - 1; n > 0; n--) if (filesToDelete.get(n).isFile()) if (!filesToDelete.get(n).delete()) {
            addWarning(filesToDelete.get(n).getPath() + PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("interface.notRemove", "No entry found for interface.notRemove"));
            logger.error(filesToDelete.get(n).getPath() + PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("interface.notRemove", "No entry found for interface.notRemove"));
        }
        if (directoriesToDelete != null) if (directoriesToDelete.size() > 0) for (int n = directoriesToDelete.size() - 1; n > 0; n--) if (directoriesToDelete.get(n).isDirectory()) if (directoriesToDelete.get(n).listFiles().length == 0) if (!directoriesToDelete.get(n).delete()) {
            addWarning(PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("interface.DirectoryUndeleteable", "No entry found for interface.DirectoryUndeleteable") + directoriesToDelete.get(n).getName());
            logger.error(PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("interface.DirectoryUndeleteable", "No entry found for interface.DirectoryUndeleteable") + directoriesToDelete.get(n).getName());
        }
        return ret;
    }
