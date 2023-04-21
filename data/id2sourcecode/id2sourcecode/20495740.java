    protected void writeSource() {
        if (includeSource()) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            m_source.write(bos, "N3");
            String output = bos.toString();
            writeln(1, "private static final String SOURCE = ");
            boolean first = true;
            StringTokenizer st = new StringTokenizer(output, "\n");
            while (st.hasMoreTokens()) {
                String tok = st.nextToken();
                if (tok.endsWith("\r")) {
                    tok = tok.substring(0, tok.length() - 1);
                }
                write(2, first ? "   " : " + ");
                write(0, "\"");
                write(0, protectQuotes(tok));
                writeln(2, "\\n\"");
                first = false;
            }
            writeln(1, ";");
            writeln(0, "");
            writeln(1, "/** Read the ontology definition into the source model */ ");
            writeln(1, "static { ");
            writeln(2, "MODEL.read( new ByteArrayInputStream( SOURCE.getBytes() ), null, \"N3\" );");
            writeln(1, "}");
            writeln(0, "");
        }
    }
