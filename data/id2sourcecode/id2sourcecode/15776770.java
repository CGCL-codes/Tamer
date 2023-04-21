    public InputStream executeQuery(String urlString, Parameter... parameters) throws BugzillaException {
        if (parameters.length > 0) {
            StringBuilder stringBuilder = new StringBuilder(urlString);
            if (urlString.contains("?")) {
                if (!urlString.endsWith("?") && !urlString.endsWith("&")) {
                    stringBuilder.append("&");
                }
            } else {
                stringBuilder.append("?");
            }
            boolean isFirst = true;
            for (Parameter parameter : parameters) {
                if (!isFirst) {
                    stringBuilder.append("&");
                }
                isFirst = false;
                stringBuilder.append(parameter.getName()).append("=").append(parameter.getValue());
            }
            urlString = stringBuilder.toString();
        }
        try {
            URLConnection connection = new URL(urlString).openConnection();
            connection.setRequestProperty("Cookie", Utils.makeCookieString(cookies));
            connection.connect();
            return connection.getInputStream();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
