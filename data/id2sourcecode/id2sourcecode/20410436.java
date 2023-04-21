    @Test
    public void testJsonCollection() throws Exception {
        FileTrackingStatusFormatter formatter = new FileTrackingStatusFormatter();
        List<FileStatus.FileTrackingStatus> coll = new ArrayList<FileStatus.FileTrackingStatus>();
        for (int i = 0; i < 10; i++) {
            Builder status = FileStatus.FileTrackingStatus.newBuilder();
            status.setAgentName("test");
            status.setLogType("test" + i);
            status.setFileName("test" + i);
            status.setDate(System.currentTimeMillis());
            status.setLastModifiedTime(System.currentTimeMillis());
            status.setFilePointer(1);
            status.setLinePointer(1);
            coll.add(status.build());
        }
        StringWriter writer = new StringWriter();
        formatter.writeList(FORMAT.JSON, coll, writer);
        System.out.println(writer.toString());
        Collection<FileStatus.FileTrackingStatus> files = formatter.readList(FORMAT.JSON, new StringReader(writer.toString()));
        assertNotNull(files);
        assertEquals(10, files.size());
    }
