    private void emitSourceSection(SourceWriter out, Doc next, LineNumberReader in) throws IOException {
        String s;
        if (next != null) {
            int l = next.getLine();
            int i = in.getLineNumber();
            for (; i < l; i++) {
                if ((s = in.readLine()) == null) throw new IOException("Encoutered unexpected end of file");
                out.writeCodeLine(s);
            }
            if (next.isFromComment()) {
                l = next.getEndLine() + 1;
                for (; i < l; i++) {
                    if ((s = in.readLine()) == null) throw new IOException("Encoutered unexpected end of file");
                }
            }
        } else {
            while ((s = in.readLine()) != null) out.writeCodeLine(s);
        }
    }
