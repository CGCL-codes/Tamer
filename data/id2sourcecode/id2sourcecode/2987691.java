    private String copyFileToPublicDir(String logicalFileName, String inputFile, String playpenDir) throws ExecutorException {
        try {
            FileUtils.copyFileToDirectory(new File(inputFile), new File(publicDirPath + File.separator + playpenDir));
        } catch (IOException e) {
            throw new ExecutorException(e);
        }
        return publicDirURL + "/" + playpenDir + "/" + logicalFileName;
    }
