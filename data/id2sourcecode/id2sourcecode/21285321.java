    public void setCalendar(Calendar newCalendar) throws IOException {
        URL url = new URL(hostUrl + "/service/home/" + user + "/calendar?fmt=ics");
        Authenticator.setDefault(new Authenticator() {

            public PasswordAuthentication getPasswordAuthentication() {
                return (new PasswordAuthentication(user, password.toCharArray()));
            }
        });
        URLConnection urlConn = url.openConnection();
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());
        printout.writeBytes(newCalendar.toString());
        printout.flush();
        printout.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
        String str;
        while (null != (str = input.readLine())) {
            log.debug(str);
        }
        input.close();
    }
