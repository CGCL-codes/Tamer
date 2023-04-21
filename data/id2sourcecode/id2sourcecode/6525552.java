    private boolean copyStartTag(Writer w) throws IOException, XmlException {
        int c;
        do {
            c = scanner.copyUntil(w, '>', '/');
            w.write(c);
            scanner.read();
        } while (c == '/' && scanner.peek() != '>');
        if (c != '/') {
            return true;
        }
        w.write(scanner.read());
        return false;
    }
