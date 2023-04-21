    public boolean isUpToDate(File[] pSchemaFiles, File[] pBindingFiles, File[] pDependsFiles, List pProducesList) {
        FileSet[] myProduces = getProduces();
        if (myProduces.length == 0) {
            log("No nested 'produces' elements, up-to-date check returns false", Project.MSG_VERBOSE);
            return false;
        }
        boolean result = true;
        long firstTarget = 0;
        File firstTargetFile = null;
        for (int i = 0; i < myProduces.length; i++) {
            File dir = myProduces[i].getDir(getProject());
            if (dir == null) {
                dir = getTarget();
                if (dir == null) {
                    dir = getProject().getBaseDir();
                }
                myProduces[i].setDir(dir);
            }
            if (!dir.exists()) {
                log("The directory specified by the nested 'produces' element #" + i + " does not exist, up-to-date check returns false", Project.MSG_VERBOSE);
                result = false;
                continue;
            }
            DirectoryScanner scanner = myProduces[i].getDirectoryScanner(getProject());
            scanner.scan();
            String[] files = scanner.getIncludedFiles();
            if (files.length == 0) {
                log("The fileset specified by the nested 'produces' element #" + i + " is empty, up-to-date check returns false", Project.MSG_VERBOSE);
                result = false;
            }
            for (int j = 0; j < files.length; j++) {
                File f = new File(dir, files[j]).getAbsoluteFile();
                if (pProducesList != null) {
                    pProducesList.add(f);
                }
                long l = f.lastModified();
                if (l == -1) {
                    log("Unable to determine timestamp of target file " + f + ", up-to-date check returns false.", Project.MSG_VERBOSE);
                    result = false;
                }
                if (firstTargetFile == null || firstTarget > l) {
                    firstTargetFile = f;
                    firstTarget = l;
                }
            }
        }
        if (isForce()) {
            log("Force option is set, up-to-date check returns false", Project.MSG_VERBOSE);
            result = false;
        }
        if (!result) {
            return false;
        }
        List sourceFiles = new ArrayList();
        for (int i = 0; i < pSchemaFiles.length; i++) {
            sourceFiles.add(pSchemaFiles[i]);
        }
        for (int i = 0; i < pBindingFiles.length; i++) {
            sourceFiles.add(pBindingFiles[i]);
        }
        for (int i = 0; i < pDependsFiles.length; i++) {
            sourceFiles.add(pDependsFiles[i]);
        }
        long lastSource = 0;
        File lastSourceFile = null;
        for (Iterator iter = sourceFiles.iterator(); iter.hasNext(); ) {
            File f = (File) iter.next();
            long l = f.lastModified();
            if (l == -1) {
                log("Unable to determine timestamp of source file " + f + ", up-to-date check returns false.", Project.MSG_VERBOSE);
                result = false;
            }
            if (lastSourceFile == null || lastSource < l) {
                lastSource = l;
                lastSourceFile = f;
            }
        }
        if (lastSourceFile == null) {
            log("No source files found, up-to-date check returns false.", Project.MSG_VERBOSE);
            return false;
        }
        if (!result) {
            return false;
        }
        try {
            URL url = Generator.class.getClassLoader().getResource(Generator.class.getName().replace('.', '/') + ".class");
            if (url != null) {
                long l = url.openConnection().getLastModified();
                if (l != 0 && lastSource < l) {
                    log("Generator class is newer than any schema files, using Generator classes timestamp as schema timestamp.", Project.MSG_DEBUG);
                    lastSource = l;
                }
            }
        } catch (IOException e) {
        }
        if (lastSource >= firstTarget) {
            log("Source file " + lastSourceFile + " is more recent than target file " + firstTargetFile + ", up-to-date check returns false", Project.MSG_VERBOSE);
            return false;
        }
        log("All target files are up-to-date.", Project.MSG_VERBOSE);
        return true;
    }
