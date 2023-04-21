    public static boolean accessURL(URL url, String[][] parameters) {
        if (parameters != null && parameters.length > 0 && parameters[0].length != 2) {
            System.err.println("Parameter Variable Format Error. (Must Be String[n][2])");
            return false;
        }
        if (url != null) {
            java.net.HttpURLConnection http;
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < parameters.length; i++) {
                if (i > 0) {
                    sb.append("&");
                }
                sb.append(parameters[i][0] + "=");
                sb.append(parameters[i][1]);
            }
            String formData = sb.toString();
            try {
                http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
                http.setDoOutput(true);
                http.setDoInput(true);
                PrintWriter pout = new PrintWriter(new OutputStreamWriter(http.getOutputStream(), "8859_1"), true);
                pout.print(formData);
                pout.flush();
                if (http.getResponseCode() == 200) {
                    boolean ok = true;
                    if (DEBUG) {
                        System.out.println("HTTP Response: " + http.getResponseMessage());
                        System.out.println("Content:  ");
                    }
                    InputStream is = http.getInputStream();
                    BufferedReader rdr = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = rdr.readLine()) != null) {
                        if (DEBUG) {
                            System.out.println(line);
                        }
                        if (line.compareTo("FAIL") == 0) {
                            ok = false;
                            System.err.println("Request to save applet data failed. (Possible Missing Parameter)");
                        }
                        ;
                    }
                    http.disconnect();
                    return ok;
                }
                System.err.println("HTTP Error: " + http.getResponseCode());
                System.err.println("HTTP Response: " + http.getResponseMessage());
                http.disconnect();
            } catch (IOException ioe) {
                System.err.println("Cannot connect to URL: " + url);
                return false;
            }
        }
        return false;
    }
