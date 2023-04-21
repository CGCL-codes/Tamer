    public void testHeader_BoundaryCheck() throws IOException {
        String resourceName = "org/apache/harmony/luni/tests/";
        URL url = ClassLoader.getSystemClassLoader().getResource(resourceName);
        URLConnection urlConnection = url.openConnection();
        assertNull(urlConnection.getHeaderField(Integer.MIN_VALUE));
        assertNull(urlConnection.getHeaderField(Integer.MAX_VALUE));
        assertNull(urlConnection.getHeaderFieldKey(Integer.MIN_VALUE));
        assertNull(urlConnection.getHeaderFieldKey(Integer.MAX_VALUE));
        assertNull(urlConnection.getHeaderField(null));
    }
