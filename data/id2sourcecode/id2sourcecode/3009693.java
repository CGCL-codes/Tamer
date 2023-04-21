    public void execute() throws BuildException {
        if (usedMatchingTask) {
            log("DEPRECATED - Use of the implicit FileSet is deprecated.  " + "Use a nested fileset element instead.", quiet ? Project.MSG_VERBOSE : verbosity);
        }
        if (file == null && dir == null && filesets.size() == 0 && rcs == null) {
            throw new BuildException("At least one of the file or dir " + "attributes, or a nested resource collection, " + "must be set.");
        }
        if (quiet && failonerror) {
            throw new BuildException("quiet and failonerror cannot both be " + "set to true", getLocation());
        }
        if (file != null) {
            if (file.exists()) {
                if (file.isDirectory()) {
                    log("Directory " + file.getAbsolutePath() + " cannot be removed using the file attribute.  " + "Use dir instead.", quiet ? Project.MSG_VERBOSE : verbosity);
                } else {
                    log("Deleting: " + file.getAbsolutePath());
                    if (!delete(file)) {
                        handle("Unable to delete file " + file.getAbsolutePath());
                    }
                }
            } else if (isDanglingSymlink(file)) {
                log("Trying to delete file " + file.getAbsolutePath() + " which looks like a broken symlink.", quiet ? Project.MSG_VERBOSE : verbosity);
                if (!delete(file)) {
                    handle("Unable to delete file " + file.getAbsolutePath());
                }
            } else {
                log("Could not find file " + file.getAbsolutePath() + " to delete.", quiet ? Project.MSG_VERBOSE : verbosity);
            }
        }
        if (dir != null && !usedMatchingTask) {
            if (dir.exists() && dir.isDirectory()) {
                if (verbosity == Project.MSG_VERBOSE) {
                    log("Deleting directory " + dir.getAbsolutePath());
                }
                removeDir(dir);
            } else if (isDanglingSymlink(dir)) {
                log("Trying to delete directory " + dir.getAbsolutePath() + " which looks like a broken symlink.", quiet ? Project.MSG_VERBOSE : verbosity);
                if (!delete(dir)) {
                    handle("Unable to delete directory " + dir.getAbsolutePath());
                }
            }
        }
        Resources resourcesToDelete = new Resources();
        resourcesToDelete.setProject(getProject());
        resourcesToDelete.setCache(true);
        Resources filesetDirs = new Resources();
        filesetDirs.setProject(getProject());
        filesetDirs.setCache(true);
        FileSet implicit = null;
        if (usedMatchingTask && dir != null && dir.isDirectory()) {
            implicit = getImplicitFileSet();
            implicit.setProject(getProject());
            filesets.add(implicit);
        }
        for (int i = 0, size = filesets.size(); i < size; i++) {
            FileSet fs = (FileSet) filesets.get(i);
            if (fs.getProject() == null) {
                log("Deleting fileset with no project specified;" + " assuming executing project", Project.MSG_VERBOSE);
                fs = (FileSet) fs.clone();
                fs.setProject(getProject());
            }
            final File fsDir = fs.getDir();
            if (fsDir == null) {
                throw new BuildException("File or Resource without directory or file specified");
            } else if (!fsDir.isDirectory()) {
                handle("Directory does not exist: " + fsDir);
            } else {
                DirectoryScanner ds = fs.getDirectoryScanner();
                final String[] files = ds.getIncludedFiles();
                resourcesToDelete.add(new ResourceCollection() {

                    public boolean isFilesystemOnly() {
                        return true;
                    }

                    public int size() {
                        return files.length;
                    }

                    public Iterator iterator() {
                        return new FileResourceIterator(getProject(), fsDir, files);
                    }
                });
                if (includeEmpty) {
                    filesetDirs.add(new ReverseDirs(getProject(), fsDir, ds.getIncludedDirectories()));
                }
                if (removeNotFollowedSymlinks) {
                    String[] n = ds.getNotFollowedSymlinks();
                    if (n.length > 0) {
                        String[] links = new String[n.length];
                        System.arraycopy(n, 0, links, 0, n.length);
                        Arrays.sort(links, ReverseDirs.REVERSE);
                        for (int l = 0; l < links.length; l++) {
                            try {
                                SYMLINK_UTILS.deleteSymbolicLink(new File(links[l]), this);
                            } catch (java.io.IOException ex) {
                                handle(ex);
                            }
                        }
                    }
                }
            }
        }
        resourcesToDelete.add(filesetDirs);
        if (rcs != null) {
            Restrict exists = new Restrict();
            exists.add(EXISTS);
            exists.add(rcs);
            Sort s = new Sort();
            s.add(REVERSE_FILESYSTEM);
            s.add(exists);
            resourcesToDelete.add(s);
        }
        try {
            if (resourcesToDelete.isFilesystemOnly()) {
                for (Iterator iter = resourcesToDelete.iterator(); iter.hasNext(); ) {
                    Resource r = (Resource) iter.next();
                    File f = ((FileProvider) r.as(FileProvider.class)).getFile();
                    if (!f.exists()) {
                        continue;
                    }
                    if (!(f.isDirectory()) || f.list().length == 0) {
                        log("Deleting " + f, verbosity);
                        if (!delete(f) && failonerror) {
                            handle("Unable to delete " + (f.isDirectory() ? "directory " : "file ") + f);
                        }
                    }
                }
            } else {
                handle(getTaskName() + " handles only filesystem resources");
            }
        } catch (Exception e) {
            handle(e);
        } finally {
            if (implicit != null) {
                filesets.remove(implicit);
            }
        }
    }
