    private void parseIPFromHTMLPage(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.addRequestProperty("User-Agent", "Mozilla/4.76");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        int positionPunktEins, positionPunktZwei, positionPunktDrei;
        while ((inputLine = in.readLine()) != null) {
            positionPunktDrei = 0;
            positionPunktZwei = 0;
            positionPunktEins = 0;
            do {
                positionPunktEins = inputLine.indexOf('.', positionPunktEins);
                if (positionPunktEins > 0) positionPunktZwei = inputLine.indexOf('.', positionPunktEins + 1);
                if (positionPunktZwei > 0) positionPunktDrei = inputLine.indexOf('.', positionPunktZwei + 1);
                if ((positionPunktDrei > 0) && ((positionPunktDrei - positionPunktEins) <= 7)) {
                    int left = positionPunktEins - 1;
                    while ((left >= 0) && ('0' <= inputLine.charAt(left)) && (inputLine.charAt(left) <= '9')) left--;
                    left++;
                    int right = positionPunktDrei + 1;
                    while ((right < inputLine.length()) && ('0' <= inputLine.charAt(right)) && (inputLine.charAt(right) <= '9')) right++;
                    String adressString = inputLine.substring(left, right).trim();
                    int oktettZaehler = 0;
                    StringTokenizer oT = new StringTokenizer(adressString, ".");
                    byte[] oktette = new byte[4];
                    try {
                        while (oT.hasMoreTokens() && (oktettZaehler < 4)) {
                            String okt = oT.nextToken().trim();
                            Integer i = new Integer(okt);
                            oktette[oktettZaehler] = (byte) i.intValue();
                            oktettZaehler++;
                        }
                    } catch (NumberFormatException e) {
                    }
                    if (oktettZaehler == 4) try {
                        adresse = InetAddress.getByName(adressString);
                    } catch (UnknownHostException e) {
                    }
                }
                positionPunktEins = positionPunktEins + 1;
            } while (positionPunktEins != 0);
        }
        in.close();
    }
