    public MultipartPost(String url) throws IOException {
        Random random = new Random();
        connection = (new URL(url)).openConnection();
        boundary = "<<" + Long.toString(random.nextLong(), 30);
        String type = "multipart/form-data; boundary=" + boundary;
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", type);
        stream = connection.getOutputStream();
    }
