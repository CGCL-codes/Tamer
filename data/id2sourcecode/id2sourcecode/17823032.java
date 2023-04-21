    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        HttpGet request = new HttpGet(SERVICE_URI + "/json/getproducts");
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String theString = new String("");
        try {
            HttpResponse response = httpClient.execute(request);
            HttpEntity responseEntity = response.getEntity();
            InputStream stream = responseEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            Vector<String> vectorOfStrings = new Vector<String>();
            String tempString = new String();
            String tempStringID = new String();
            String tempStringName = new String();
            String tempStringPrice = new String();
            String tempStringSymbol = new String();
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            stream.close();
            theString = builder.toString();
            JSONObject json = new JSONObject(theString);
            Log.i("_GetPerson_", "<jsonobject>\n" + json.toString() + "\n</jsonobject>");
            this.dm = new DataManipulator(this);
            JSONArray nameArray = json.getJSONArray("getProductsResult");
            for (int i = 0; i < nameArray.length(); i++) {
                tempStringID = nameArray.getJSONObject(i).getString("ID");
                tempStringName = nameArray.getJSONObject(i).getString("Name");
                tempStringPrice = nameArray.getJSONObject(i).getString("Price");
                tempStringSymbol = nameArray.getJSONObject(i).getString("Symbol");
                this.dm.insertIntoProducts(tempStringID, tempStringName, tempStringPrice, tempStringSymbol);
                tempString = nameArray.getJSONObject(i).getString("Name") + "\n" + nameArray.getJSONObject(i).getString("Price") + "\n" + nameArray.getJSONObject(i).getString("Symbol");
                vectorOfStrings.add(new String(tempString));
            }
            int orderCount = vectorOfStrings.size();
            String[] orderTimeStamps = new String[orderCount];
            vectorOfStrings.copyInto(orderTimeStamps);
            setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, orderTimeStamps));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
