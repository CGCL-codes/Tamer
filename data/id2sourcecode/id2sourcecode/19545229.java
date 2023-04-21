    public void testInvariants() {
        for (final SOURCE_TYPE s : SOURCE_TYPE.values()) {
            DefaultGroupSource<GeneratedPatientRecord> tmp = null;
            try {
                tmp = s.cls.newInstance();
            } catch (Exception e) {
                fail(e.toString());
            }
            final DefaultGroupSource<GeneratedPatientRecord> source = tmp;
            assertTrue(s.name(), !source.isOpen());
            assertTrue(s.name(), source.sourceInvariant());
            Sink<GeneratedPair<GeneratedPatientRecord>> sink = new Sink<GeneratedPair<GeneratedPatientRecord>>() {

                public void open() {
                }

                public void write(GeneratedPair<GeneratedPatientRecord> pair) {
                    try {
                        assertTrue(s.name(), source.isOpen());
                        assertTrue(s.name(), source.sourceInvariant());
                    } catch (Exception x) {
                        fail(s.name() + ": sourceInvariant failed: " + x.toString());
                    }
                    try {
                        assertTrue(s.name(), source.pairInvariant(pair));
                    } catch (Exception x) {
                        fail(s.name() + ": pairInvariant failed: " + x.toString());
                    }
                    try {
                        assertTrue(s.name(), source.groupInvariant(pair));
                    } catch (IllegalStateException x) {
                        fail(s.name() + ": groupInvariant failed: " + x.toString());
                    }
                }

                public void close() {
                }
            };
            try {
                source.open();
            } catch (ProcessingException x) {
                fail(s.name() + ": source.open() failed: " + x.toString());
            }
            assertTrue(s.name(), source.isOpen());
            assertTrue(s.name(), source.sourceInvariant());
            for (int count = 0; count < DEFAULT_COUNT; ++count) {
                try {
                    sink.write(source.read());
                } catch (ProcessingException e) {
                    fail(s.name() + ": read failed: " + e.toString());
                }
            }
            try {
                source.close();
            } catch (ProcessingException x) {
                fail(s.name() + ": source.close() failed: " + x.toString());
            }
            assertTrue(s.name(), !source.isOpen());
            assertTrue(s.name(), source.sourceInvariant());
        }
    }
