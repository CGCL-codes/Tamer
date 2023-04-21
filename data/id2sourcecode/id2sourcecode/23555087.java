    private void showJoker() {
        if (Global.Jokers.isEmpty()) {
            try {
                URL url = new URL("http://www.gcjoker.de/cachebox.php?md5=" + Config.GetString("GcJoker") + "&wpt=" + GlobalCore.SelectedCache().GcCode);
                URLConnection urlConnection = url.openConnection();
                HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpConnection.getInputStream();
                    if (inputStream != null) {
                        String line;
                        try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                            while ((line = reader.readLine()) != null) {
                                String[] s = line.split(";", 7);
                                try {
                                    if (s[0].equals("2")) {
                                        MessageBox.Show(s[1], null);
                                        break;
                                    }
                                    if (s[0].equals("1")) {
                                        MessageBox.Show(s[1], null);
                                    }
                                    if (s[0].equals("0")) {
                                        Global.Jokers.AddJoker(s[1], s[2], s[3], s[4], s[5], s[6]);
                                    }
                                } catch (Exception exc) {
                                    Logger.Error("main.initialBtnInfoContextMenu()", "HTTP response Jokers", exc);
                                    return;
                                }
                            }
                            if (Global.Jokers.isEmpty()) {
                                MessageBox.Show("Keine Joker bekannt", null);
                            } else {
                                Logger.General("Open JokerView...");
                                showView(12);
                            }
                        } finally {
                            inputStream.close();
                        }
                    }
                }
            } catch (MalformedURLException urlEx) {
                Logger.Error("main.initialBtnInfoContextMenu()", "MalformedURLException HTTP response Jokers", urlEx);
                Log.d("DroidCachebox", urlEx.getMessage());
            } catch (IOException ioEx) {
                Logger.Error("main.initialBtnInfoContextMenu()", "IOException HTTP response Jokers", ioEx);
                Log.d("DroidCachebox", ioEx.getMessage());
                MessageBox.Show("Fehler bei Internetzugriff", null);
            } catch (Exception ex) {
                Logger.Error("main.initialBtnInfoContextMenu()", "HTTP response Jokers", ex);
                Log.d("DroidCachebox", ex.getMessage());
            }
        }
    }
