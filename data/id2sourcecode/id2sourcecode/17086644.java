    public void getData() {
        new Thread(new Runnable() {

            public void run() {
                InputStream content = null;
                boolean loggedin = false;
                try {
                    Log.d("http", "Start get");
                    httpget.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
                    Log.d("http", "get response");
                    HttpResponse response = httpclient.execute(httpget);
                    content = response.getEntity().getContent();
                    Log.d("http", "read out response");
                    BufferedReader rd = new BufferedReader(new InputStreamReader(content), 4096);
                    String line;
                    while ((line = rd.readLine()) != null) {
                        Log.d("getData()", line);
                        if (line.contains("<a href=\"/usercp/page%3Agulden\">\"")) {
                            getGulden(line.toCharArray());
                            loggedin = true;
                            break;
                        }
                    }
                    rd.close();
                    if (!loggedin) {
                        loggedOut(settings.getString("username", "none"));
                    }
                } catch (Exception e) {
                    Log.e("getData()", e.getCause().toString());
                }
                guldenAdapter.notifyDataSetChanged();
            }
        }).start();
    }
