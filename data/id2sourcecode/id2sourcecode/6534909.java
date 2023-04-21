    public Simulator(String[] args) {
        processArgs(args);
        System.out.println("Generating reads");
        generateFragments();
        System.out.println("Writing files");
        File tagFile = new File(resultsDirectory, "chIPPeaks.bed");
        IO.writeArrayList(tags, tagFile);
        File reads = new File(resultsDirectory, "reads.txt");
        IO.writeArrayList(seqReads, reads);
        File lineFile = new File(resultsDirectory, "frags.xls");
        IO.writeArrayList(lines, lineFile);
    }
