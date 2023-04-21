    public String ask(String s) {
        String query = s;
        try {
            String result = null;
            URL url = new URL("http://www.inferret.com/cgi-bin/edw/pc/qa.pl?question=" + URLEncoder.encode(query, "UTF-8") + "&lang=en&efc=yes&iic=yes&ucd=yes");
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");
            connection.setDoOutput(false);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String inputLine;
            int state = 0;
            String answer = null;
            while ((inputLine = in.readLine()) != null) {
                if (state == 0) {
                    int textPos = inputLine.indexOf("<!--SHORT ANSWERS-->");
                    if (textPos >= 0) {
                        state = 1;
                    }
                } else if (state == 1) {
                    int textPos = inputLine.indexOf("myArray[0]");
                    if (textPos >= 0) {
                        answer = inputLine;
                        state = 2;
                    }
                } else {
                    break;
                }
            }
            in.close();
            if (answer == null || answer.indexOf("Exiting..") >= 0) {
                return null;
            }
            int idf = answer.indexOf("get_answers.pl?answer=");
            if (idf > 0) {
                String chop = answer.substring(idf + 22);
                int id = chop.indexOf("&question=");
                result = chop.substring(0, id);
            } else return null;
            return "I am not sure. Maybe " + result.toLowerCase() + "...?";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
