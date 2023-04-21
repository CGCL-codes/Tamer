    public void writeObjectRaw(Object obj, Consumer out) {
        if (obj instanceof Boolean) writeBoolean(((Boolean) obj).booleanValue(), out); else if (obj instanceof Char) write(((Char) obj).intValue(), out); else if (obj instanceof Character) write(((Character) obj).charValue(), out); else if (obj instanceof Symbol) {
            Symbol sym = (Symbol) obj;
            if (sym.getNamespace() == XmlNamespace.HTML) {
                write("html:", out);
                write(sym.getLocalPart(), out);
            } else writeSymbol(sym, out, readable);
        } else if (obj instanceof java.net.URI && getReadableOutput() && out instanceof PrintWriter) {
            write("#,(URI ", out);
            Strings.printQuoted(obj.toString(), (PrintWriter) out, 1);
            out.write(')');
        } else if (obj instanceof CharSequence) {
            CharSequence str = (CharSequence) obj;
            if (getReadableOutput() && out instanceof PrintWriter) Strings.printQuoted(str, (PrintWriter) out, 1); else if (obj instanceof String) {
                out.write((String) obj);
            } else if (obj instanceof CharSeq) {
                CharSeq seq = (CharSeq) obj;
                seq.consume(0, seq.size(), out);
            } else {
                int len = str.length();
                for (int i = 0; i < len; i++) out.write(str.charAt(i));
            }
        } else if (obj instanceof LList && out instanceof OutPort) writeList((LList) obj, (OutPort) out); else if (obj instanceof SimpleVector) {
            SimpleVector vec = (SimpleVector) obj;
            String tag = vec.getTag();
            String start, end;
            if (language == 'E') {
                start = "[";
                end = "]";
            } else {
                start = tag == null ? "#(" : ("#" + tag + "(");
                end = ")";
            }
            if (out instanceof OutPort) ((OutPort) out).startLogicalBlock(start, false, end); else write(start, out);
            int endpos = vec.size() << 1;
            for (int ipos = 0; ipos < endpos; ipos += 2) {
                if (ipos > 0 && out instanceof OutPort) ((OutPort) out).writeSpaceFill();
                if (!vec.consumeNext(ipos, out)) break;
            }
            if (out instanceof OutPort) ((OutPort) out).endLogicalBlock(end); else write(end, out);
        } else if (obj instanceof Array) {
            write((Array) obj, 0, 0, out);
        } else if (obj instanceof Consumable) ((Consumable) obj).consume(out); else if (obj instanceof Printable) ((Printable) obj).print(out); else if (obj instanceof RatNum) {
            int b = 10;
            boolean showRadix = false;
            Object base = outBase.get(null);
            Object printRadix = outRadix.get(null);
            if (printRadix != null && (printRadix == Boolean.TRUE || "yes".equals(printRadix.toString()))) showRadix = true;
            if (base instanceof Number) b = ((IntNum) base).intValue(); else if (base != null) b = Integer.parseInt(base.toString());
            String asString = ((RatNum) obj).toString(b);
            if (showRadix) {
                if (b == 16) write("#x", out); else if (b == 8) write("#o", out); else if (b == 2) write("#b", out); else if (b != 10 || !(obj instanceof IntNum)) write("#" + base + "r", out);
            }
            write(asString, out);
            if (showRadix && b == 10 && obj instanceof IntNum) write(".", out);
        } else {
            String asString;
            if (obj == null) asString = null; else {
                Class cl = obj.getClass();
                if (cl.isArray()) {
                    int len = java.lang.reflect.Array.getLength(obj);
                    if (out instanceof OutPort) ((OutPort) out).startLogicalBlock("[", false, "]"); else write("[", out);
                    for (int i = 0; i < len; i++) {
                        if (i > 0) {
                            write(" ", out);
                            if (out instanceof OutPort) ((OutPort) out).writeBreakFill();
                        }
                        writeObject(java.lang.reflect.Array.get(obj, i), out);
                    }
                    if (out instanceof OutPort) ((OutPort) out).endLogicalBlock("]"); else write("]", out);
                    return;
                }
                asString = obj.toString();
            }
            if (asString == null) write("#!null", out); else write(asString, out);
        }
    }
