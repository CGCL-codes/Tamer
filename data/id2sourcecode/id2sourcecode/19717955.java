    private void fetch() {
        try {
            URL url = new URL("https://fx2.oanda.com/mod_perl/user/interestrates.pl");
            URLConnection connection = url.openConnection();
            ((HttpURLConnection) connection).setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            Calendar c = Calendar.getInstance();
            String content = "month=" + Integer.toString(c.get(Calendar.MONTH + 1)) + "&day=" + Integer.toString(c.get(Calendar.DAY_OF_MONTH)) + "&year=" + Integer.toString(c.get(Calendar.YEAR)) + "&startdate=&enddate=" + "&currency=AUD" + "&currency=GBP" + "&currency=CAD" + "&currency=CNY" + "&currency=CZK" + "&currency=DKK" + "&currency=EUR" + "&currency=XAU" + "&currency=HKD" + "&currency=HUF" + "&currency=INR" + "&currency=JPY" + "&currency=MXN" + "&currency=TWD" + "&currency=NZD" + "&currency=NOK" + "&currency=PLN" + "&currency=SAR" + "&currency=XAG" + "&currency=SGD" + "&currency=ZAR" + "&currency=SEK" + "&currency=CHF" + "&currency=THB" + "&currency=TRY" + "&currency=USD";
            DataOutputStream printout = new DataOutputStream(connection.getOutputStream());
            printout.writeBytes(content);
            printout.flush();
            printout.close();
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader r = new BufferedReader(isr);
            String html = "";
            String line;
            while ((line = r.readLine()) != null) {
                html += line;
            }
            char chars[] = html.toCharArray();
            char last = ' ';
            String filtered = "";
            boolean intag = false;
            for (int n = 0; n < chars.length; n++) {
                if (!intag) {
                    if (chars[n] == '<') intag = true; else {
                        if (chars[n] != ' ' || last != ' ') filtered += chars[n];
                        last = chars[n];
                    }
                } else {
                    if (chars[n] == '>') intag = false;
                }
            }
            StringTokenizer tokens = new StringTokenizer(filtered);
            int tot = tokens.countTokens();
            Currency ccy = null;
            double borrowing = 0;
            double lending = 0;
            boolean error = false;
            for (int n = 0; n < tot; n++) {
                String token = tokens.nextToken();
                if (!token.startsWith("&") && n > 9 && (n - 10) % 8 < 3) {
                }
                if (!token.startsWith("&") && n > 9 && (n - 10) % 8 == 0) {
                    try {
                        ccy = Enum.valueOf(Currency.class, token);
                    } catch (Exception e) {
                        error = true;
                    }
                }
                if (!token.startsWith("&") && n > 9 && (n - 10) % 8 == 1) {
                    try {
                        borrowing = Double.parseDouble(token);
                    } catch (Exception e) {
                        error = true;
                    }
                }
                if (!token.startsWith("&") && n > 9 && (n - 10) % 8 == 2) {
                    try {
                        lending = Double.parseDouble(token);
                        if (!error) lastRates.put(ccy, new CurrencyIR(ccy, borrowing, lending));
                    } catch (Exception e) {
                        error = true;
                    }
                }
                if (!token.startsWith("&") && n > 9 && (n - 10) % 8 == 3) {
                    error = false;
                }
                if (n > 9 && (n - 10) % 8 == 3) {
                }
            }
            log.debug("Successfully retrevied oanda interest rates");
        } catch (Exception e) {
            log.warn("Error: " + e.getMessage(), e);
        }
    }
