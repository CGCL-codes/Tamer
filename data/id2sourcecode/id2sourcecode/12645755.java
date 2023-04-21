    public String fetchPage() {
        Map<String, String> params = getPostParams();
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            String query = "";
            for (Map.Entry<String, String> param : params.entrySet()) {
                query += param.getKey() + "=" + URLEncoder.encode(param.getValue(), "UTF-8") + "&";
            }
            writer.write(query);
            writer.close();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuffer sb = new StringBuffer();
                String line = null;
                while (true) {
                    line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line);
                }
                String result = sb.toString();
                return result;
            } else {
                log.severe("Service returned: " + responseCode);
            }
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Service error", ex);
        }
        return null;
    }
