            @Override
            protected Boolean doInBackground(Void... params) {
                BufferedReader reader = null;
                Boolean ret = Boolean.FALSE;
                String osbUrl = "http://openstreetbugs.schokokeks.org/api/0.1/getBugs?b=" + (pos.getLatitude() - 0.25f) + "&t=" + (pos.getLatitude() + 0.25f) + "&l=" + (pos.getLongitude() - 0.25f) + "&r=" + (pos.getLongitude() + 0.25f);
                LogIt.d("Url is: " + osbUrl);
                if (MapsForgeActivity.isOnline(activity)) {
                    ret = Boolean.TRUE;
                    try {
                        URL url = new URL(osbUrl);
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                        osbugs.clear();
                        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                            IDataBug b = extractBug(line);
                            if (b != null) {
                                osbugs.add(b);
                            }
                        }
                        LogIt.d("Found " + osbugs.size() + " bugs!");
                        activity.fillBugs();
                    } catch (IOException e) {
                        ret = Boolean.FALSE;
                        LogIt.e("Download error: " + e.getMessage());
                    } finally {
                        try {
                            if (reader != null) {
                                reader.close();
                            }
                        } catch (IOException e) {
                        }
                    }
                }
                return ret;
            }
