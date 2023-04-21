    public static void main(String[] args) {
        System.out.println("RyanRequest");
        HttpClient client = new DefaultHttpClient();
        try {
            List<String> params = new ArrayList();
            Calendar calendar = Calendar.getInstance();
            int day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);
            int max_days_of_month = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            int month_for_search = 6;
            List<String> fromList = new ArrayList<String>();
            fromList.add("LPA");
            List<String> toList = new ArrayList<String>();
            toList.add("MAD");
            toList.add("STN");
            toList.add("OPO");
            toList.add("PSA");
            for (int i = 0; i < toList.size(); i++) {
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);
                for (int iterator = 0; iterator < month_for_search; iterator++) {
                    month++;
                    if (month == 13) {
                        month = 1;
                        year++;
                    }
                    day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
                    for (; day_of_month <= max_days_of_month; day_of_month += 7) {
                        params.clear();
                        params.add(Constants.ADULT + Constants.EQ + "1");
                        params.add(Constants.BALEARIC_USER_ANSWER + Constants.EQ + "YES");
                        params.add(Constants.CHILD + Constants.EQ + "0");
                        params.add(Constants.INFANT + Constants.EQ + "0");
                        params.add(Constants.SEARCH_BY + Constants.EQ + "columenView");
                        params.add(Constants.ACCEPT_TERMS + Constants.EQ + "yes");
                        params.add(Constants.CULTURE + Constants.EQ + "es");
                        params.add(Constants.DATE1 + Constants.EQ + year + normalize(month) + normalize(day_of_month));
                        params.add(Constants.DATE2 + Constants.EQ + year + normalize(month) + normalize((day_of_month)));
                        params.add(Constants.LANGUAGE + Constants.EQ + "");
                        params.add(Constants.M1 + Constants.EQ + year + normalize(month) + normalize(day_of_month) + "a" + fromList.get(0) + toList.get(i));
                        params.add(Constants.M1DO + Constants.EQ + "0");
                        params.add(Constants.M1DP + Constants.EQ + "0");
                        params.add(Constants.M2 + Constants.EQ + year + normalize(month) + normalize((day_of_month)) + toList.get(i) + "a" + fromList.get(0));
                        params.add(Constants.M2DO + Constants.EQ + "0");
                        params.add(Constants.M2DP + Constants.EQ + "0");
                        params.add(Constants.MODE + Constants.EQ + "0");
                        params.add(Constants.MODULE + Constants.EQ + "SB");
                        params.add(Constants.NOM + Constants.EQ + "2");
                        params.add(Constants.OP + Constants.EQ + "");
                        params.add(Constants.PM + Constants.EQ + "0");
                        params.add(Constants.PT + Constants.EQ + "1ADULT");
                        params.add(Constants.PAGE + Constants.EQ + "SELECT");
                        params.add(Constants.RP + Constants.EQ + "");
                        params.add(Constants.SECTOR1_D + Constants.EQ + toList.get(i));
                        params.add(Constants.SECTOR1_O + Constants.EQ + fromList.get(0));
                        params.add(Constants.SECTOR_1_D + Constants.EQ + normalize(day_of_month));
                        params.add(Constants.SECTOR_1_M + Constants.EQ + normalize(month) + year);
                        params.add(Constants.SECTOR_2_D + Constants.EQ + normalize((day_of_month)));
                        params.add(Constants.SECTOR_2_M + Constants.EQ + normalize(month) + year);
                        params.add(Constants.TC + Constants.EQ + "1");
                        params.add(Constants.TRAVEL_TYPE + Constants.EQ + "on");
                        day_of_month++;
                        String parameters = new String();
                        Iterator<String> paramsIterator = params.iterator();
                        while (paramsIterator.hasNext()) {
                            String object = (String) paramsIterator.next();
                            parameters += object + "&";
                        }
                        parameters = parameters.substring(0, parameters.lastIndexOf("&"));
                        System.out.println("parameters: " + parameters);
                        String url = "http://www.bookryanair.com/SkySales/FRSearchRedirect.aspx?culture=es-es&lc=es-es?";
                        HttpPost httpPost = new HttpPost(url + parameters);
                        HttpResponse response = client.execute(httpPost);
                        String responseBody = EntityUtils.toString(response.getEntity());
                        HttpGet httpGetAfterResponse = new HttpGet("http://www.bookryanair.com/SkySales/FRSelect.aspx");
                        HttpResponse responseGet = client.execute(httpGetAfterResponse);
                        String responseGetBody = EntityUtils.toString(responseGet.getEntity());
                        Book book = new Book();
                        BookParser bookParser = new BookParser();
                        book = bookParser.parse(responseGetBody);
                        Logger.write("=====================================================================================");
                        Logger.write(fromList.get(0));
                        Logger.write(toList.get(i));
                        Logger.write("=====================================================================================");
                        Item itemFrom = new Item();
                        itemFrom = book.getFromItem();
                        List<String> itemsFrom = itemFrom.getWeekAvailable();
                        for (int j = 0; j < itemFrom.getWeekAvailable().size(); j++) {
                            Logger.write("IDA - " + itemsFrom.get(j));
                        }
                        Item itemTo = new Item();
                        itemTo = book.getToItem();
                        List<String> itemsTo = itemTo.getWeekAvailable();
                        for (int k = 0; k < itemTo.getWeekAvailable().size(); k++) {
                            Logger.write("VUELTA - " + itemsTo.get(k));
                        }
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
