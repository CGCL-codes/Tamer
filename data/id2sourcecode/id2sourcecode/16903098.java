    public void login(String pCaptchaAnswer, String pCaptchaToken) throws IOException {
        String data = URLEncoder.encode("accountType", enc) + "=" + URLEncoder.encode(account_type, enc);
        data += "&" + URLEncoder.encode("Email", enc) + "=" + URLEncoder.encode(user, enc);
        data += "&" + URLEncoder.encode("Passwd", enc) + "=" + URLEncoder.encode(pass, enc);
        data += "&" + URLEncoder.encode("service", enc) + "=" + URLEncoder.encode(SERVICE, enc);
        data += "&" + URLEncoder.encode("source", enc) + "=" + URLEncoder.encode(source, enc);
        if (pCaptchaAnswer != null && pCaptchaToken != null) {
            data += "&" + URLEncoder.encode("logintoken", enc) + "=" + URLEncoder.encode(pCaptchaToken, enc);
            data += "&" + URLEncoder.encode("logincaptcha", enc) + "=" + URLEncoder.encode(pCaptchaAnswer, enc);
        }
        URL url = new URL(loginURLString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-agent", USER_AGENT);
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();
        conn.connect();
        int responseCode = conn.getResponseCode();
        if (PRINT_TO_CONSOLE) System.out.println(loginURLString + " - " + conn.getResponseMessage());
        InputStream is;
        if (responseCode == 200) {
            is = conn.getInputStream();
        } else {
            is = conn.getErrorStream();
        }
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader rd = new BufferedReader(isr);
        String line;
        String completelineDebug = "";
        String lErrorString = "Unknown Connection Error.";
        while ((line = rd.readLine()) != null) {
            completelineDebug += line + "\n";
            if (line.contains("Auth=")) {
                this.authToken = line.split("=", 2)[1].trim();
                if (PRINT_TO_CONSOLE) {
                    System.out.println("Logged in to Google - Auth token received");
                }
            } else if (line.contains("Error=")) {
                lErrorString = line.split("=", 2)[1].trim();
                error = ERROR_CODE.valueOf(lErrorString);
                if (PRINT_TO_CONSOLE) System.out.println("Login error - " + lErrorString);
            }
            if (line.contains("CaptchaToken=")) {
                captchaToken = line.split("=", 2)[1].trim();
            }
            if (line.contains("CaptchaUrl=")) {
                captchaUrl = "http://www.google.com/accounts/" + line.split("=", 2)[1].trim();
            }
            if (line.contains("Url=")) {
                captchaUrl2 = line.split("=", 2)[1].trim();
            }
        }
        wr.close();
        rd.close();
        if (this.authToken == null) {
            AuthenticationException.throwProperException(error, captchaToken, captchaUrl);
        }
        String response = this.getRawPhonesInfo();
        int phoneIndex = response.indexOf("gc-user-number-value\">");
        this.phoneNumber = response.substring(phoneIndex + 22, phoneIndex + 36);
        this.phoneNumber = this.phoneNumber.replaceAll("[^a-zA-Z0-9]", "");
        if (this.phoneNumber.indexOf("+") == -1) {
            this.phoneNumber = "+1" + this.phoneNumber;
        }
    }
