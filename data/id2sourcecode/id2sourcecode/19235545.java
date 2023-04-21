    protected BufferedImage handleNLAException() {
        if (params.uri.startsWith("http://nla.gov.au/nla.map")) try {
            params.uri = params.uri + "-v.jpg";
            URL url = new URL(params.uri);
            URLConnection connection = url.openConnection();
            return processNewUri(connection);
        } catch (Exception e) {
        } else if (params.uri.startsWith("http://www.nla.gov.au/apps/cdview?pi=nla.map")) try {
            params.uri = "http://nla.gov.au/nla.map" + params.uri.substring(params.uri.indexOf('-'), params.uri.lastIndexOf('-')) + "-v.jpg";
            URL url = new URL(params.uri);
            URLConnection connection = url.openConnection();
            return processNewUri(connection);
        } catch (Exception e) {
        }
        return null;
    }
