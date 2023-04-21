    public void testGlobalPartition() throws Exception {
        String[] values = { "val1", "val2", "val3", "val2", "val1" };
        GrammarCache grammarCache = new GrammarCache(EmptySchema.getEXISchema(), GrammarOptions.DEFAULT_OPTIONS);
        Transmogrifier encoder = new Transmogrifier();
        EXIDecoder decoder = new EXIDecoder(999);
        Scanner scanner;
        InputSource inputSource;
        encoder.setAlignmentType(AlignmentType.bitPacked);
        encoder.setValuePartitionCapacity(2);
        encoder.setOutputOptions(HeaderOptionsOutputType.lessSchemaId);
        encoder.setEXISchema(grammarCache);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        encoder.setOutputStream(baos);
        URL url = resolveSystemIdAsURL("/interop/datatypes/string/indexed-05.xml");
        inputSource = new InputSource(url.toString());
        inputSource.setByteStream(url.openStream());
        byte[] bts;
        int n_texts;
        encoder.encode(inputSource);
        bts = baos.toByteArray();
        decoder.setEXISchema(grammarCache);
        decoder.setInputStream(new ByteArrayInputStream(bts));
        scanner = decoder.processHeader();
        Assert.assertEquals(2, scanner.getHeaderOptions().getValuePartitionCapacity());
        ArrayList<EXIEvent> exiEventList = new ArrayList<EXIEvent>();
        EXIEvent exiEvent;
        String stringValue = null;
        n_texts = 0;
        while ((exiEvent = scanner.nextEvent()) != null) {
            if (exiEvent.getEventVariety() == EXIEvent.EVENT_CH) {
                stringValue = exiEvent.getCharacters().makeString();
                Assert.assertEquals(values[n_texts], stringValue);
                ++n_texts;
            }
            exiEventList.add(exiEvent);
        }
    }
