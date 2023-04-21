    public void test4BooleanStore() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/DataStore/DataStore.xsd", getClass(), m_compilerErrors);
        Assert.assertEquals(0, m_compilerErrors.getTotalCount());
        GrammarCache grammarCache = new GrammarCache(corpus, GrammarOptions.STRICT_OPTIONS);
        String[] booleanValues4 = { "true", "false", "0", "1" };
        for (AlignmentType alignment : Alignments) {
            Transmogrifier encoder = new Transmogrifier();
            EXIDecoder decoder = new EXIDecoder(999);
            Scanner scanner;
            InputSource inputSource;
            encoder.setAlignmentType(alignment);
            decoder.setAlignmentType(alignment);
            encoder.setEXISchema(grammarCache);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            encoder.setOutputStream(baos);
            URL url = resolveSystemIdAsURL("/DataStore/instance/4BooleanStore.xml");
            inputSource = new InputSource(url.toString());
            inputSource.setByteStream(url.openStream());
            byte[] bts;
            int n_texts;
            encoder.encode(inputSource);
            bts = baos.toByteArray();
            decoder.setEXISchema(grammarCache);
            decoder.setInputStream(new ByteArrayInputStream(bts));
            scanner = decoder.processHeader();
            ArrayList<EXIEvent> exiEventList = new ArrayList<EXIEvent>();
            EXIEvent exiEvent;
            n_texts = 0;
            while ((exiEvent = scanner.nextEvent()) != null) {
                if (exiEvent.getEventVariety() == EXIEvent.EVENT_CH) {
                    String expected = booleanValues4[n_texts];
                    String val = exiEvent.getCharacters().makeString();
                    if ("true".equals(val)) {
                        Assert.assertTrue("true".equals(expected) || "1".equals(expected));
                    } else {
                        Assert.assertEquals("false", val);
                        Assert.assertTrue("false".equals(expected) || "0".equals(expected));
                    }
                    ++n_texts;
                }
                exiEventList.add(exiEvent);
            }
            Assert.assertEquals(4, n_texts);
        }
    }
