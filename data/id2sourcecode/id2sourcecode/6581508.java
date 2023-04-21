    @Test
    public void testAuthorizedMirrorHEAD() throws IOException {
        final URL url = new URL("http://127.0.0.1:" + testPort + "/mirror?version=5&direction=just+right");
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("HEAD");
        con.setRequestProperty("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        con.setRequestProperty("WWW-Authenticate", "Basic realm=\"karatasi\"");
        con.setRequestProperty("testline1", "1234567890");
        assertEquals("Expecting resource to exist.", HttpURLConnection.HTTP_OK, con.getResponseCode());
        assertTrue("mirror responds with Content-Type text/plain.", con.getContentType().startsWith("text/plain"));
        assertNull("The server does not use any special encoding.", con.getContentEncoding());
        int bytesRemaining = con.getContentLength();
        final int originalBytesRemaining = bytesRemaining;
        final InputStream err = con.getErrorStream();
        assertNull("Expecting no error stream.", err);
        final InputStream in = con.getInputStream();
        final byte[] buf = new byte[bytesRemaining];
        for (int bytesRead; bytesRemaining > 0 && (bytesRead = in.read(buf, buf.length - bytesRemaining, bytesRemaining)) != -1; bytesRemaining -= bytesRead) {
            fail("Expecting server to send no data.");
        }
        assertEquals("Expecting server to send not fewer bytes as indicated.", originalBytesRemaining, bytesRemaining);
    }
