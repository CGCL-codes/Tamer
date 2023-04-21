    public void loadSratimCookie() {
        if (!cookieHeader.equals("")) return;
        try {
            FileReader cookieFile = new FileReader("sratim.cookie");
            BufferedReader in = new BufferedReader(cookieFile);
            cookieHeader = in.readLine();
            in.close();
        } catch (Exception e) {
        }
        if (!cookieHeader.equals("")) {
            try {
                URL url = new URL("http://www.sratim.co.il/");
                HttpURLConnection connection = (HttpURLConnection) (url.openConnection());
                connection.setRequestProperty("Cookie", cookieHeader);
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                }
                rd.close();
                String xml = response.toString();
                if (xml.indexOf("logout=1") != -1) {
                    logger.finest("Sratim Subtitles Cookies Valid");
                    return;
                }
            } catch (Exception e) {
                logger.severe("Error : " + e.getMessage());
                return;
            }
            logger.severe("Sratim Cookie Use Failed - Creating new session and jpg files");
            cookieHeader = "";
            File dcookieFile = new File("sratim.cookie");
            dcookieFile.delete();
        }
        try {
            FileReader sessionFile = new FileReader("sratim.session");
            BufferedReader in = new BufferedReader(sessionFile);
            cookieHeader = in.readLine();
            in.close();
        } catch (Exception e) {
        }
        if (!cookieHeader.equals("")) {
            try {
                logger.finest("cookieHeader: " + cookieHeader);
                String post;
                post = "Username=" + login + "&Password=" + pass + "&VerificationCode=" + code + "&Referrer=%2Fdefault.aspx%3F";
                logger.finest("post: " + post);
                URL url = new URL("http://www.sratim.co.il/users/login.aspx");
                HttpURLConnection connection = (HttpURLConnection) (url.openConnection());
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length", "" + Integer.toString(post.getBytes().length));
                connection.setRequestProperty("Accept-Language", "en-us,en;q=0.5");
                connection.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
                connection.setRequestProperty("Referer", "http://www.sratim.co.il/users/login.aspx");
                connection.setRequestProperty("Cookie", cookieHeader);
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setInstanceFollowRedirects(false);
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(post);
                wr.flush();
                wr.close();
                cookieHeader = "";
                for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
                    if ("Set-Cookie".equals(header.getKey())) {
                        for (String rcookieHeader : header.getValue()) {
                            String[] cookieElements = rcookieHeader.split(" *; *");
                            if (cookieElements.length >= 1) {
                                String[] firstElem = cookieElements[0].split(" *= *");
                                String cookieName = firstElem[0];
                                String cookieValue = firstElem.length > 1 ? firstElem[1] : null;
                                logger.finest("cookieName:" + cookieName);
                                logger.finest("cookieValue:" + cookieValue);
                                if (!cookieHeader.equals("")) cookieHeader = cookieHeader + "; ";
                                cookieHeader = cookieHeader + cookieName + "=" + cookieValue;
                            }
                        }
                    }
                }
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                }
                rd.close();
                String xml = response.toString();
                if (xml.indexOf("<h2>Object moved to <a href=\"%2fdefault.aspx%3f\">here</a>.</h2>") != -1) {
                    FileWriter cookieFile = new FileWriter("sratim.cookie");
                    cookieFile.write(cookieHeader);
                    cookieFile.close();
                    File dimageFile = new File("sratim.jpg");
                    dimageFile.delete();
                    File dsessionFile = new File("sratim.session");
                    dsessionFile.delete();
                    return;
                }
                logger.severe("Sratim Login Failed - Creating new session and jpg files");
            } catch (Exception e) {
                logger.severe("Error : " + e.getMessage());
                return;
            }
        }
        try {
            URL url = new URL("http://www.sratim.co.il/users/login.aspx");
            HttpURLConnection connection = (HttpURLConnection) (url.openConnection());
            cookieHeader = "";
            for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
                if ("Set-Cookie".equals(header.getKey())) {
                    for (String rcookieHeader : header.getValue()) {
                        String[] cookieElements = rcookieHeader.split(" *; *");
                        if (cookieElements.length >= 1) {
                            String[] firstElem = cookieElements[0].split(" *= *");
                            String cookieName = firstElem[0];
                            String cookieValue = firstElem.length > 1 ? firstElem[1] : null;
                            logger.finest("cookieName:" + cookieName);
                            logger.finest("cookieValue:" + cookieValue);
                            if (!cookieHeader.equals("")) cookieHeader = cookieHeader + "; ";
                            cookieHeader = cookieHeader + cookieName + "=" + cookieValue;
                        }
                    }
                }
            }
            FileWriter sessionFile = new FileWriter("sratim.session");
            sessionFile.write(cookieHeader);
            sessionFile.close();
            url = new URL("http://www.sratim.co.il/verificationimage.aspx");
            connection = (HttpURLConnection) (url.openConnection());
            connection.setRequestProperty("Cookie", cookieHeader);
            File imageFile = new File("sratim.jpg");
            FileTools.copy(connection.getInputStream(), new FileOutputStream(imageFile));
            logger.severe("#############################################################################");
            logger.severe("### Open \"sratim.jpg\" file, and write the code in the sratim.code field ###");
            logger.severe("#############################################################################");
            System.exit(0);
        } catch (Exception e) {
            logger.severe("Error : " + e.getMessage());
            return;
        }
    }
