    private void testRequest(HttpMethod method, byte expectedResponse[], boolean stripWhitespaceAndFormatting) {
        try {
            int httpResult = client.executeMethod(method);
            assertEquals(HttpStatus.SC_OK, httpResult);
            byte buf[] = new byte[1024];
            int read = -1;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream is = method.getResponseBodyAsStream();
            while ((read = is.read(buf)) > -1) {
                baos.write(buf, 0, read);
            }
            byte actualResponse[] = baos.toByteArray();
            if (stripWhitespaceAndFormatting) {
                expectedResponse = new String(expectedResponse).replace("\n", "").replace("\t", "").replace(" ", "").getBytes();
                actualResponse = new String(actualResponse).replace("\n", "").replace("\t", "").replace(" ", "").getBytes();
            }
            assertArrayEquals(expectedResponse, actualResponse);
        } catch (HttpException he) {
            fail(he.getMessage());
        } catch (IOException ioe) {
            fail(ioe.getMessage());
        } finally {
            method.releaseConnection();
        }
    }
