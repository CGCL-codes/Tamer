    public int addPatterns() throws IOException {
        if (paintList.size() > 0) {
            PDFDictionary patterns = pdf.openDictionary("Pattern");
            ListIterator i = paintList.listIterator();
            while (i.hasNext()) {
                Entry e = (Entry) i.next();
                patterns.entry(e.name, pdf.ref(e.name));
            }
            pdf.close(patterns);
        }
        return paintList.size();
    }
