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
            CMSink<GeneratedPair<GeneratedPatientRecord>> sink = new CMSink<GeneratedPair<GeneratedPatientRecord>>() {

                @Override
                public void open() {
                }

                @Override
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

                @Override
                public void close() {
                }
            };
            try {
                source.open();
            } catch (CMException x) {
                fail(s.name() + ": source.open() failed: " + x.toString());
            }
            assertTrue(s.name(), source.isOpen());
            assertTrue(s.name(), source.sourceInvariant());
            for (int count = 0; count < DEFAULT_COUNT; ++count) {
                try {
                    sink.write(source.read());
                } catch (CMException e) {
                    fail(s.name() + ": read failed: " + e.toString());
                }
            }
            try {
                source.close();
            } catch (CMException x) {
                fail(s.name() + ": source.close() failed: " + x.toString());
            }
            assertTrue(s.name(), !source.isOpen());
            assertTrue(s.name(), source.sourceInvariant());
        }
    }
