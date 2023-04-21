    public static int getPubChemID(String metaboliteName) {
        try {
            System.setProperty("http.proxyHost", "rohto.vtt.fi");
            System.setProperty("http.proxyPort", "8000");
            String page = "http://www.ncbi.nlm.nih.gov/sites/entrez?db=pcsubstance&term=" + URLEncoder.encode("\"" + metaboliteName + "\"", "UTF-8");
            URL urlObject = new URL(page);
            URLConnection con = urlObject.openConnection();
            BufferedReader webData = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            int mark = 0;
            int result = 0;
            while ((line = webData.readLine()) != null) {
                if (line.indexOf("- - - - - - - - begin Results - - - - - -") > -1) {
                    mark = 1;
                }
                if (mark == 1) {
                    if (line.indexOf("var Menu") > -1) {
                        int i = line.indexOf("var Menu") + 8;
                        if (i != -1) {
                            int j = line.indexOf("_");
                            line = line.substring(i, j);
                            result = Integer.parseInt(line.trim());
                            webData.close();
                            webData.close();
                            return result;
                        } else {
                            webData.close();
                            webData.close();
                            return 0;
                        }
                    }
                }
            }
            return 0;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return 0;
        }
    }
