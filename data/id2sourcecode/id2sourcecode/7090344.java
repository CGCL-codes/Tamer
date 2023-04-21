    public void initiateCall(PhoneNumber otherPhoneNumber) throws Click2CallException {
        String results = "";
        try {
            URL url = new URL(VonageAccount.VONAGE_REST_MAKE_CALL);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            Writer writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write("username=" + URLEncoder.encode(account.getUsername(), "UTF-8") + "&password=" + URLEncoder.encode(account.getPassword(), "UTF-8") + "&fromnumber=" + URLEncoder.encode(number.getNumber(), "UTF-8") + "&tonumber=" + URLEncoder.encode(otherPhoneNumber.getNumber(), "UTF-8"));
            writer.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            results = reader.readLine();
        } catch (MalformedURLException e) {
            throw new Click2CallException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new Click2CallException(e.getMessage());
        } catch (IOException e) {
            throw new Click2CallException(e.getMessage());
        }
        if (!results.substring(0, 3).equals("000")) {
            throw new Click2CallException(results);
        }
    }
