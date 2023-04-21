    private void write(XMLEventReader xmlreader, XMLEvent first, List<Namespace> ns, PrintWriter listWriter) throws XMLStreamException, IOException {
        File fout = nextFile();
        OutputStream fo = new FileOutputStream(fout);
        XMLEventWriter w = mOutputFactory.createXMLEventWriter(fo);
        for (XMLEvent e : mHeader) w.add(e);
        w.add(first);
        if (ns != null) for (Namespace n : ns) w.add(n);
        int depth = 0;
        int nchild = 0;
        while (xmlreader.hasNext()) {
            XMLEvent e = xmlreader.nextEvent();
            w.add(e);
            if (e.getEventType() == XMLStreamConstants.START_ELEMENT) depth++; else if (e.getEventType() == XMLStreamConstants.END_ELEMENT) {
                if (depth-- <= 0) {
                    if (++nchild == mNumChildren) break;
                }
            }
        }
        w.add(mEventFactory.createEndDocument());
        w.flush();
        w.close();
        fo.close();
        if (listWriter != null) {
            listWriter.println(fout.getName());
            listWriter.flush();
        }
    }
