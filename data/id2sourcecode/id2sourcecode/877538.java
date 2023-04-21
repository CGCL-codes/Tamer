    private static void roundTrip(Reader reader, Writer writer, String indent) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser pp = factory.newPullParser();
        pp.setInput(reader);
        XmlSerializer serializer = factory.newSerializer();
        serializer.setOutput(writer);
        if (indent != null) {
            serializer.setProperty(PROPERTY_SERIALIZER_INDENTATION, indent);
        }
        (new Roundtrip(pp, serializer)).roundTrip();
    }
