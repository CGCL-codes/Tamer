    private void openFiles(final File dir) throws IOException {
        dir.mkdirs();
        for (int instanceIndex = 0; instanceIndex < 100; instanceIndex++) {
            String fileName = "OmegaT" + (instanceIndex > 0 ? ("-" + instanceIndex) : "");
            lockFile = new File(dir, fileName + ".log.lck");
            lockStream = new FileOutputStream(lockFile);
            if (lockStream.getChannel().tryLock() != null) {
                rotate(dir, fileName);
                setEncoding(OConsts.UTF8);
                setOutputStream(new FileOutputStream(new File(dir, fileName + ".log"), true));
                break;
            }
        }
        setErrorManager(new ErrorManager());
    }
