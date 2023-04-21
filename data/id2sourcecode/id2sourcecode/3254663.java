    private ReturnValue recursivelyCopyDir(String baseDir, String[] files, String outputDir, ArrayList<FileMetadata> fileArray) {
        ReturnValue ret = new ReturnValue(ReturnValue.SUCCESS);
        boolean skipIfMissing = options.has("skip-if-missing");
        for (String file : files) {
            if (baseDir.endsWith("/")) {
                baseDir = baseDir.substring(0, baseDir.length() - 1);
            }
            if (outputDir.endsWith("/")) {
                outputDir = outputDir.substring(0, outputDir.length() - 1);
            }
            File currFile = new File(baseDir + "/" + file);
            String additionalPath = currFile.getAbsolutePath().replace(baseDir + "/", "");
            if (currFile.isDirectory()) {
                ReturnValue currRet = recursivelyCopyDir(currFile.getAbsolutePath(), currFile.list(), outputDir + "/" + additionalPath, fileArray);
                if (currRet.getExitStatus() != ReturnValue.SUCCESS) {
                    return (currRet);
                }
            } else {
                System.out.println("\n  COPYING FILE: " + currFile.getAbsolutePath() + "\n    to " + outputDir + "/" + additionalPath);
                if (!provisionFile(currFile.getAbsolutePath(), outputDir + "/" + additionalPath.substring(0, additionalPath.length() - file.length()), skipIfMissing, fileArray)) {
                    ret.setExitStatus(ReturnValue.FAILURE);
                    return (ret);
                }
            }
        }
        return (ret);
    }
