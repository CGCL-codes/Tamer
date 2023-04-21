    public void copyText(Writer w) throws IOException, XmlException {
        if (curEvent == XmlEvent.STAG || curEvent == XmlEvent.ETAG) {
            next(XmlEvent.ALL_EVENTS);
            curEvent = scanner.peekEvent();
        }
        while (true) {
            switch(curEvent) {
                case XmlEvent.CHARDATA:
                    reader.CharData(w);
                    break;
                case XmlEvent.CDSECT:
                    reader.CDSect(w);
                    break;
                case XmlEvent.REFERENCE:
                    Entity entity = reader.Reference();
                    if (entity == null) w.write((char) scanner.read()); else w.write(entity.resolveAll(reader));
                    break;
                default:
                    returnLast = true;
                    return;
            }
            curEvent = scanner.peekEvent();
        }
    }
