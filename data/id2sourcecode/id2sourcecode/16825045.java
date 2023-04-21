    @Test
    @Browsers(Browser.NONE)
    public void testUploadFileWithNonASCIIName_HttpClient() throws Exception {
        final String filename = "檔案파일ファイルملف.txt";
        final String path = getClass().getClassLoader().getResource(filename).toExternalForm();
        final File file = new File(new URI(path));
        assertTrue(file.exists());
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/upload2", Upload2Servlet.class);
        startWebServer("./", null, servlets);
        final HttpPost filePost = new HttpPost("http://localhost:" + PORT + "/upload2");
        final MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        reqEntity.addPart("myInput", new FileBody(file, "application/octet-stream"));
        filePost.setEntity(reqEntity);
        final HttpClient client = new DefaultHttpClient();
        final HttpResponse httpResponse = client.execute(filePost);
        InputStream content = null;
        try {
            content = httpResponse.getEntity().getContent();
            final String response = new String(IOUtils.toByteArray(content));
            assertFalse("3F 3F 3F 3F 3F 3F 3F 3F 3F 3F 3F 2E 74 78 74 <br>myInput".equals(response));
        } finally {
            IOUtils.closeQuietly(content);
        }
    }
