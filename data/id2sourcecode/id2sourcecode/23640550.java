    public void testRemoves() throws Exception {
        String[][] tests = new String[][] { { "/test", "[/, /test]", "/test", "/test", "[/]" }, { "/test/123", "[/, /test, /test/123]", "/test/123", "/test/123", "[/, /test]" }, { "/test/123", "[/, /test, /test/123]", "/test/abc", null, "[/, /test, /test/123]" }, { "/test/123", "[/, /test, /test/123]", "/123", null, "[/, /test, /test/123]" }, { "/test/123", "[/, /test, /test/123]", "/test", null, "[/, /test, /test/123]" } };
        for (String[] test : tests) {
            _bayeux.getChannel(test[0], true);
            assertEquals(test[1], _bayeux.getChannels().toString());
            Channel removed = _bayeux.removeChannel(test[2]);
            assertEquals(test[3], removed == null ? null : removed.toString());
            assertEquals(test[4], _bayeux.getChannels().toString());
        }
    }
