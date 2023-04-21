    private boolean getInfo(String searchString) {
        try {
            String urlString = _searchUrl + filterTitle(searchString);
            URL url = new URL(urlString);
            URLConnection urlConn = url.openConnection();
            if (!(urlConn.getContent() instanceof InputStream)) return false;
            boolean redirect = false;
            String data = "";
            BufferedReader in = null;
            String line;
            try {
                in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                while ((line = in.readLine()) != null) {
                    data += line + "\n";
                }
            } finally {
                if (in != null) {
                    in.close();
                }
            }
            if (data.indexOf("<b>No Matches.</b>") > 0) return false;
            if (data.indexOf("<title>IMDb  Search</title>") >= 0 || data.indexOf("<a href=\"/title/tt") >= 0) {
                int start = data.indexOf("/title/tt");
                if (start > 0) {
                    int end = data.indexOf("/", start + "/title/tt".length());
                    _url = data.substring(start, end);
                    if (_url.indexOf("http://") < 0) _url = _baseUrl + _url;
                }
                if (_url == null) return false;
            } else {
                _url = urlString;
            }
            url = new URL(_url);
            urlConn = url.openConnection();
            if (!(urlConn.getContent() instanceof InputStream)) return false;
            try {
                in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                while ((line = in.readLine()) != null) data = data + line + "\n";
            } finally {
                in.close();
            }
            _title = parseData(data, "<h1>", " <span>");
            _genre = parseData(data, "<h5>Genre:</h5>", "</div>");
            _genre = _genre.replaceAll("more", "").trim();
            _plot = parseData(data, "<h5>Plot Outline:</h5>", "<a href=\"");
            _rating = parseData(data, "<b>User Rating:</b>", "<small>");
            _rating = _rating.equals("N/A") || _rating.indexOf("/") < 0 ? "N/A" : _rating.substring(0, _rating.indexOf("/"));
            _votes = parseData(data, "<b>User Rating:</b>", "</small>");
            _votes = _votes.indexOf("(") < 0 || _votes.indexOf("votes") < 0 ? "N/A" : _votes.substring(_votes.indexOf("(") + 1, _votes.indexOf("votes")).trim();
            _year = parseData(data, "<a href=\"/Sections/Years/", "</a>");
            if (_year.length() >= 6) _year = _year.substring(6);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
