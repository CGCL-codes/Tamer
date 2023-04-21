    public boolean createBatchFile(Reader reader, Writer writer, Hashtable variables) {
        if ((reader == null) || (writer == null) || (variables == null)) {
            return false;
        }
        try {
            BufferedReader in = new BufferedReader(reader);
            StreamTokenizer tin = new StreamTokenizer(in);
            tin.eolIsSignificant(false);
            final int quoteChar = (int) quotingCharacter;
            tin.resetSyntax();
            tin.wordChars(' ', 255);
            tin.whitespaceChars(0, ' ');
            tin.quoteChar(quoteChar);
            tin.eolIsSignificant(true);
            int type;
            String outString = "";
            String eol = System.getProperty("line.separator");
            boolean quoted = false;
            boolean newLine = true;
            String space = " ";
            String noSpace = new String();
            String usedSpace = null;
            String variable;
            while ((type = tin.nextToken()) != StreamTokenizer.TT_EOF) {
                if (quoted || newLine) {
                    usedSpace = noSpace;
                    quoted = false;
                    newLine = false;
                } else {
                    usedSpace = space;
                }
                outString = "";
                switch(type) {
                    case StreamTokenizer.TT_NUMBER:
                        writer.write(usedSpace);
                        writer.write(Double.toString(tin.nval));
                        break;
                    case StreamTokenizer.TT_WORD:
                        writer.write(usedSpace);
                        writer.write(tin.sval);
                        break;
                    case StreamTokenizer.TT_EOL:
                        writer.write(eol);
                        newLine = true;
                        break;
                }
                if (type == quoteChar) {
                    variable = outString = tin.sval;
                    quoted = true;
                    if (outString.lastIndexOf("\n") == -1) {
                        outString = "" + (String) variables.get(variable);
                    }
                    if (outString == null) {
                        logger.error("Could not generate new batch file, the user variable '" + "' " + variable + " is not defined.");
                        return false;
                    }
                    writer.write(outString);
                }
            }
            reader.close();
            writer.close();
        } catch (Exception e) {
            logger.error("Could not generate new batch file...");
            return false;
        }
        return true;
    }
