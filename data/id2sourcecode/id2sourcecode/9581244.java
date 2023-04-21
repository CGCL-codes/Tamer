    @Test
    public void mockURLAndURLConnectionUsingMockParameterAndMockField(final URL url) throws Exception {
        new Expectations() {

            {
                url.openConnection();
                result = mockConnection;
            }
        };
        URLConnection conn = url.openConnection();
        assertSame(mockConnection, conn);
    }
