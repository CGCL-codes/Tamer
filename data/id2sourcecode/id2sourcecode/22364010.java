    @Test
    public void testJsonList() throws Exception {
        Collection<FileTrackingStatus> coll = new ArrayList<FileTrackingStatus>();
        for (int i = 0; i < 10; i++) {
            FileTrackingStatus status = new FileTrackingStatus();
            status.setPath("test" + i);
            status.setLogType("test" + i);
        }
        FileTrackingStatusFormatter formatter = new FileTrackingStatusFormatter();
        StringWriter writer = new StringWriter();
        formatter.writeList(FORMAT.JSON, coll, writer);
        Collection<FileTrackingStatus> collRead = formatter.readList(FORMAT.JSON, new StringReader(writer.toString()));
        assertEquals(coll.size(), collRead.size());
    }
