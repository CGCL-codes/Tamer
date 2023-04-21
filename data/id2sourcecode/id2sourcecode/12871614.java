    private boolean parse(XMLEventReader reader, PrintWriter writer, boolean bComma) throws XMLStreamException, CoreException, UnsupportedEncodingException, IOException, TransformException, SaxonApiException {
        mLevel++;
        while (reader.hasNext()) {
            XMLEvent e = reader.nextEvent();
            if (e.isStartElement()) {
                StartElement start = e.asStartElement();
                QName name = start.getName();
                if (name.equals(kELEM_XJSON)) {
                    if (mLevel != 1) throw new UnexpectedException("XJSON element must be at document root");
                    mLevel = 0;
                    while (parse(reader, writer, bComma)) ;
                    return false;
                } else if (name.equals(kELEM_FILE)) {
                    if (!writeFile(start, reader, writer)) return false;
                } else if (bComma) writer.print(",");
                if (name.equals(kELEM_OBJECT)) writeObject(start, reader, writer); else if (name.equals(kELEM_ARRAY)) writeArray(start, reader, writer); else if (name.equals(kELEM_MEMBER)) writeMember(start, reader, writer); else if (name.equals(kELEM_NUMBER)) writeNumber(start, reader, writer); else if (name.equals(kELEM_BOOLEAN)) writeBoolean(start, reader, writer); else if (name.equals(kELEM_NULL)) writeNull(reader, writer); else if (name.equals(kELEM_STRING)) writeString(start, reader, writer); else readToEnd(reader);
                mLevel--;
                return true;
            } else if (e.isEndElement()) {
                mLevel--;
                return false;
            }
        }
        mLevel--;
        return false;
    }
