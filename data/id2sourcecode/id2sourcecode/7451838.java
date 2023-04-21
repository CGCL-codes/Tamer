    public boolean book(String id, String in, String out, String name, String surname) {
        String bookUrl = "http://labos.diee.unica.it/hotel/Sms/create.htm";
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
        Date dateIn = df.parse(in, new ParsePosition(0));
        Date dateOut = df.parse(out, new ParsePosition(0));
        in = in.replace("/", ".");
        int days = (int) ((dateOut.getTime() - dateIn.getTime()) / (1000 * 24 * 60 * 60));
        String parameters = "11796 book owner" + id + " " + "password 204 " + in + " " + days + " " + name + " " + surname;
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("TextMessage", parameters));
        client = createHttpClient();
        HttpPost httppost = new HttpPost(bookUrl);
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            HttpResponse rsp = client.execute(httppost);
            int result = rsp.getStatusLine().getStatusCode();
            Log.d(TAG, "I'm booking @: " + id + "; result code is: " + result);
            if (result == 200) return true;
            return false;
        } catch (Exception e) {
            return false;
        }
    }
