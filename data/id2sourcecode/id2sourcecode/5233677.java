    public static Vector<String> getData(URL url) {
        Vector<String> answer = new Vector<String>();
        int responseCode = 0;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            responseCode = connection.getResponseCode();
        } catch (Exception ex) {
            return null;
        }
        if (responseCode != 200) {
            return null;
        }
        try {
            String line;
            InputStream input = connection.getInputStream();
            BufferedReader dataInput = new BufferedReader(new InputStreamReader(input));
            while ((line = dataInput.readLine()) != null) {
                answer.add(line);
            }
        } catch (Exception ex) {
            return null;
        }
        return answer;
    }
