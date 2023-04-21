    public void addProlog(final String encoding, final boolean standalone, final String doctypeDecl) {
        if (doIndent || openStartTag || indentLevel > 0 || elementStack.size() > 0) throw new IllegalStateException("can not write prolog if already content exists");
        out.print("<?xml version='1.0");
        if (encoding != null && encoding.length() > 0) {
            out.print("' encoding='");
            out.print(encoding);
        }
        if (!standalone) out.print("' standalone='no");
        out.println("'?>");
        if (doctypeDecl != null && doctypeDecl.length() > 0) out.println(doctypeDecl);
        out.println();
    }
