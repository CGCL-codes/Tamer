    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case FROM_PREF:
                if (resultCode == RESULT_OK) {
                    getDevices();
                }
                break;
            case FROM_LIST:
            default:
                switch(resultCode) {
                    case RESULT_CANCELED:
                        break;
                    case RESULT_OK:
                    default:
                        SharedPreferences pref = getSharedPreferences(Preference.TAIYAKI_PREF, MODE_PRIVATE);
                        String taiyaki = pref.getString(TAIYAKI_URL, null);
                        if (taiyaki == null) {
                            return;
                        }
                        HttpClient client = new DefaultHttpClient();
                        HttpGet get = new HttpGet(taiyaki + TAIYAKI_REST + data.getDataString());
                        try {
                            HttpResponse response = client.execute(get);
                            if (response.getStatusLine().getStatusCode() == 200) {
                                String res = EntityUtils.toString(response.getEntity());
                                JSONObject result = new JSONObject(res);
                                if (!result.getBoolean("success")) {
                                    new AlertDialog.Builder(this).setMessage(result.getString("result")).show();
                                    return;
                                }
                                Intent sended = new Intent(X10Receiver.ACTION_DIRECT);
                                sended.putExtra(X10Receiver.RESULT, res);
                                sendBroadcast(sended);
                            }
                        } catch (ClientProtocolException e) {
                            new AlertDialog.Builder(this).setMessage(e.getMessage()).show();
                        } catch (IOException e) {
                            new AlertDialog.Builder(this).setMessage(e.getMessage()).show();
                        } catch (JSONException e) {
                            new AlertDialog.Builder(this).setMessage(e.getMessage()).show();
                        }
                }
        }
    }
