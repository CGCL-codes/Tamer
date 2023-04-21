    public static void save(File file) throws IOException, XMLStreamException, TransformerConfigurationException, TransformerException {
        ArrayList<String> channels = MasterChannels.getInstance().getChannels();
        ArrayList<Stream> streams = MasterChannels.getInstance().getStreams();
        StringWriter writer = new StringWriter();
        XMLStreamWriter xml = javax.xml.stream.XMLOutputFactory.newFactory().createXMLStreamWriter(writer);
        xml.writeStartDocument();
        xml.writeStartElement("WebcamStudio");
        xml.writeStartElement("Channels");
        for (String c : channels) {
            xml.writeStartElement("Channel");
            xml.writeAttribute("name", c);
            xml.writeEndElement();
        }
        xml.writeEndElement();
        xml.writeStartElement("Sources");
        for (Stream s : streams) {
            xml.writeStartElement("Source");
            xml.writeAttribute("id", s.getID());
            xml.writeAttribute("name", s.getName());
            if (s.getFile() != null) {
                xml.writeAttribute("file", s.getFile().getAbsolutePath());
            }
            if (s.getURL() != null) {
                xml.writeAttribute("url", s.getURL());
            }
            xml.writeAttribute("clazz", s.getClass().getCanonicalName());
            xml.writeStartElement("Format");
            xml.writeAttribute("x", s.getX() + "");
            xml.writeAttribute("y", s.getY() + "");
            xml.writeAttribute("captureWidth", s.getCaptureWidth() + "");
            xml.writeAttribute("captureHeight", s.getCaptureHeight() + "");
            xml.writeAttribute("width", s.getWidth() + "");
            xml.writeAttribute("height", s.getHeight() + "");
            xml.writeAttribute("zorder", s.getZOrder() + "");
            xml.writeAttribute("opacity", s.getOpacity() + "");
            xml.writeAttribute("volume", s.getVolume() + "");
            if (s instanceof SourceText) {
                SourceText t = (SourceText) s;
                xml.writeStartElement("content");
                xml.writeAttribute("font", t.getFont());
                xml.writeAttribute("color", t.getColor() + "");
                xml.writeAttribute("bgColor", t.getBackgroundColor() + "");
                xml.writeAttribute("bgOpacity", t.getBackgroundOpacity() + "");
                xml.writeCData(t.getContent());
                xml.writeEndElement();
            }
            if (s instanceof SourceDesktop) {
                SourceDesktop d = (SourceDesktop) s;
                xml.writeStartElement("desktop");
                xml.writeAttribute("x", d.getDesktopX() + "");
                xml.writeAttribute("y", d.getDesktopY() + "");
                xml.writeAttribute("w", d.getDesktopW() + "");
                xml.writeAttribute("h", d.getDesktopH() + "");
                xml.writeEndElement();
            }
            xml.writeEndElement();
            xml.writeEndElement();
        }
        xml.writeEndElement();
        xml.writeStartElement("Mixer");
        xml.writeAttribute("width", MasterMixer.getInstance().getWidth() + "");
        xml.writeAttribute("height", MasterMixer.getInstance().getHeight() + "");
        xml.writeAttribute("rate", MasterMixer.getInstance().getRate() + "");
        xml.writeEndElement();
        xml.writeEndElement();
        xml.writeEndDocument();
        xml.flush();
        xml.close();
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        StringWriter formattedStringWriter = new StringWriter();
        transformer.transform(new StreamSource(new StringReader(writer.getBuffer().toString())), new StreamResult(file));
    }
