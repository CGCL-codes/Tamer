    public static Map<String, List<String>> getURLHeadersForMP3Server(URL url) {
        Map<String, List<String>> header = null;
        try {
            URLConnection connection = (URLConnection) url.openConnection();
            connection.setRequestProperty("Icy-MetaData", "1");
            connection.setRequestProperty("User-Agent", "Winamp/5.52");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Connection", "close");
            connection.setConnectTimeout(7000);
            connection.connect();
            header = connection.getHeaderFields();
            if (header.get("Server") == null || connection.getContentType().equalsIgnoreCase("unknown/unknown")) {
                Log.writeToStdout(Log.WARNING, "FileHelper", "getURLHeaderForMP3Server", "Header not found in " + url.toString() + " Retry Force");
                header = new Hashtable<String, List<String>>();
                String inputLine;
                int lineNumber = 0;
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((inputLine = in.readLine()) != null && lineNumber < 15) {
                    lineNumber++;
                    if (inputLine.length() == 0) {
                        break;
                    }
                    List<String> values = null;
                    String key = "";
                    String value = "";
                    int location = inputLine.indexOf(":");
                    if (location > 0) {
                        key = inputLine.substring(0, location);
                        if (key.trim().equalsIgnoreCase("content-type")) {
                            key = "Content-type";
                        }
                        location++;
                    } else {
                        location = 0;
                    }
                    if (inputLine.length() > location) {
                        value = inputLine.substring(location).trim();
                    }
                    values = header.get(key);
                    if (values != null && value != null) {
                        values.add(value);
                    } else if (values == null && value != null) {
                        values = new ArrayList<String>();
                        values.add(value);
                    }
                    header.put(key, values);
                }
                in.close();
            }
        } catch (IOException e) {
            Log.writeToStdout(Log.WARNING, "FileHelper", "getURLHeaderForMP3Server", e.getClass() + " " + e.getMessage());
        }
        return header;
    }
