    private Date readIn(Ticker ticker, String proquest, int nostories) {
        CookieManager cookiemonster = new CookieManager();
        CookieHandler.setDefault(cookiemonster);
        Boolean reallydone = false;
        int storycounter = 0;
        int pagenumber = 1;
        Boolean stemfound = false;
        Boolean totalres = false;
        double noresf = 0.0;
        int maxpage = 0;
        String urlstem = null;
        Date earliestdate = new Date(this.enddate.getTime());
        while (!reallydone && storycounter < nostories && pagenumber <= 100 && (!totalres || (pagenumber <= maxpage))) {
            try {
                String nexturl = null;
                if (pagenumber == 1) {
                    nexturl = proquest;
                } else {
                    nexturl = "http://proquest.umi.com" + urlstem + ((pagenumber - 1) * 10);
                }
                URL proquesturl = new URL(nexturl);
                BufferedReader proquestsearchreader = new BufferedReader(new InputStreamReader(proquesturl.openStream()));
                System.out.println(ticker.getSymbol() + ":" + nexturl);
                Scanner s3 = new Scanner(proquestsearchreader);
                s3.useDelimiter("[\n\r]+");
                Boolean done = false;
                while (s3.hasNext() && !done && storycounter < nostories) {
                    String next = s3.next();
                    if (next.contains("No documents found for: ")) {
                        done = true;
                        reallydone = true;
                        break;
                    }
                    if (next.contains("<td class=\"textMedium\" colspan=\"2\"><a href=\"javascript:CheckAll(true)") && stemfound) {
                        done = true;
                        break;
                    }
                    if (next.contains("<a class=\'bold\' href=\'")) {
                        try {
                            String exploreurl = next.substring(next.indexOf("href") + 6, next.indexOf('>') - 1);
                            String extractor = next.substring(next.indexOf('<') + 1);
                            String title = extractor.substring(extractor.indexOf('>') + 1, extractor.indexOf('<'));
                            String dextractor = s3.next();
                            String dextractor2 = dextractor.substring(dextractor.indexOf("</span>") + 7);
                            if (dextractor2.indexOf("</span>") != -1) {
                                dextractor2 = dextractor2.substring(dextractor2.indexOf("</span>") + 7);
                            }
                            String dextractor3 = dextractor2;
                            String datestring = null;
                            if (dextractor3.indexOf(':') != -1) {
                                datestring = dextractor3.substring(dextractor3.indexOf(':') + 2, dextractor3.indexOf('.', dextractor3.indexOf(':') + 1));
                            } else {
                                datestring = dextractor3.substring(3, dextractor3.indexOf('.', dextractor3.indexOf(',') + 1));
                            }
                            if (datestring.indexOf('.') != -1) {
                                datestring = datestring.substring(0, (datestring.indexOf('.')));
                            }
                            if (datestring.indexOf('-') != -1) {
                                datestring = datestring.substring(datestring.indexOf('-') + 1);
                            }
                            Date date = proquestdconv(datestring);
                            if (date.before(earliestdate)) {
                                earliestdate = date;
                            }
                            String content = URLExplorer(new URL("http://proquest.umi.com" + exploreurl));
                            ticker.addNews(new NewsEvent(title, content, date.getTime()));
                            storycounter++;
                        } catch (Exception e) {
                            System.out.println("Bad parse!");
                        }
                    }
                    if (!totalres && next.contains("<!--PAGECOUNT BEGIN-->")) {
                        totalres = true;
                        next = s3.next();
                        String resex = next.substring(next.indexOf("of") + 3, next.indexOf("</div>"));
                        int nores = Integer.valueOf(resex);
                        noresf = (new Integer(nores)).doubleValue();
                        double page = Math.floor(noresf / 10.0);
                        maxpage = (new Double(page)).intValue() + 1;
                    }
                    if (!stemfound && next.contains("2</a> &nbsp;")) {
                        String stemextractor = next.substring(next.indexOf("<a href=\"") + 9, next.indexOf("firstIndex="));
                        stemfound = true;
                        urlstem = stemextractor + "firstIndex=";
                        System.out.println(urlstem);
                    }
                }
                pagenumber++;
                if (s3 != null) {
                    s3.close();
                }
                if (proquestsearchreader != null) {
                    proquestsearchreader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (noresf > 1000.0) {
            return earliestdate;
        } else {
            return startdate;
        }
    }
