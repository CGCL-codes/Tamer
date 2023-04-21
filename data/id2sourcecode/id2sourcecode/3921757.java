    public InputStream doPost(String adress) throws IOException {
        InputStream input = null;
        URL url = new URL(adress);
        URLConnection connection = url.openConnection(proxy);
        connection.setDoOutput(true);
        connection.setRequestProperty("Cookie", jSessionId);
        OutputStream output = connection.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(output);
        writer.write(this.getParameters());
        writer.flush();
        writer.close();
        output.flush();
        output.close();
        input = connection.getInputStream();
        return input;
    }
