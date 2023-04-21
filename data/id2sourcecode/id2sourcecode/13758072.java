    @Override
    public String[] fetchURLContents(String urlString, String userKey) {
        String result[] = new String[2];
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            String userAgent = getThreadLocalRequest().getHeader("user-agent");
            connection.setRequestProperty("User-Agent", userAgent);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder contents = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                contents.append(line + "\r");
            }
            in.close();
            result[0] = contents.toString();
        } catch (Exception e) {
            e.printStackTrace();
            result[1] = ServerUtils.severe("Error while trying to fetch " + urlString + ". " + e.getMessage());
        }
        return result;
    }
