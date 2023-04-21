    public OperationProcessor(BufferedReader inputfilereader, BufferedWriter outputfilewriter, BufferedWriter errorfilewriter, BufferedWriter rejectedlineswriter, Vector<Operation> ops, int sourcecolnb, int targetcolnb, boolean verbose) {
        this.inputfilereader = inputfilereader;
        this.outputfilewriter = outputfilewriter;
        this.errorfilewriter = errorfilewriter;
        this.rejectedlineswriter = rejectedlineswriter;
        this.sourcecolnb = sourcecolnb;
        this.targetcolnb = targetcolnb;
        this.ops = ops;
        this.verbose = verbose;
    }
