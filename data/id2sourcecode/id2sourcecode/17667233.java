    public void assertEquals(StatusTableRow str, String host, String name, String group, int port, long updateTimestamp, boolean ssl, boolean online, boolean readOnly, boolean writeOnly, boolean dataStore, Collection<HashSpan> hashSpans) throws Exception {
        if (str == null) {
            fail("Object is null");
        }
        assertEquals(host, str.getHost());
        assertEquals(name, str.getName());
        assertEquals(group, str.getGroup());
        assertEquals(port, str.getPort());
        assertEquals(updateTimestamp, str.getUpdateTimestamp());
        assertEquals(ssl, str.isSSL());
        assertEquals(online, str.isOnline());
        assertEquals(readOnly, str.isReadable());
        assertEquals(writeOnly, str.isWritable());
        assertEquals(dataStore, str.isDataStore());
        assertEquals(hashSpans.size(), str.getHashSpans().size());
        if (!HashSpanCollection.areEqual(hashSpans, str.getHashSpans())) {
            fail("Hash span collections are inequal.");
        }
    }
