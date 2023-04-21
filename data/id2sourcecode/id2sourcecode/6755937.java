    private void dumpTuple(Gene g, Transcript tr, Translation tl, String peptide, PrintWriter geneOut, PrintWriter peptideOut) {
        byte[] checksumBytes;
        try {
            checksumBytes = peptide.getBytes("ISO-8859-1");
        } catch (Exception e) {
            throw new RuntimeException("ISO-8859-1 not supported, should be!");
        }
        md.update(checksumBytes);
        byte[] checksum = md.digest();
        md.reset();
        geneOut.println(g.getAccessionID() + "\t" + g.getVersion() + "\t" + tr.getAccessionID() + "\t" + tr.getVersion() + "\t" + tl.getAccessionID() + "\t" + tl.getVersion() + "\t" + currentPeptideId + "\t" + mappingSession);
        peptideOut.println(currentPeptideId + "\t" + hexDumpBytes(checksum) + "\t" + peptide);
        currentPeptideId++;
    }
