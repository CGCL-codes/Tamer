    protected void assertEquals(Broadcast data1, Broadcast data2) {
        assertEquals("ID", data1.getId(), data2.getId());
        assertEquals("date", data1.getDate(), data2.getDate());
        assertEquals("descr", data1.getDescr(), data2.getDescr());
        assertEquals("extId", data1.getExtId(), data2.getExtId());
        assertEquals("name", data1.getName(), data2.getName());
        assertEquals("pic", data1.getPicName(), data2.getPicName());
        assertEquals("type", data1.getType(), data2.getType());
        assertSame("channel", data1.getChannel(), data2.getChannel());
    }
