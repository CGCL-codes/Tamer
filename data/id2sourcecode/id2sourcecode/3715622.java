    public Collection<Package> filesToRename(Collection<Package> packages) throws PackageManagerException {
        fromToFileMap = new HashMap<File, File>();
        removeDirectoryList = new ArrayList<File>();
        final ArrayList<Package> remove = new ArrayList<Package>();
        for (final Package pkg : packages) remove.add((Package) DeepObjectCopy.clone(pkg, this));
        String newDirName = null;
        if (!new File(oldInstallDir).isDirectory()) new File(oldInstallDir).mkdirs();
        lock.readLock().lock();
        try {
            for (int x = 0; x < remove.size(); x++) {
                final Package pack = installedPackages.getPackage(remove.get(x).getName());
                remove.remove(x);
                remove.add(x, pack);
            }
        } finally {
            lock.readLock().unlock();
        }
        for (int i = 0; i < remove.size(); i++) {
            final String dateForFolder = getFormattedDate();
            newDirName = new File(dateForFolder + "#" + remove.get(i).getName() + "#" + remove.get(i).getVersion().toString().replaceAll(":", "_").replaceAll("\\.", "_")).getPath();
            final String newDirNamePath = oldInstallDir + File.separator + newDirName;
            if (!new File(newDirNamePath).mkdir()) {
                addWarning(PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("DPKGPackageManager.getDebianFilePackages.unableToCreateDir", "NO entry for DPKGPackageManager.getDebianFilePackages.unableToCreateDir"));
                logger.error(PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("DPKGPackageManager.getDebianFilePackages.unableToCreateDir", "NO entry for DPKGPackageManager.getDebianFilePackages.unableToCreateDir"));
            }
            remove.get(i).setoldFolder(newDirName);
            for (int z = 0; z < remove.get(i).getFileList().size(); z++) {
                final String fi = remove.get(i).getFileList().get(z).getPath();
                if (!new File(installDir, fi).exists()) {
                    final List<File> removeDirs = new ArrayList<File>();
                    for (int n = 0; n < i; n++) {
                        for (final File file : remove.get(n).getDirectoryList()) removeDirs.add(new File(oldInstallDir + File.separator + remove.get(n).getoldFolder(), file.getPath()));
                        removeDirs.add(new File(oldInstallDir + File.separator + remove.get(n).getoldFolder()));
                    }
                    removeDirs.add(new File(newDirNamePath));
                    Collections.sort(removeDirs);
                    Collections.reverse(removeDirs);
                    for (final File file : removeDirs) if (!file.delete()) {
                        addWarning(PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("DPKGPackageManager.filesToRename.notExisting", "No entry found for DPKGPackageManager.filesToRename.notExisting") + " \n" + new File(installDir, fi).getPath());
                        logger.error(PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("DPKGPackageManager.filesToRename.notExisting", "No entry found for DPKGPackageManager.filesToRename.notExisting") + " \n" + new File(installDir, fi).getPath());
                    }
                }
                if (new File(installDir, fi).isFile()) fromToFileMap.put(new File(installDir, fi), new File(newDirNamePath, fi));
            }
            for (final File f : remove.get(i).getDirectoryList()) {
                new File(newDirNamePath, f.getPath()).mkdirs();
                if (!removeDirectoryList.contains(f)) removeDirectoryList.add(f);
            }
        }
        return remove;
    }
