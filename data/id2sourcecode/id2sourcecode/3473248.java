    protected void setUp() {
        try {
            url = new URL("http://localhost:" + port + "/");
            uc = (HttpURLConnection) url.openConnection();
        } catch (Exception e) {
            fail("Exception during setup : " + e.getMessage());
        }
        mockHeaderMap = new Hashtable<String, List<String>>();
        List<String> valueList = new ArrayList<String>();
        valueList.add("value1");
        valueList.add("value2");
        mockHeaderMap.put("field1", valueList);
        mockHeaderMap.put("field2", valueList);
        isGetCalled = false;
        isPutCalled = false;
        isCacheWriteCalled = false;
    }
