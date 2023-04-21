    private String doRequest(Method method, String action, PostParameter... params) {
        StringBuilder output = new StringBuilder();
        BufferedReader reader = null;
        OutputStreamWriter writer = null;
        try {
            String target = (action == null) ? url : url + action;
            URL url = new URL(target);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method.toString());
            connection.setRequestProperty("Accept", "text/xml");
            if (method == Method.POST) {
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            }
            connection.setDoOutput(method == Method.POST);
            connection.setDoInput(true);
            connection.setReadTimeout(30 * 1000);
            connection.connect();
            if (method == Method.POST) {
                assert params != null;
                writer = new OutputStreamWriter(connection.getOutputStream());
                for (PostParameter param : params) {
                    writer.write(param.encode());
                }
                writer.close();
                writer = null;
            }
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return output.toString();
    }
