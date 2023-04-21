    private String readWebPage() throws IOException {
        String readString;
        String result = "";
        BufferedReader HTMLpage = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        while ((readString = HTMLpage.readLine()) != null) result += readString;
        return result;
    }
