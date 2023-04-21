    @Override
    protected MarketsAdapter doInBackground(MarketsAdapter... params) {
        try {
            URL url = new URL("http://merkat.sourceforge.net/webservice/markets.php");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(false);
            conn.setDefaultUseCaches(true);
            conn.setUseCaches(true);
            conn.setConnectTimeout(5000);
            InputStream is = conn.getInputStream();
            Log.v(Main.TAG, "loading from " + url);
            String jsonData = readStream(is);
            Log.v(Main.TAG, "loaded data " + jsonData);
            JSONObject object = (JSONObject) new JSONTokener(jsonData).nextValue();
            mJSONList = object.getJSONArray("markets");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params[0];
    }
