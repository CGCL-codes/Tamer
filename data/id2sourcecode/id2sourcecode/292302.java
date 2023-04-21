    void buildIncludeRegion(Vector region) throws IOException {
        QuotedStringTokenizer pst = new QuotedStringTokenizer(params);
        if (!pst.hasMoreTokens()) throw new IOException("Missing filename in INCLUDE");
        String file_name = pst.nextToken();
        LineNumberReader old_in = in;
        in = new LineNumberReader(new FileReader(file_name));
        String inLine;
        for (inLine = readLine(); inLine != null; inLine = readLine()) {
            if (isTemplateLine(inLine)) {
                region.addElement(buildTemplateRegion(inLine));
            } else {
                if (DEBUG) System.out.println("adding line to region :" + inLine);
                region.addElement(inLine);
            }
        }
        in = old_in;
    }
