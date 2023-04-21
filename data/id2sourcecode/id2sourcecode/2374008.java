    public void testWriteReadXml() {
        final String TYPE = "GenericEvent";
        final String KEY1 = "k1";
        final String VALUE1 = "v1";
        final EventsFactory factory = new EventsFactoryImpl();
        final double time = 3601;
        GenericEvent writeEvent = factory.createGenericEvent(TYPE, time);
        writeEvent.getAttributes().put(KEY1, VALUE1);
        GenericEvent readEvent = XmlEventsTester.testWriteReadXml(getOutputDirectory() + "events.xml", writeEvent);
        assertEquals(TYPE, readEvent.getAttributes().get("type"));
        assertEquals(VALUE1, readEvent.getAttributes().get(KEY1));
        assertEquals(String.valueOf(time), readEvent.getAttributes().get("time"));
        assertEquals(time, readEvent.getTime());
    }
