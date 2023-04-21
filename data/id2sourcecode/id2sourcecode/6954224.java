    private List fetchIdsOfPage(String query, int start) {
        String url = "http://www.nal.usda.gov/cgi-bin/agricola-ind?searchtype=keyword&searcharg=" + query + "&startnum=" + start;
        List agricolaIds = new ArrayList();
        try {
            in = new BufferedReader(new InputStreamReader((new URL(url)).openStream()));
            in.readLine();
            String inputLine = "";
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.startsWith("<tr> <td")) {
                    StringTokenizer st = new StringTokenizer(inputLine, "<>");
                    while (st.hasMoreTokens()) {
                        String token = st.nextToken();
                        if (token.startsWith("a href")) {
                            String bib = token.substring(token.indexOf("?") + 5, token.indexOf("&"));
                            if (!agricolaIds.contains(bib)) {
                                agricolaIds.add(bib);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return agricolaIds;
    }
