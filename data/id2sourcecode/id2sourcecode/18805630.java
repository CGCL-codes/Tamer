    @Test
    public void testStreamBinary() {
        final String testValue = "hello world";
        final String xquery = "response:stream-binary(xs:base64Binary('" + Base64.encodeBase64String(testValue.getBytes()) + "'), 'application/octet-stream', 'test.bin')";
        GetMethod get = new GetMethod(COLLECTION_ROOT_URL);
        NameValuePair qsParams[] = { new NameValuePair("_query", xquery), new NameValuePair("_indent", "no") };
        get.setQueryString(qsParams);
        try {
            int httpResult = client.executeMethod(get);
            byte buf[] = new byte[1024];
            int read = -1;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream is = get.getResponseBodyAsStream();
            while ((read = is.read(buf)) > -1) {
                baos.write(buf, 0, read);
            }
            assertEquals(httpResult, HttpStatus.SC_OK);
            assertArrayEquals(testValue.getBytes(), baos.toByteArray());
        } catch (HttpException he) {
            fail(he.getMessage());
        } catch (IOException ioe) {
            fail(ioe.getMessage());
        } finally {
            get.releaseConnection();
        }
    }
