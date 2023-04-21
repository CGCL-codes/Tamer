    public void handle(File f) throws IOException {
        if (FileTypes.isBytecodeFile(f)) {
            FileInputStream fis = null;
            String className = null;
            try {
                fis = new FileInputStream(f);
                CtClass cl = this.pool.makeClassIfNew(fis);
                className = cl.getName();
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }
            String file = className.replaceAll("\\.", "/");
            String abs = this.dir.getAbsolutePath() + "/" + file + FileTypes.BYTECODE_FILE_EXT;
            FileUtils.copyFile(f, new File(abs));
        }
    }
