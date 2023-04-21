    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("start");
        setContentView(tv);
        AccountManager ac = new AccountManager();
        String text = "not parsed";
        Account a = null;
        URL url;
        try {
            tv.setText("try to download");
            HttpClient client = new DefaultHttpClient();
            HttpGet getMethod = new HttpGet("http://www.helloworld-network.org/markusAckermann/account.xml");
            tv.setText("start download");
            HttpResponse response = client.execute(getMethod);
            tv.setText("download executed");
            InputStream in = response.getEntity().getContent();
            tv.setText("opened");
            tv.setText("try to encrypt");
            a = ac.decryptAndParseAccount(in, "asdf");
            tv.setText("encryption done");
            if (a != null) tv.setText("encrypted, fn: " + a.getPrivateProfile().getHCard().getFn().getValue());
            tv.setText("encrypted, hcard: " + a.getPrivateProfile().getHCard().toString());
        } catch (MalformedURLException e) {
            tv.setText(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            tv.setText(e.toString());
            e.printStackTrace();
        }
    }
