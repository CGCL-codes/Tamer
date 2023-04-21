    @Override
    protected void doToXml(IXmlWriter writer) {
        if (subjects != null) {
            for (XmlLangText subject : subjects) {
                writer.writeStartElement("subject");
                if (subject.getXmlLang() != null) {
                    writer.writeAttribute("xml:lang", subject.getXmlLang());
                }
                if (subject.getText() != null) {
                    writer.writeCharacters(subject.getText());
                }
                writer.writeEndElement();
            }
        }
        if (bodies != null) {
            for (XmlLangText body : bodies) {
                writer.writeStartElement("body");
                if (body.getXmlLang() != null) {
                    writer.writeAttribute("xml:lang", body.getXmlLang());
                }
                if (body.getText() != null) {
                    writer.writeCharacters(body.getText());
                }
                writer.writeEndElement();
            }
        }
        if (thread != null) {
            writer.writeStartElement("thread");
            writer.writeCharacters(thread);
            writer.writeEndElement();
        }
    }
