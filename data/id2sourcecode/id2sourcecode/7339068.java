    public void testAuthorization() {
        try {
            Authenticator.setDefault(new Authenticator() {

                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("sbv@lakeside.dk", "sbv".toCharArray());
                }
            });
            URL url = new URL("http://netl.dk/xplanner/");
            URLConnection urlConnection = url.openConnection();
            System.out.println(urlConnection.getHeaderFields());
            urlConnection.connect();
            InputStreamReader ReadIn = new InputStreamReader(urlConnection.getInputStream());
            BufferedReader BufData = new BufferedReader(ReadIn);
            String UrlData = null;
            while ((UrlData = BufData.readLine()) != null) {
                System.out.println(UrlData);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
