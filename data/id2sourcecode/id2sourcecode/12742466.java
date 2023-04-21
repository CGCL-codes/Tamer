    public void send() {
        try {
            URL url = new URL("http://asia.search.yahooapis.com/cas/v1/ws");
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setUseCaches(false);
            DataOutputStream post = new DataOutputStream(httpConn.getOutputStream());
            String postData = "appid=" + this.appid + "&content=" + URLEncoder.encode(this.rawText, "UTF-8");
            post.writeBytes(postData);
            post.flush();
            post.close();
            BufferedReader response = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
            String s;
            while ((s = response.readLine()) != null) {
                this.returnText += s;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
