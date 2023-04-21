    private void downloadTEIFile(URI uri, File teiFile) throws Exception {
        Client create = Client.create();
        WebResource service = create.resource(getGROBIDHost());
        ClientResponse response = null;
        service = Client.create().resource(uri);
        response = service.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
        System.out.println("downloading tei file from '" + uri + "'.");
        System.out.println("status: " + response.getStatus());
        if (Status.ACCEPTED.getStatusCode() == response.getStatus()) {
            int counter = 50;
            while ((counter != 0) && (Status.OK.getStatusCode() != response.getStatus())) {
                response = service.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
                counter--;
                Thread.sleep(1000);
            }
        }
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        InputStream inputStream = response.getEntity(InputStream.class);
        OutputStream out = null;
        try {
            out = new FileOutputStream(teiFile);
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
    }
