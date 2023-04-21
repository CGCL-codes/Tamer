        public List downloadQuery(String query, int start) throws IOException {
            if (cookie == null) return null;
            String fullQuery = QUERY_URL + "?" + query;
            if (start != 0) fullQuery = fullQuery + "&pagestart=" + start;
            URL queryURL = new URL(fullQuery);
            URLConnection urlConn = queryURL.openConnection();
            urlConn.setRequestProperty("Cookie", cookie);
            urlConn.setRequestProperty("Accept-Encoding", "gzip");
            urlConn.connect();
            InputStream input = urlConn.getInputStream();
            String encode = urlConn.getContentEncoding();
            if ("gzip".equalsIgnoreCase(encode)) {
                input = new GZIPInputStream(input);
            }
            List beacons = new LinkedList();
            parseStream(input, beacons);
            input.close();
            return beacons;
        }
