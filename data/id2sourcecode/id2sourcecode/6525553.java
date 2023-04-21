    public void copyContent(Writer w) throws IOException, XmlException {
        if (curEvent != STAG) throw new IllegalXmlParserStateException("Must be on STAG");
        depth++;
        int depth2 = depth;
        do {
            if (depth > depth2) {
                int c = scanner.copyUntil(w, '<');
                if (c != '<') throw new XmlException("EOF in copyContent");
                curEvent = scanner.peekEvent();
            }
            switch(curEvent) {
                case ETAG:
                    depth--;
                    fakeETAG = false;
                    if (depth > depth2) {
                        scanner.copyUntil(w, '>');
                        w.write((char) scanner.read());
                    }
                    break;
                case STAG:
                    Writer w2 = (depth > depth2) ? w : NullWriter.getInstance();
                    boolean open = copyStartTag(w2);
                    if (open) {
                        depth++;
                    } else {
                        curEvent = ETAG;
                        fakeETAG = true;
                    }
                    break;
                case COMMENT:
                    reader.copyUntil(w, XmlTags.COMMENT_END);
                    w.write(XmlTags.COMMENT_END);
                    break;
                case CDSECT:
                    w.write(XmlTags.CDATA_BEGIN);
                    reader.CDSect(w);
                    w.write(XmlTags.CDATA_END);
                    break;
                default:
                    w.write(scanner.read());
                    break;
            }
        } while (depth > depth2);
        if (fakeETAG && skipWS) {
            reader.S();
        }
        returnLast = true;
    }
