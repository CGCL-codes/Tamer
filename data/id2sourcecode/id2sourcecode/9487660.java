    void writeSymbol(Symbol sym, Consumer out, boolean readable) {
        String prefix = sym.getPrefix();
        Namespace namespace = sym.getNamespace();
        String uri = namespace == null ? null : namespace.getName();
        boolean suffixColon = false;
        if (namespace == Keyword.keywordNamespace) {
            if (language == 'C' || language == 'E') out.write(':'); else suffixColon = true;
        } else if (prefix != null && prefix.length() > 0) {
            writeSymbol(prefix, out, readable);
            out.write(':');
        } else if (uri != null && uri.length() > 0) {
            out.write('{');
            out.write(uri);
            out.write('}');
        }
        writeSymbol(sym.getName(), out, readable);
        if (suffixColon) out.write(':');
    }
