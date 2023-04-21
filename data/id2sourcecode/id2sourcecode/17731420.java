        protected Integer doInBackground(String... urls) {
            ArrayList<String> images = new ArrayList<String>();
            if (!FileManager.INSTANCE.isFileCached(urls[0])) {
                URL url;
                try {
                    Writer out = FileManager.INSTANCE.getFileWriterBuffered(urls[0]);
                    url = new URL(checkUrl(urls[0]));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoOutput(true);
                    connection.connect();
                    InputStreamReader is = new InputStreamReader(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(is);
                    String line;
                    boolean flag1 = false;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.contains("</center>")) {
                            flag1 = true;
                            break;
                        }
                    }
                    if (flag1) {
                        while ((line = reader.readLine()) != null) {
                            if (line.contains("<br><center>")) {
                                break;
                            }
                            if (line.startsWith("<img vspace=")) {
                                images.add(getImageName(line));
                            }
                            out.write(line);
                        }
                    }
                    out.close();
                    reader.close();
                    is.close();
                    connection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            int totalImage = images.size();
            for (int i = 0; i < totalImage; i++) {
                FileManager.INSTANCE.downloadFileFromInternet(images.get(i), checkUrl(images.get(i)));
            }
            return 0;
        }
