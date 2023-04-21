    private void init() {
        try {
            encodedSearch = URLEncoder.encode(search, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.toString());
        }
        try {
            url = new URL(baseURL + encodedSearch);
            temp = url.openConnection();
            conn = (HttpURLConnection) temp;
            conn.setDoOutput(false);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; PircBot; JVM)");
            conn.setRequestProperty("Pragma", "no-cache");
            conn.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                resultPage += line;
                Matcher matcher = _pattern.matcher(line);
                if (matcher.matches() && i < maxResults) {
                    result[i] = Colors.RED + matcher.group(1) + Colors.NORMAL + " (" + Colors.REVERSE + matcher.group(2).replaceAll("<b>", Colors.BOLD).replaceAll("</b>", Colors.BOLD) + Colors.NORMAL + ")";
                    i++;
                } else {
                    matcher = _pattern_calc.matcher(line);
                    if (matcher.matches()) {
                        result[i] = "Calculator: " + matcher.group(1);
                        i++;
                    }
                }
            }
        } catch (MalformedURLException mue) {
            System.out.println("URL non corretto: " + mue.getMessage());
        } catch (IOException ioe) {
            System.out.println("Errore nella richiesta o nella risposta HTTP: " + ioe.getMessage());
        }
    }
