    public static String apiQuery(final Map<String, String> arg) {
        if (arg.isEmpty()) {
            return null;
        }
        if (!arg.containsKey("format")) {
            arg.put("format", "xml");
        }
        StringBuilder data = new StringBuilder();
        boolean first = true;
        for (String key : arg.keySet()) {
            if (!first) {
                data.append("&");
            } else {
                first = false;
            }
            try {
                data.append(URLEncoder.encode(key, "UTF-8"));
                data.append("=");
                data.append(URLEncoder.encode(arg.get(key), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                logger.error("UTF-8 encoding not supported: " + e.getMessage());
                return null;
            }
        }
        if (path == null || path.isEmpty()) {
            path = Config.get("api.path");
        }
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(path);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("Cookie", cookies);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data.toString());
            writer.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                result.append(line);
                line = reader.readLine();
            }
            Map<String, List<String>> headers = conn.getHeaderFields();
            List<String> values = headers.get("Set-Cookie");
            if (values != null) {
                for (String value : values) {
                    if (cookies.isEmpty()) {
                        cookies = value;
                    } else {
                        cookies = cookies + ";" + value;
                    }
                }
            }
        } catch (MalformedURLException e) {
            logger.error("URL to api.php ist not valid: " + e.getMessage());
            return null;
        } catch (IOException e) {
            logger.error("Couldn't do API request: " + e.getMessage());
            return null;
        }
        return result.toString();
    }
