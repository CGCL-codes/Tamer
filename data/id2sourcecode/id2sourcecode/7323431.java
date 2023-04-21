    public static void main(String[] args) throws Exception {
        HttpClient httpClient = HttpClient.createInstance();
        FileUploadRequest request = httpClient.createFileUploadRequest();
        request.setUrl("http://img5.glowfoto.com/upload2.php");
        request.setFile("image", new File("/home/proktor/a.png"));
        request.addParameter("thumbsize", "200");
        HttpResponse response = httpClient.execute(request);
        System.out.println(IOUtils.toString(response.getResponseBody()));
    }
