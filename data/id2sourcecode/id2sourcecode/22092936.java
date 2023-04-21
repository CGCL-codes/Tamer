    public String getCompanyName(String symbol) {
        if (!connected) return null;
        String companyName;
        companyName = (String) symbolToName.get(symbol);
        if (companyName == null) {
            ProgressDialog p = ProgressDialogManager.getProgressDialog();
            p.setNote("Retrieving stock name");
            boolean symbolFound = false;
            try {
                URL url = new URL(PROTOCOL, HOST, "/sanford/Members/MarketInfo/MarketWatch.asp");
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Cookie", cookie);
                OutputStream os = connection.getOutputStream();
                PrintWriter writer = new PrintWriter(os);
                writer.print("Code=" + symbol + "&" + "type=Basic");
                writer.close();
                InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    if (line.indexOf("<p><font face") != -1) {
                        Pattern pat = Pattern.compile("<[^>]*>");
                        Matcher m = pat.matcher(line);
                        companyName = m.replaceAll("");
                        pat = Pattern.compile("  ");
                        m = pat.matcher(companyName);
                        companyName = m.replaceAll("");
                        symbolToName.put(symbol, companyName);
                        break;
                    }
                }
                reader.close();
            } catch (java.io.IOException io) {
                DesktopManager.showErrorMessage("Error talking to Sanford");
            }
            ProgressDialogManager.closeProgressDialog();
        }
        return companyName;
    }
