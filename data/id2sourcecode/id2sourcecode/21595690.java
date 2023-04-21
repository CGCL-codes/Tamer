    @Override
    public void run() {
        resultEntries = new ArrayList<ResultEntry>();
        try {
            System.out.println("Scraping : " + _url);
            URLConnection urlConn = _url.openConnection();
            urlConn.setConnectTimeout(5000);
            urlConn.setReadTimeout(5000);
            int urlPadding = 0;
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String inputLine;
            String finalContents = "";
            while ((inputLine = reader.readLine()) != null) {
                finalContents += "\n" + inputLine.replaceAll("<code", "<pre").replaceAll("code>", "pre>");
            }
            reader.close();
            Document doc = Jsoup.parse(finalContents);
            Elements eles = doc.getElementsByTag("pre");
            ResultEntry newEntry;
            for (int j = 0; j < eles.size(); j++) {
                if (eles.get(j).text().length() >= 40) {
                    Pattern pattern = Pattern.compile("/\\*.*\\*/", Pattern.DOTALL);
                    Matcher matcher = pattern.matcher(eles.get(j).text());
                    Pattern pattern2 = Pattern.compile("^ *//.*");
                    Matcher matcher2 = pattern2.matcher(matcher.replaceAll(""));
                    Pattern pattern3 = Pattern.compile("[0-9]+: *");
                    Matcher matcher3 = pattern3.matcher(matcher2.replaceAll(""));
                    int NumOpenBraces = matcher3.replaceAll("").replaceAll("[^{]", "").length();
                    int NumClosedBraces = matcher3.replaceAll("").replaceAll("[^}]", "").length();
                    String finalString = matcher3.replaceAll("").replace(" ", " ");
                    if (NumOpenBraces == NumClosedBraces) {
                        newEntry = new ResultEntry(Convertor.FormatCode(finalString), (urlPadding++ + _url.toString()), finalString.length());
                        resultEntries.add(newEntry);
                    }
                }
            }
            setter.setResult(resultEntries);
        } catch (Exception e) {
            System.err.println(e.toString() + " thrown by following URL : " + _url);
        }
    }
