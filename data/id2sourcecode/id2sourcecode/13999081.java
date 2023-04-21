    public void testNLM_strict_01() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/NLM/nlmcatalogrecord_060101.xsd", getClass(), m_compilerErrors);
        Assert.assertEquals(0, m_compilerErrors.getTotalCount());
        GrammarCache grammarCache = new GrammarCache(corpus, GrammarOptions.STRICT_OPTIONS);
        String[] exiFiles = { "/NLM/catplussamp2006.bitPacked", "/NLM/catplussamp2006.byteAligned", "/NLM/catplussamp2006.preCompress", "/NLM/catplussamp2006.compress" };
        for (int i = 0; i < Alignments.length; i++) {
            AlignmentType alignment = Alignments[i];
            EXIDecoder decoder = new EXIDecoder();
            Scanner scanner;
            decoder.setAlignmentType(alignment);
            URL url = resolveSystemIdAsURL(exiFiles[i]);
            int n_events;
            decoder.setEXISchema(grammarCache);
            decoder.setInputStream(url.openStream());
            scanner = decoder.processHeader();
            ArrayList<EXIEvent> exiEventList = new ArrayList<EXIEvent>();
            EXIEvent exiEvent;
            n_events = 0;
            while ((exiEvent = scanner.nextEvent()) != null) {
                ++n_events;
                exiEventList.add(exiEvent);
            }
            Assert.assertEquals(35176, n_events);
            exiEvent = exiEventList.get(33009);
            Assert.assertEquals("Interdisciplinary Studies", exiEvent.getCharacters().makeString());
        }
    }
