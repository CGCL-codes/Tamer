    public void createSelfExtractingArchive(boolean noSrc, boolean noClasses, boolean isGlobalRwx) {
        File srcFile = getParent().getProjectArchive().getArchiveFile();
        File destFile = new File(getLayout().getClassesDir(), srcFile.getName());
        destFile.delete();
        final ClassesArchive classesArchive = getParent().getClassesArchive();
        classesArchive.visit(new ArgRunnable<ClassesEntry>() {

            public void run(ClassesEntry entry) {
                if (entry.isClasspathLib()) {
                    String name = entry.getArchiveEntry().getName();
                    classesArchive.createArchive(name);
                }
            }
        });
        getParent().getProjectArchive().createArchive(noSrc, noClasses, isGlobalRwx);
        FileUtils.copyFile(srcFile, destFile);
        getParent().getClassesArchive().createArchive(extractingArchiveName);
    }
