    public ArrayList getPhoneNumbers() {
        if (numbers == null) {
            String results = "";
            try {
                URL url = new URL(VonageAccount.VONAGE_REST_GET_NUMBERS);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                Writer writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write("username=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8"));
                writer.close();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                results = reader.readLine();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] allNumbers = results.split(",");
            numbers = new ArrayList(allNumbers.length);
            for (int i = 0; i < allNumbers.length; i++) {
                try {
                    numbers.add(new VonagePhoneNumber(this, allNumbers[i]));
                } catch (InvalidPhoneNumberException e) {
                }
            }
        }
        return numbers;
    }
