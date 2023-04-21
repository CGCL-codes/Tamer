    public static void parseFastaFile(ONDEXGraph graph, String fileName, WriteFastaFile write) throws Exception {
        BufferedReader input = null;
        File file = new File(fileName);
        if (!file.isFile() || !file.exists()) {
            ONDEXEventHandler.getEventHandlerForSID(graph.getSID()).fireEventOccurred(new DataFileMissingEvent("FASTA file missing :" + file, "parseFastaFile(ONDEXGraph graph, String fileName, WriteFastaFile write)"));
            return;
        } else if (!file.canRead()) {
            ONDEXEventHandler.getEventHandlerForSID(graph.getSID()).fireEventOccurred(new DataFileMissingEvent("FASTA file has no read permissions :" + file, "parseFastaFile(ONDEXGraph graph, String fileName, WriteFastaFile write)"));
            return;
        }
        if (fileName.endsWith(".gz")) {
            GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(fileName));
            input = new BufferedReader(new InputStreamReader(gzip));
        } else {
            input = new BufferedReader(new FileReader(fileName));
        }
        String inputLine = input.readLine();
        FastaBlock currentFastaSeq = null;
        while (inputLine != null) {
            if (inputLine.charAt(0) == '>') {
                if (currentFastaSeq != null) {
                    write.parseFastaBlock(graph, currentFastaSeq);
                }
                currentFastaSeq = new FastaBlock();
                currentFastaSeq.setHeader(inputLine.substring(1));
            } else if (inputLine.length() >= 1) {
                currentFastaSeq.addSequence(inputLine);
            }
            inputLine = input.readLine();
        }
        if (currentFastaSeq != null) {
            write.parseFastaBlock(graph, currentFastaSeq);
        }
    }
