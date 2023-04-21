    private void writeRead(TigrAssemblerPlacedRead read) throws Exception {
        writeToOutputStream("<Seq Id=\"" + getOutputFileReadId(read) + "\">\n");
        Map<String, String> readAttributes = read.getAttributes();
        String gapIndexes = getGapIndexString(read.getEncodedGlyphs());
        writeToOutputStream("<Gaps>" + gapIndexes + "</Gaps>\n");
        String leftSeqrange = readAttributes.get("seq_lend");
        String rightSeqrange = readAttributes.get("seq_rend");
        writeToOutputStream("<Seqrange Left=\"" + leftSeqrange + "\" Right=\"" + rightSeqrange + "\"/>\n");
        String leftAsmrange = readAttributes.get("asm_lend");
        String rightAsmrange = readAttributes.get("asm_rend");
        writeToOutputStream("<Asmrange Left=\"" + leftAsmrange + "\" Right=\"" + rightAsmrange + "\"/>\n");
        String offset = readAttributes.get("offset");
        writeToOutputStream("<Offset>" + offset + "</Offset>\n");
        writeToOutputStream("</Seq>\n");
    }
