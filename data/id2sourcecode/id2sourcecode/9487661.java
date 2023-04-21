    void writeSymbol(String sym, Consumer out, boolean readable) {
        if (readable && !r5rsIdentifierMinusInteriorColons.matcher(sym).matches()) {
            int len = sym.length();
            if (len == 0) {
                write("||", out);
            } else {
                boolean inVerticalBars = false;
                for (int i = 0; i < len; i++) {
                    char ch = sym.charAt(i);
                    if (ch == '|') {
                        write(inVerticalBars ? "|\\" : "\\", out);
                        inVerticalBars = false;
                    } else if (!inVerticalBars) {
                        out.write('|');
                        inVerticalBars = true;
                    }
                    out.write(ch);
                }
                if (inVerticalBars) out.write('|');
            }
            return;
        }
        write(sym, out);
    }
