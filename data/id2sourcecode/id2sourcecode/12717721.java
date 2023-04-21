    public void createJar() throws IOException {
        Manifest manifest = new Manifest();
        setDefaultAttributes(metaInfAttributes);
        for (String attrName : metaInfAttributes.keySet()) {
            manifest.getMainAttributes().putValue(attrName, metaInfAttributes.get(attrName));
        }
        removeOldJar();
        if (unpackaged) {
            for (File inputDir : inputDirectory) {
                FileUtils.copyFilesFromDir(inputDir, outputFile, getIncludes(), getExcludes());
            }
            File metaInfDir = new File(outputFile, "META-INF");
            metaInfDir.mkdirs();
            manifest.write(new FileOutputStream(new File(metaInfDir, "MANIFEST.MF")));
        } else {
            File parentFile = outputFile.getParentFile();
            if (parentFile != null && !parentFile.exists()) {
                parentFile.mkdirs();
            }
            JarOutputStream target = new JarOutputStream(new FileOutputStream(outputFile), manifest);
            if (inputDirectory != null) {
                Set<String> added = new HashSet<String>();
                for (File inputDir : inputDirectory) {
                    addFile(inputDir, target, inputDir.getCanonicalPath().length(), added);
                }
            }
            target.close();
        }
    }
