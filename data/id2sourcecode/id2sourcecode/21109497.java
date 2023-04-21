    public String readwriteText(OutputFormat outFormat, boolean mergeAdjacentText) throws Exception {
        StringWriter out = new StringWriter();
        StringReader in = new StringReader(inputText);
        SAXReader reader = new SAXReader();
        reader.setMergeAdjacentText(mergeAdjacentText);
        Document document = reader.read(in);
        XMLWriter writer = (outFormat == null) ? new XMLWriter(out) : new XMLWriter(out, outFormat);
        writer.write(document);
        writer.close();
        String outText = out.toString();
        return outText;
    }
