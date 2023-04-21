    protected BufferedImage handleGOVException() {
        if (params.uri.startsWith("http://memory.loc.gov/cgi-bin/ampage")) try {
            URLConnection connection = new URL(params.uri).openConnection();
            params.uri = ".*/cgi-bin/map_item.pl.*<img src=.*";
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String url = null;
            while ((url = reader.readLine()) != null) {
                if (url.matches(params.uri)) {
                    url = "http://memory.loc.gov" + url.substring(url.indexOf("<img src=\"") + 10);
                    url = url.substring(0, url.indexOf("\""));
                    break;
                }
            }
            if (url != null) {
                connection = new URL(url).openConnection();
                return processNewUri(connection);
            }
        } catch (Exception e) {
        }
        return null;
    }
