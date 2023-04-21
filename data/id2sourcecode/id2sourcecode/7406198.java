    public static byte[] readFile(URL url) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        StringBuffer buf = new StringBuffer();
        while ((inputLine = in.readLine()) != null) buf.append(inputLine).append('\n');
        in.close();
        return buf.toString().getBytes();
    }
