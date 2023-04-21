    public void execute() throws BuildException {
        if (cachedIOException != null) throw new BuildException(cachedIOException, getLocation()); else if (cachedSAXException != null) throw new BuildException(cachedSAXException, getLocation()); else if (tempBuildDir == null) throw new BuildException("intermediaryBuildDir attribute must be specified on the compilewithwalls element", getLocation()); else if (javac == null) throw new BuildException("There must be a nested javac element", getLocation()); else if (walls == null) throw new BuildException("There must be a nested walls element", getLocation()); else if (setWallsTwice) throw new BuildException("compilewithwalls task only supports one nested walls element or one walls attribute", getLocation()); else if (setJavacTwice) throw new BuildException("compilewithwalls task only supports one nested javac element", getLocation());
        getProject().addTaskDefinition("SilentMove", SilentMove.class);
        getProject().addTaskDefinition("SilentCopy", SilentCopy.class);
        File destDir = javac.getDestdir();
        Path src = javac.getSrcdir();
        if (src == null) throw new BuildException("Javac inside compilewithwalls must have a srcdir specified");
        String[] list = src.list();
        File[] tempSrcDirs1 = new File[list.length];
        for (int i = 0; i < list.length; i++) {
            tempSrcDirs1[i] = getProject().resolveFile(list[i]);
        }
        String[] classpaths = new String[0];
        if (javac.getClasspath() != null) classpaths = javac.getClasspath().list();
        File temp = null;
        for (int i = 0; i < classpaths.length; i++) {
            temp = new File(classpaths[i]);
            if (temp.isDirectory()) {
                for (int n = 0; n < tempSrcDirs1.length; n++) {
                    if (tempSrcDirs1[n].compareTo(temp) == 0) throw new BuildException("The classpath cannot contain any of the\n" + "src directories, but it does.\n" + "srcdir=" + tempSrcDirs1[n]);
                }
            }
        }
        List srcDirs2 = new ArrayList();
        for (int i = 0; i < tempSrcDirs1.length; i++) {
            if (tempSrcDirs1[i].exists()) srcDirs2.add(tempSrcDirs1[i]);
        }
        if (destDir == null) throw new BuildException("destdir was not specified in nested javac task", getLocation());
        if (file1IsChildOfFile2(tempBuildDir, destDir)) throw new BuildException("intermediaryBuildDir attribute cannot be specified\n" + "to be the same as destdir or inside desdir of the javac task.\n" + "This is an intermediary build directory only used by the\n" + "compilewithwalls task, not the class file output directory.\n" + "The class file output directory is specified in javac's destdir attribute", getLocation());
        if (!tempBuildDir.exists()) {
            tempBuildDir.mkdirs();
            log("created direction=" + tempBuildDir, Project.MSG_VERBOSE);
        }
        Iterator iter = walls.getPackagesToCompile();
        while (iter.hasNext()) {
            Package toCompile = (Package) iter.next();
            File buildSpace = toCompile.getBuildSpace(tempBuildDir);
            if (!buildSpace.exists()) {
                buildSpace.mkdir();
                log("created directory=" + buildSpace, Project.MSG_VERBOSE);
            }
            FileSet javaIncludes2 = toCompile.getJavaCopyFileSet(getProject(), getLocation());
            for (int i = 0; i < srcDirs2.size(); i++) {
                File srcDir = (File) srcDirs2.get(i);
                javaIncludes2.setDir(srcDir);
                log(toCompile.getPackage() + ": sourceDir[" + i + "]=" + srcDir + " destDir=" + buildSpace, Project.MSG_VERBOSE);
                copyFiles(srcDir, buildSpace, javaIncludes2);
            }
            Path srcDir2 = toCompile.getSrcPath(tempBuildDir, getProject());
            Path classPath = toCompile.getClasspath(tempBuildDir, getProject());
            if (javac.getClasspath() != null) classPath.addExisting(javac.getClasspath());
            Javac buildSpaceJavac = new Javac();
            buildSpaceJavac.setProject(getProject());
            buildSpaceJavac.setOwningTarget(getOwningTarget());
            buildSpaceJavac.setTaskName(getTaskName());
            log(toCompile.getPackage() + ": Compiling");
            log(toCompile.getPackage() + ": sourceDir=" + srcDir2, Project.MSG_VERBOSE);
            log(toCompile.getPackage() + ": classPath=" + classPath, Project.MSG_VERBOSE);
            log(toCompile.getPackage() + ": destDir=" + buildSpace, Project.MSG_VERBOSE);
            buildSpaceJavac.setSrcdir(srcDir2);
            buildSpaceJavac.setDestdir(buildSpace);
            buildSpaceJavac.setClasspath(classPath);
            buildSpaceJavac.setBootclasspath(javac.getBootclasspath());
            buildSpaceJavac.setExtdirs(javac.getExtdirs());
            buildSpaceJavac.setEncoding(javac.getEncoding());
            buildSpaceJavac.setNowarn(javac.getNowarn());
            buildSpaceJavac.setDebug(javac.getDebug());
            buildSpaceJavac.setDebugLevel(javac.getDebugLevel());
            buildSpaceJavac.setOptimize(javac.getOptimize());
            buildSpaceJavac.setDeprecation(javac.getDeprecation());
            buildSpaceJavac.setTarget(javac.getTarget());
            buildSpaceJavac.setVerbose(javac.getVerbose());
            buildSpaceJavac.setDepend(javac.getDepend());
            buildSpaceJavac.setIncludeantruntime(javac.getIncludeantruntime());
            buildSpaceJavac.setIncludejavaruntime(javac.getIncludejavaruntime());
            buildSpaceJavac.setFork(javac.isForkedJavac());
            buildSpaceJavac.setExecutable(javac.getJavacExecutable());
            buildSpaceJavac.setMemoryInitialSize(javac.getMemoryInitialSize());
            buildSpaceJavac.setMemoryMaximumSize(javac.getMemoryMaximumSize());
            buildSpaceJavac.setFailonerror(javac.getFailonerror());
            buildSpaceJavac.setSource(javac.getSource());
            buildSpaceJavac.setCompiler(javac.getCompiler());
            Javac.ImplementationSpecificArgument arg;
            String[] args = javac.getCurrentCompilerArgs();
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    arg = buildSpaceJavac.createCompilerArg();
                    arg.setValue(args[i]);
                }
            }
            buildSpaceJavac.setProject(getProject());
            buildSpaceJavac.perform();
            copyFiles(buildSpace, destDir, toCompile.getClassCopyFileSet(getProject(), getLocation()));
        }
    }
