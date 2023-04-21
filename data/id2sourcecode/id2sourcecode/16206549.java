    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsactivity);
        text = (TextView) findViewById(R.id.NewsActivityText);
        image = (ImageView) findViewById(R.id.NewsActivityImage);
        Intent intent = this.getIntent();
        String u = intent.getStringExtra("newsurl");
        URL url = null;
        try {
            url = new URL(u);
            String jsonText = getJSONScoreStringFromNet(url);
            JSONObject j1 = new JSONObject(jsonText);
            URL urlText = new URL(j1.getString("actualpicurl"));
            String actualText = j1.getString("actualtext");
            text.setText(actualText);
            InputStream ins = null;
            try {
                ins = urlText.openStream();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap b = BitmapFactory.decodeStream(new FlushedInputStream(ins));
            image.setImageBitmap(b);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
