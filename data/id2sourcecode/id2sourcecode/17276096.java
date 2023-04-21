    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        HttpGet request = new HttpGet(SERVICE_URI + "/json/getallpersons");
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
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            stream.close();
            theString = builder.toString();
            JSONObject json = new JSONObject(theString);
            Log.i("_GetPerson_", "<jsonobject>\n" + json.toString() + "\n</jsonobject>");
            JSONArray nameArray = json.getJSONArray("getJsonPersonsResult");
            for (int i = 0; i < nameArray.length(); i++) {
                Log.i("_GetPerson_", "<User" + i + ">" + nameArray.getJSONObject(i).getString("User") + "</User" + i + ">\n");
                Log.i("_GetPerson_", "<Name" + i + ">" + nameArray.getJSONObject(i).getString("Name") + "</Name" + i + ">\n");
                Log.i("_GetPerson_", "<Email" + i + ">" + nameArray.getJSONObject(i).getString("Email") + "</Email" + i + ">\n");
                Log.i("_GetPerson_", "<Password" + i + ">" + nameArray.getJSONObject(i).getString("Password") + "</Password" + i + ">\n");
                tempString = nameArray.getJSONObject(i).getString("User") + " " + nameArray.getJSONObject(i).getString("Name") + "\n" + nameArray.getJSONObject(i).getString("Email") + "\n" + nameArray.getJSONObject(i).getString("Password");
                vectorOfStrings.add(new String(tempString));
            }
            int orderCount = vectorOfStrings.size();
            String[] orderTimeStamps = new String[orderCount];
            vectorOfStrings.copyInto(orderTimeStamps);
            setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, orderTimeStamps));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(this, theString + "\n", Toast.LENGTH_LONG).show();
    }
