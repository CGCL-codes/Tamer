    public void testDecode1000BooleanStore_BitPacked() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/DataStore/DataStore.xsd", getClass(), m_compilerErrors);
        Assert.assertEquals(0, m_compilerErrors.getTotalCount());
        GrammarCache grammarCache = new GrammarCache(corpus, GrammarOptions.STRICT_OPTIONS);
        String[] booleanValues100 = { "false", "true", "false", "true", "0", "0", "1", "1", "1", "1" };
        AlignmentType alignment = AlignmentType.bitPacked;
        Scanner scanner;
        int n_texts;
        EXIDecoder decoder = new EXIDecoder(999);
        decoder.setEXISchema(grammarCache);
        decoder.setAlignmentType(alignment);
        URL url = resolveSystemIdAsURL("/DataStore/instance/1000BooleanStore.bitPacked");
        decoder.setInputStream(url.openStream());
        scanner = decoder.processHeader();
        EXIEvent exiEvent;
        n_texts = 0;
        while ((exiEvent = scanner.nextEvent()) != null) {
            if (exiEvent.getEventVariety() == EXIEvent.EVENT_CH) {
                if (++n_texts % 100 == 0) {
                    String expected = booleanValues100[(n_texts / 100) - 1];
                    String val = exiEvent.getCharacters().makeString();
                    if ("true".equals(val)) {
                        Assert.assertTrue("true".equals(expected) || "1".equals(expected));
                    } else {
                        Assert.assertEquals("false", val);
                        Assert.assertTrue("false".equals(expected) || "0".equals(expected));
                    }
                }
            }
        }
        Assert.assertEquals(1000, n_texts);
    }
