    public MakefileWeaver(File makefile) throws FileNotFoundException, Exception {
        this.makefile = makefile;
        this.makefileBackup = new File(makefile + ".orig");
        FileUtils.copyFile(makefile, makefileBackup);
    }
