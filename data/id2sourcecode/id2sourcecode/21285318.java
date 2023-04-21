    public void putContact(String email, Contact contact) throws IOException {
        contact.setEmail(email);
        URL url = new URL(hostUrl + "/service/home/" + user + "/contacts?fmt=csv");
        authenticate();
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        urlConn.setRequestMethod("PUT");
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());
        printout.writeBytes(contact.toCSV());
        printout.flush();
        printout.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
        String str;
        while (null != ((str = input.readLine()))) {
            log.debug(str);
        }
        input.close();
    }
