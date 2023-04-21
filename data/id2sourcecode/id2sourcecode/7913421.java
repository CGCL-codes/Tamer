    public String translate(String segment, String sourceLanguage, String targetLanguage) {
        try {
            String translation = "";
            String address = "http://api.microsofttranslator.com/V1/Http.svc/Translate";
            String baseQuery = "appId=1B25382C82AA5FFC00EC0A5043C9D98D329E956B&from=%s&to=%s";
            String microsoftURL = de.folt.util.OpenTMSProperties.getInstance().getOpenTMSProperty("Microsoft.URL");
            if ((microsoftURL != null) && !microsoftURL.equals("")) {
                address = microsoftURL;
            }
            String microsoftQuery = de.folt.util.OpenTMSProperties.getInstance().getOpenTMSProperty("Microsoft.Query");
            if ((microsoftQuery != null) && !microsoftQuery.equals("")) {
                baseQuery = microsoftQuery;
            }
            String query = address + "";
            String data = String.format(baseQuery, sourceLanguage, targetLanguage);
            String fullQuery = query + "?" + data;
            URL url = new URL(fullQuery);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setAllowUserInteraction(false);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-length", data.length() + "");
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            wr.write(segment);
            wr.flush();
            wr.close();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder res = new StringBuilder();
            char[] buf = new char[2048];
            int count = 0;
            while ((count = rd.read(buf)) != -1) {
                res.append(buf, 0, count);
            }
            rd.close();
            res.deleteCharAt(0);
            translation = res.toString();
            return translation;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
