    private void writeContig(TigrAssemblerContig contig) throws Exception {
        writeToOutputStream("<Contig Id=\"RESASM_" + contig.getId() + "\">\n");
        Map<String, String> contigAttributes = contig.getAttributes();
        String consensus = contigAttributes.get("lsequence");
        writeToOutputStream("<Nuc>" + consensus + "</Nuc>\n");
        String qualities = getQualityElementString(contigAttributes.get("quality"));
        writeToOutputStream("<Qualclass>" + qualities + "</Qualclass>\n");
        for (TigrAssemblerPlacedRead read : contig.getPlacedReads()) {
            writeRead(read);
        }
        writeToOutputStream("</Contig>\n");
    }
