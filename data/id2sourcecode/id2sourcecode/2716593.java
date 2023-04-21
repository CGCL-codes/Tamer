    public void testReadHTML() throws Exception {
        StringBuffer response = new StringBuffer();
        response.append("HTTP/1.1 200 OK\n").append("content-length: 44\n").append("content-type: text/html\n").append("content-encoding: MyEncoding\n").append("expires: Mon, 05 Oct 1998 01:23:50 GMT\n").append("date: Fri, 02 Oct 1998 13:27:15 GMT\n").append("last-modified: Thu, 01 Oct 1998 18:00:32 GMT\n").append("user-agent: My little browser.\n\n").append("<html>\n<body>\n<p>Hello World</p>\n</body>\n</html>\n");
        System.out.println("length is " + response.length());
        InputStream dataIS = new ReplayInputStream(new ByteArrayInputStream(response.toString().getBytes()));
        TestURLRegistry.register("replay", dataIS);
        URL url = new URL("testurl:replay");
        URLConnection conn = url.openConnection();
        assertEquals("text/html", conn.getContentType());
        assertEquals(44, conn.getContentLength());
        assertEquals("MyEncoding", conn.getContentEncoding());
        assertEquals(Date.parse("Fri, 02 Oct 1998 13:27:15 GMT"), conn.getDate());
        assertEquals(Date.parse("Mon, 05 Oct 1998 01:23:50 GMT"), conn.getExpiration());
        assertEquals(Date.parse("Thu, 01 Oct 1998 18:00:32 GMT"), conn.getLastModified());
        assertEquals("My little browser.", conn.getHeaderField("user-agent"));
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        assertEquals("<html>", rd.readLine());
        assertEquals("<body>", rd.readLine());
        assertEquals("<p>Hello World</p>", rd.readLine());
        assertEquals("</body>", rd.readLine());
        assertEquals("</html>", rd.readLine());
    }
