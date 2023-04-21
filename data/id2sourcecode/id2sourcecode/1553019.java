    public void submit(AjaxRequestTarget target) {
        String line;
        HttpURLConnection con = null;
        try {
            String data = URLEncoder.encode("accountType", "UTF-8") + "=" + URLEncoder.encode("HOSTED_OR_GOOGLE", "UTF-8");
            data += "&" + URLEncoder.encode("Email", "UTF-8") + "=" + URLEncoder.encode(email.getValue(), "UTF-8");
            data += "&" + URLEncoder.encode("Passwd", "UTF-8") + "=" + URLEncoder.encode(password.getValue(), "UTF-8");
            data += "&" + URLEncoder.encode("service", "UTF-8") + "=" + URLEncoder.encode("cl", "UTF-8");
            data += "&" + URLEncoder.encode("source", "UTF-8") + "=" + URLEncoder.encode("GrahamRhodes-StaffPM-0.1-SNAPSHOT", "UTF-8");
            if (captchaRequired) {
                data += "&" + URLEncoder.encode("logintoken", "UTF-8") + "=" + URLEncoder.encode(captchaToken, "UTF-8");
                data += "&" + URLEncoder.encode("logincaptcha", "UTF-8") + "=" + URLEncoder.encode(captcha.getValue(), "UTF-8");
            }
            captchaToken = "";
            captchaRequired = false;
            captchaContainer.setVisible(false);
            URL url = new URL("https://www.google.com/accounts/ClientLogin");
            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((line = rd.readLine()) != null) {
                if (line.startsWith("Auth=")) {
                    User currUser = userService.getCurrentUser();
                    currUser.setGcalAuth(line.substring(5));
                    userService.addOrUpdateUser(currUser);
                }
            }
            wr.close();
            rd.close();
        } catch (IOException e) {
            String error = "";
            BufferedReader rd;
            try {
                rd = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                while ((line = rd.readLine()) != null) {
                    if (line.startsWith("CaptchaToken=")) captchaToken = line.substring(13);
                    if (line.startsWith("CaptchaUrl=")) captchaUrl = line.substring(11);
                    if (line.startsWith("Url=")) error = line.substring(6);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (!captchaToken.isEmpty()) {
                captchaRequired = true;
                captchaImg.add(new AttributeModifier("src", true, new AbstractReadOnlyModel<String>() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public final String getObject() {
                        return "http://www.google.com/accounts/" + captchaUrl;
                    }
                }));
                form.add(new AttributeModifier("style", true, new AbstractReadOnlyModel<String>() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public final String getObject() {
                        return "width: 350px; height: 200px;";
                    }
                }));
                captchaContainer.setVisible(true);
            } else {
                error(error);
            }
            target.addComponent(form);
        } catch (Exception e) {
            log.error("Exception sending details to google.", e);
        }
    }
