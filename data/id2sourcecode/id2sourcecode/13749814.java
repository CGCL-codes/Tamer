    private String AttValue(boolean resolvePE) throws IOException, XmlException {
        int q = scanner.read();
        if (q != '"' && q != '\'') throw new XmlException("Expected AttValue quote, got " + (char) q);
        attValue.reset();
        int c;
        while (true) {
            c = scanner.peek();
            if (c == -1) throw new XmlException("EOF in AttValue");
            if (c == '>' && !resolvePE) throw new XmlException("Must not have > in AttValue");
            if (c == '&') {
                if (CharRef()) {
                    attValue.write(scanner.read());
                    continue;
                } else if (!resolvePE) {
                    Entity ent = Reference();
                    if (ent != null) {
                        if (ent.isExternal()) throw new XmlException("No external entity in att value");
                        attValue.write(ent.resolveAll(this));
                    } else {
                        attValue.write((char) scanner.read());
                    }
                    continue;
                } else {
                    checkEntityReference(attValue);
                }
            }
            if (c == '%' && resolvePE) {
                Entity e = PEReference();
                attValue.write(e.resolveAll(this));
                continue;
            }
            c = scanner.read();
            if (q == c) return attValue.toString();
            attValue.write(c);
            if (attValue.size() > XmlReaderPrefs.MAX_ATTRIBUTE_LEN) {
                throw new XmlException("Exceeded MAX_ATTRIBUTE_LEN, read to " + attValue);
            }
        }
    }
