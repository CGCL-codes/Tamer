    public static double[] getValues(final PeakListBinaryType peakListBinaryType) throws JAXBException, XMLStreamException {
        final StringWriter writer = new StringWriter();
        getMarshaller().marshal(new JAXBElement<PeakListBinaryType.Data>(new QName("", "peakListBinaryType"), PeakListBinaryType.Data.class, peakListBinaryType.getData()), writer);
        final XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(writer.toString()));
        final StringBuffer characters = new StringBuffer();
        int event = XMLStreamConstants.START_DOCUMENT;
        while ((event = reader.next()) != XMLStreamConstants.END_DOCUMENT) {
            switch(event) {
                case XMLStreamConstants.CHARACTERS:
                    {
                        characters.append(reader.getText());
                        break;
                    }
                default:
                    {
                        continue;
                    }
            }
        }
        return MathUtils.decode(characters.toString().getBytes(Charset.defaultCharset()), !peakListBinaryType.getData().getEndian().equals(org.mcisb.massspectrometry.PropertyNames.LITTLE), !peakListBinaryType.getData().getPrecision().equals(org.mcisb.massspectrometry.PropertyNames.PRECISION_FLOAT));
    }
