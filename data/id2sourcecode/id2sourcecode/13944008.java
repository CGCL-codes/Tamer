    @Override
    public void run() throws Exception {
        Pattern startScanPattern = Pattern.compile("<scan num=\"(\\d+)\"");
        Pattern endMsRunPattern = Pattern.compile("</msRun>");
        RandomAccessFile rin = new RandomAccessFile(params.getMzXMLFile(), "rw");
        File outFile = new File(params.getOutputFileName());
        if (params.isVerbose()) {
            System.out.println("Processing bytes...");
        }
        TerminalProgressBar progressBar = null;
        if (params.isVerbose()) {
            progressBar = TerminalProgressBar.newInstance(0, (int) rin.length());
        }
        if (outFile.exists()) {
            outFile.delete();
        }
        RandomAccessFile rout = new RandomAccessFile(outFile, "rw");
        FileChannel channel = rin.getChannel();
        String line = null;
        Map<Integer, Long> offsets = new HashMap<Integer, Long>();
        long lastLineOffset = 0;
        while ((line = rin.readLine()) != null) {
            Matcher startScanMatcher = startScanPattern.matcher(line);
            Matcher endMsRunMatcher = endMsRunPattern.matcher(line);
            if (startScanMatcher.find()) {
                offsets.put(Integer.parseInt(startScanMatcher.group(1)), lastLineOffset);
            } else if (endMsRunMatcher.find()) {
                break;
            }
            lastLineOffset = rin.getFilePointer();
            if (params.isVerbose()) {
                progressBar.setValue((int) lastLineOffset);
            }
        }
        long indexOffset = rin.getFilePointer();
        if (params.isVerbose()) {
            progressBar.setValue((int) indexOffset);
        }
        channel.transferTo(0, indexOffset, rout.getChannel());
        byte[] indexBytes = makeScanIndices(offsets, indexOffset);
        rout.getChannel().write(ByteBuffer.wrap(indexBytes), indexOffset);
        long sha1Offset = indexOffset + indexBytes.length;
        String sha1 = "29688c4392a9a81c83e0d18b5779d6aaf6089d3f";
        StringBuilder sb = new StringBuilder(sha1);
        sb.append("</sha1>\n</mzXML>\n");
        rout.getChannel().write(ByteBuffer.wrap(sb.toString().getBytes()), sha1Offset);
        if (params.isVerbose()) {
            progressBar.setValue((int) rin.length());
        }
    }
