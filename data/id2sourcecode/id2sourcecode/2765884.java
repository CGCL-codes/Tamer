    public String fetchRemoteData(URL url) throws IOException {
        URLConnection jamonCon = url.openConnection();
        BufferedReader jamonReader = new BufferedReader(new InputStreamReader(jamonCon.getInputStream()));
        StringBuffer buffer = new StringBuffer();
        String line = jamonReader.readLine();
        while (line != null) {
            buffer.append(line);
            line = jamonReader.readLine();
        }
        return buffer.toString();
    }
