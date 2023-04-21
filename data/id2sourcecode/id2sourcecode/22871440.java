    @PUT
    @Path("xop")
    @Consumes(MultipartConstants.MULTIPART_RELATED)
    public void putXopWithMultipartRelated(@XopWithMultipartRelated Xop xop) throws IOException {
        Assert.assertNotNull(xop.getBill());
        Assert.assertEquals("billé", xop.getBill().getName());
        Assert.assertNotNull(xop.getMonica());
        Assert.assertEquals("monica", xop.getMonica().getName());
        Assert.assertNotNull(xop.getMyBinary());
        Assert.assertNotNull(xop.getMyDataHandler());
        Assert.assertEquals("Hello Xop World!", new String(xop.getMyBinary(), "UTF-8"));
        for (int fi = 0; fi < 2; fi++) {
            InputStream inputStream = xop.getMyDataHandler().getInputStream();
            InputStreamReader inputStreamReader = null;
            try {
                inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                StringWriter writer = new StringWriter();
                char[] buffer = new char[4048];
                int n = 0;
                while ((n = inputStreamReader.read(buffer)) != -1) writer.write(buffer, 0, n);
                Assert.assertEquals("Hello Xop World!", writer.toString());
            } finally {
                if (inputStreamReader != null) inputStreamReader.close();
                inputStream.close();
            }
        }
    }
