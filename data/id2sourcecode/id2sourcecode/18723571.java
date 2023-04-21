    public User getUser(String username) {
        URL url;
        try {
            url = new URL("http://vimeo.com/api/" + username + "/info.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            InputStream in = conn.getInputStream();
            Gson gson = new Gson();
            return gson.fromJson(new BufferedReader(new InputStreamReader(in)), User.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
