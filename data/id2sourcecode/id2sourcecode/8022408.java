    public void testBlockSize_01() throws Exception {
        GrammarCache grammarCache = new GrammarCache((EXISchema) null, GrammarOptions.DEFAULT_OPTIONS);
        for (AlignmentType alignment : new AlignmentType[] { AlignmentType.preCompress, AlignmentType.compress }) {
            Transmogrifier encoder = new Transmogrifier();
            EXIReader decoder = new EXIReader();
            encoder.setAlignmentType(alignment);
            decoder.setAlignmentType(alignment);
            encoder.setBlockSize(1);
            encoder.setEXISchema(grammarCache);
            decoder.setEXISchema(grammarCache);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            encoder.setOutputStream(baos);
            URL url = resolveSystemIdAsURL("/interop/datatypes/string/indexed-10.xml");
            InputSource inputSource = new InputSource(url.toString());
            inputSource.setByteStream(url.openStream());
            encoder.encode(inputSource);
            byte[] bts = baos.toByteArray();
            ArrayList<Event> exiEventList = new ArrayList<Event>();
            SAXRecorder saxRecorder = new SAXRecorder(exiEventList, true);
            decoder.setContentHandler(saxRecorder);
            try {
                decoder.parse(new InputSource(new ByteArrayInputStream(bts)));
            } catch (Exception e) {
                continue;
            }
            Assert.fail();
        }
        for (AlignmentType alignment : new AlignmentType[] { AlignmentType.preCompress, AlignmentType.compress }) {
            Transmogrifier encoder = new Transmogrifier();
            EXIReader decoder = new EXIReader();
            encoder.setAlignmentType(alignment);
            decoder.setAlignmentType(alignment);
            encoder.setBlockSize(1);
            decoder.setBlockSize(1);
            encoder.setEXISchema(grammarCache);
            decoder.setEXISchema(grammarCache);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            encoder.setOutputStream(baos);
            URL url = resolveSystemIdAsURL("/interop/datatypes/string/indexed-10.xml");
            InputSource inputSource = new InputSource(url.toString());
            inputSource.setByteStream(url.openStream());
            encoder.encode(inputSource);
            byte[] bts = baos.toByteArray();
            ArrayList<Event> exiEventList = new ArrayList<Event>();
            SAXRecorder saxRecorder = new SAXRecorder(exiEventList, true);
            decoder.setContentHandler(saxRecorder);
            decoder.parse(new InputSource(new ByteArrayInputStream(bts)));
            Assert.assertEquals(302, exiEventList.size());
            Event saxEvent;
            int n = 0;
            saxEvent = exiEventList.get(n++);
            Assert.assertEquals(Event.START_ELEMENT, saxEvent.type);
            Assert.assertEquals("", saxEvent.namespace);
            Assert.assertEquals("root", saxEvent.localName);
            Assert.assertEquals("root", saxEvent.name);
            for (int i = 0; i < 100; i++) {
                saxEvent = exiEventList.get(n++);
                Assert.assertEquals(Event.START_ELEMENT, saxEvent.type);
                Assert.assertEquals("", saxEvent.namespace);
                Assert.assertEquals("a", saxEvent.localName);
                Assert.assertEquals("a", saxEvent.name);
                saxEvent = exiEventList.get(n++);
                Assert.assertEquals(Event.CHARACTERS, saxEvent.type);
                Assert.assertEquals(String.format("test%1$02d", i), new String(saxEvent.charValue));
                saxEvent = exiEventList.get(n++);
                Assert.assertEquals(Event.END_ELEMENT, saxEvent.type);
                Assert.assertEquals("", saxEvent.namespace);
                Assert.assertEquals("a", saxEvent.localName);
                Assert.assertEquals("a", saxEvent.name);
            }
            saxEvent = exiEventList.get(n++);
            Assert.assertEquals(Event.END_ELEMENT, saxEvent.type);
            Assert.assertEquals("", saxEvent.namespace);
            Assert.assertEquals("root", saxEvent.localName);
            Assert.assertEquals("root", saxEvent.name);
        }
    }
