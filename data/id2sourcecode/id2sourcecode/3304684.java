    public String getContent() throws IOException {
        URLConnection urlConnection = new URL(url).openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        byte[] bytes = read(inputStream);
        String response = new String(bytes, "windows-1251");
        inputStream.close();
        return response;
    }
