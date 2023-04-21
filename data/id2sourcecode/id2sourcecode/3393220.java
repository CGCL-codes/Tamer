    private void getDevices() {
        SharedPreferences pref = getSharedPreferences(Preference.TAIYAKI_PREF, MODE_PRIVATE);
        String taiyaki = pref.getString(TAIYAKI_URL, null);
        if (taiyaki == null) {
            return;
        }
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(taiyaki + TAIYAKI_DEVICES);
        try {
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                String res = EntityUtils.toString(response.getEntity());
                setListAdapter(new JSONArrayAdapter(this, android.R.layout.simple_list_item_1, new JSONArray(res), "name"));
            }
        } catch (ClientProtocolException e) {
            new AlertDialog.Builder(this).setMessage(e.getMessage()).show();
        } catch (IOException e) {
            new AlertDialog.Builder(this).setMessage(e.getMessage()).show();
        } catch (JSONException e) {
            new AlertDialog.Builder(this).setMessage(e.getMessage()).show();
        }
    }
