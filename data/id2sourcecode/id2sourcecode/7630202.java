    public void testUploadDownloadOriginDocument() throws IOException {
        GROBIDJob job = this.createJob();
        File pdfFile = new File(this.getResourceDir().getAbsoluteFile() + "/sample1/sample.pdf");
        this.uploadPDFDocument(job.getDocumentOriginURI(), pdfFile);
        {
            Client create = Client.create();
            WebResource service = create.resource(getGROBIDHost());
            ClientResponse response = null;
            service = Client.create().resource(job.getDocumentOriginURI());
            response = service.accept("application/pdf").get(ClientResponse.class);
            assertEquals(Status.OK.getStatusCode(), response.getStatus());
            File d_pdfFile = new File(this.getTMPDir().getAbsoluteFile() + "/sample1_d.pdf");
            InputStream inputStream = response.getEntity(InputStream.class);
            OutputStream out = null;
            try {
                out = new FileOutputStream(d_pdfFile);
                byte buf[] = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) out.write(buf, 0, len);
            } catch (IOException e) {
                throw e;
            } finally {
                try {
                    if (out != null) out.close();
                    inputStream.close();
                } catch (IOException e) {
                    throw e;
                }
            }
            assertTrue(pdfFile.exists());
        }
    }
