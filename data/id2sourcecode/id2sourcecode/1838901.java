    public String getOfdbIdFromOfdb(String imdbId) {
        try {
            URL url;
            URLConnection urlConn;
            DataOutputStream printout;
            url = new URL("http://www.ofdb.de/view.php?page=suchergebnis");
            urlConn = url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            printout = new DataOutputStream(urlConn.getOutputStream());
            String content = "&SText=" + imdbId + "&Kat=IMDb";
            printout.writeBytes(content);
            printout.flush();
            printout.close();
            StringWriter site = new StringWriter();
            BufferedReader input = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
            String line;
            while ((line = input.readLine()) != null) {
                site.write(line);
            }
            input.close();
            String xml = site.toString();
            String ofdbID;
            int beginIndex = xml.indexOf("film/");
            if (beginIndex != -1) {
                StringTokenizer st = new StringTokenizer(xml.substring(beginIndex), "\"");
                ofdbID = st.nextToken();
                ofdbID = "http://www.ofdb.de/" + ofdbID;
            } else {
                ofdbID = "UNKNOWN";
            }
            System.out.println(ofdbID);
            return ofdbID;
        } catch (Exception e) {
            logger.severe("Failed retreiving ofdb URL for movie : ");
            logger.severe("Error : " + e.getMessage());
            return "UNKNOWN";
        }
    }
