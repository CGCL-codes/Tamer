    private String translate(String segment, String sourceLanguage, String targetLanguage) {
        try {
            segment = segment.replaceAll("\n", "<p>");
            segment = segment.replaceAll("\r", "<br>");
            String address = "http://ajax.googleapis.com/ajax/services/language/translate?v=1.0";
            String baseQuery = "&q=%s&langpair=%s%%7C%s";
            Pattern pattern = Pattern.compile(".*translatedText\":\"(.*?)\"},.*");
            String googleURL = de.folt.util.OpenTMSProperties.getInstance().getOpenTMSProperty("Google.URL");
            if ((googleURL != null) && !googleURL.equals("")) {
                address = googleURL;
            }
            String googleQuery = de.folt.util.OpenTMSProperties.getInstance().getOpenTMSProperty("Google.Query");
            if ((googleQuery != null) && !googleQuery.equals("")) {
                baseQuery = googleQuery;
            }
            String googlePattern = de.folt.util.OpenTMSProperties.getInstance().getOpenTMSProperty("Google.Pattern");
            if ((googlePattern != null) && !googlePattern.equals("")) {
                pattern = Pattern.compile(googlePattern);
            }
            String query = address + String.format(baseQuery, URLEncoder.encode(segment, "UTF-8"), sourceLanguage, targetLanguage);
            URL url = new URL(query);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder res = new StringBuilder();
            char[] buf = new char[2048];
            int count = 0;
            while ((count = rd.read(buf)) != -1) {
                res.append(buf, 0, count);
            }
            rd.close();
            String result = res.toString();
            Matcher m = pattern.matcher(result);
            String translation = "";
            if (m.find()) {
                translation = m.group(1);
            }
            String re = Pattern.quote("\\u003cp\\u003e");
            translation = translation.replaceAll(re, "\n");
            re = Pattern.quote("\\u003cbr\\u003e");
            translation = translation.replaceAll(re, "\r");
            re = Pattern.quote("\\u0026");
            translation = translation.replaceAll(re, "&");
            translation = HTMLEntities.unhtmlentities(translation);
            return translation;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
