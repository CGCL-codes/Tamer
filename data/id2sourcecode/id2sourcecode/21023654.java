    public void testHeaderOptionsAlignment_01() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/optionsSchema.xsd", getClass(), m_compilerErrors);
        Assert.assertEquals(0, m_compilerErrors.getTotalCount());
        GrammarCache grammarCache = new GrammarCache(corpus, GrammarOptions.DEFAULT_OPTIONS);
        String[] exiFiles = { "/encoding/headerOptions-01.bitPacked", "/encoding/headerOptions-01.byteAligned", "/encoding/headerOptions-01.preCompress", "/encoding/headerOptions-01.compress" };
        AlignmentType[] alignments = { AlignmentType.bitPacked, AlignmentType.byteAligned, AlignmentType.preCompress, AlignmentType.compress };
        for (int i = 0; i < alignments.length; i++) {
            EXIDecoder decoder = new EXIDecoder();
            Scanner scanner;
            URL url = resolveSystemIdAsURL(exiFiles[i]);
            int n_events;
            final AlignmentType falseAlignmentType;
            falseAlignmentType = alignments[i] == AlignmentType.compress ? AlignmentType.bitPacked : AlignmentType.compress;
            decoder.setAlignmentType(falseAlignmentType);
            decoder.setEXISchema(grammarCache);
            decoder.setInputStream(url.openStream());
            scanner = decoder.processHeader();
            Assert.assertEquals(alignments[i], scanner.getAlignmentType());
            ArrayList<EXIEvent> exiEventList = new ArrayList<EXIEvent>();
            EXIEvent exiEvent;
            n_events = 0;
            while ((exiEvent = scanner.nextEvent()) != null) {
                ++n_events;
                exiEventList.add(exiEvent);
            }
            Assert.assertEquals(6, n_events);
            EventType eventType;
            exiEvent = exiEventList.get(0);
            Assert.assertEquals(EXIEvent.EVENT_SD, exiEvent.getEventVariety());
            exiEvent = exiEventList.get(1);
            Assert.assertEquals(EXIEvent.EVENT_SE, exiEvent.getEventVariety());
            Assert.assertEquals("header", exiEvent.getName());
            Assert.assertEquals(ExiUriConst.W3C_2009_EXI_URI, exiEvent.getURI());
            exiEvent = exiEventList.get(2);
            Assert.assertEquals(EXIEvent.EVENT_SE, exiEvent.getEventVariety());
            Assert.assertEquals("strict", exiEvent.getName());
            Assert.assertEquals(ExiUriConst.W3C_2009_EXI_URI, exiEvent.getURI());
            exiEvent = exiEventList.get(3);
            Assert.assertEquals(EXIEvent.EVENT_EE, exiEvent.getEventVariety());
            exiEvent = exiEventList.get(4);
            Assert.assertEquals(EXIEvent.EVENT_EE, exiEvent.getEventVariety());
            exiEvent = exiEventList.get(5);
            Assert.assertEquals(EXIEvent.EVENT_ED, exiEvent.getEventVariety());
            eventType = exiEvent.getEventType();
            Assert.assertSame(exiEvent, eventType);
            Assert.assertEquals(0, eventType.getIndex());
        }
    }
