    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        HttpPost request = new HttpPost(SERVICE_URI + "/json/adduser");
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        String not = new String(" ");
        try {
            JSONStringer vehicle = new JSONStringer().object().key("rData").object().key("details").value("bar|bob|b@h.us|why").endObject().endObject();
            StringEntity entity = new StringEntity(vehicle.toString());
            Toast.makeText(this, vehicle.toString() + "\n", Toast.LENGTH_LONG).show();
            request.setEntity(entity);
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(request);
            Toast.makeText(this, response.getStatusLine().getStatusCode() + "\n", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            not = "NOT ";
        }
        Toast.makeText(this, not + " OK ! " + "\n", Toast.LENGTH_LONG).show();
    }
