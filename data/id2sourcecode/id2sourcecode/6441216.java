    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String urlBeingSent = req.getParameter("url");
        String notificationToken = req.getParameter("notificationToken");
        if (notificationToken == null) {
            notificationToken = defaultNotificationToken;
        }
        String message = URLEncoder.encode("my message", "UTF-8");
        try {
            URL url = new URL("https://www.appnotifications.com/account/notifications.xml");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            HashMap<String, String> postVariables = new HashMap<String, String>();
            postVariables.put("user_credentials", notificationToken);
            postVariables.put("notification[title]", "View URL");
            postVariables.put("notification[message]", urlBeingSent);
            postVariables.put("notification[run_command]", urlBeingSent);
            resp.sendRedirect(urlBeingSent);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            String sep = "";
            for (String key : postVariables.keySet()) {
                writer.write(sep + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(postVariables.get(key), "UTF-8"));
                sep = "&";
            }
            writer.close();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                log.info("Notification sent: " + url);
            } else {
                log.severe("ERROR sending notification: " + url);
                InputStreamReader inputReader = new InputStreamReader(connection.getInputStream());
                int b = inputReader.read();
                while (b != -1) {
                    b = inputReader.read();
                }
                inputReader.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
