    public void appendToFile(String pFile, String pAppend) throws Exception {
        FileUtil.writeFile(iDependsFolder + pFile, FileUtil.readFile(iDependsFolder + pFile) + pAppend);
    }
