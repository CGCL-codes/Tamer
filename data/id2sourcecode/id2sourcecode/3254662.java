    @Override
    public ReturnValue do_run() {
        ReturnValue ret = new ReturnValue();
        ret.setExitStatus(ReturnValue.SUCCESS);
        boolean skipIfMissing = options.has("skip-if-missing");
        boolean verbose = options.has("verbose");
        filesUtil.setVerbose(verbose);
        ret.setAlgorithm(algorithmName);
        ArrayList<FileMetadata> fileArray = ret.getFiles();
        ArrayList<String> newArray = new ArrayList<String>();
        List<String> inputs = (List<String>) options.valuesOf("input-file");
        List<String> metaInputs = (List<String>) options.valuesOf("input-file-metadata");
        if (metaInputs != null) {
            if (inputs != null && inputs.size() > 0) {
                newArray.addAll(inputs);
            }
            for (String input : metaInputs) {
                String[] tokens = input.split("::");
                if (tokens.length == 3) {
                    newArray.add(tokens[2]);
                    FileMetadata fmd = new FileMetadata();
                    fmd.setDescription(tokens[0]);
                    fmd.setMetaType(tokens[1]);
                    fmd.setFilePath(tokens[2]);
                    fmd.setType(tokens[0]);
                    fileArray.add(fmd);
                }
            }
            inputs = newArray;
        }
        for (String input : inputs) {
            System.err.println("PROCESSING INPUT: " + input);
            this.size = 0;
            this.position = 0;
            this.fileName = "";
            if (!input.startsWith("http") && !input.startsWith("s3") && new File(input).isDirectory()) {
                if (options.has("recursive")) {
                    ReturnValue currRet = recursivelyCopyDir(new File(input).getAbsolutePath(), new File(input).list(), (String) options.valueOf("output-dir"), fileArray);
                    if (currRet.getExitStatus() != ReturnValue.SUCCESS) {
                        return (currRet);
                    }
                }
            } else if (!provisionFile(input, (String) options.valueOf("output-dir"), skipIfMissing, fileArray)) {
                ret.setExitStatus(ReturnValue.FAILURE);
                return (ret);
            }
        }
        for (FileMetadata fmd : fileArray) {
            System.err.println("FMD:\nDescription: " + fmd.getDescription() + "\nFile Path: " + fmd.getFilePath() + "\nMeta Type: " + fmd.getMetaType() + "\nType: " + fmd.getType());
        }
        return (ret);
    }
