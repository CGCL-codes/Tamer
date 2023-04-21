            @Override
            public void run() {
                try {
                    final HttpClient httpClient = new DefaultHttpClient();
                    final HttpPost httpPost = new HttpPost(LevelStatsDBConnector.this.mSubmitURL);
                    final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                    nameValuePairs.add(new BasicNameValuePair("level_id", String.valueOf(pLevelID)));
                    nameValuePairs.add(new BasicNameValuePair("solved", (pSolved) ? "1" : "0"));
                    nameValuePairs.add(new BasicNameValuePair("secondsplayed", String.valueOf(pSecondsElapsed)));
                    nameValuePairs.add(new BasicNameValuePair("player_id", String.valueOf(LevelStatsDBConnector.this.mPlayerID)));
                    nameValuePairs.add(new BasicNameValuePair("secret", String.valueOf(LevelStatsDBConnector.this.mSecret)));
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    final HttpResponse httpResponse = httpClient.execute(httpPost);
                    final int statusCode = httpResponse.getStatusLine().getStatusCode();
                    if (statusCode == HttpStatus.SC_OK) {
                        final String response = StreamUtils.readFully(httpResponse.getEntity().getContent());
                        if (response.equals("<success/>")) {
                            if (pCallback != null) {
                                pCallback.onCallback(true);
                            }
                        } else {
                            if (pCallback != null) {
                                pCallback.onCallback(false);
                            }
                        }
                    } else {
                        if (pCallback != null) {
                            pCallback.onCallback(false);
                        }
                    }
                } catch (final IOException e) {
                    Debug.e(e);
                    if (pCallback != null) {
                        pCallback.onCallback(false);
                    }
                }
            }
