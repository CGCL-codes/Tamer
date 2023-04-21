        public final void updateCoDaPack() throws JSONException, MalformedURLException, IOException {
            String str = "<html>" + "<font color=\"#008000\">" + "<b>" + "Downloading is in process......." + "</b>" + "</font>" + "</html>";
            label.setText(str);
            for (int i = updates_needed.length() - 1; i >= 0; i--) {
                JSONObject object = updates_needed.getJSONObject(i);
                if (object.has("remove")) {
                    JSONArray removeFiles = object.getJSONArray("remove");
                    for (int j = 0; j < removeFiles.length(); j++) {
                        File f = new File(removeFiles.getString(j));
                        f.delete();
                    }
                }
                if (object.has("add")) {
                    int size = object.getInt("size");
                    JSONArray addFiles = object.getJSONArray("add");
                    int oneChar, byteSize = 0;
                    for (int j = 0; j < addFiles.length(); j++) {
                        System.out.println(addFiles.getString(j));
                        URL url = new URL(LocalConfiguration.HTTP_ROOT + "codapack/" + addFiles.getString(j));
                        URLConnection urlC = url.openConnection();
                        InputStream is = url.openStream();
                        FileOutputStream fos = null;
                        File tempFile = new File(addFiles.getString(j).concat("_temp"));
                        fos = new FileOutputStream(tempFile);
                        int val = 0, pval = 0;
                        while ((oneChar = is.read()) != -1) {
                            fos.write(oneChar);
                            byteSize++;
                            val = (int) ((float) byteSize / (float) size * 100);
                            if (pval < val) {
                                pval = val;
                                pb.setValue(pval);
                            }
                        }
                        System.out.println(((float) byteSize / (float) size) + "%");
                        is.close();
                        fos.close();
                        (new File(addFiles.getString(j))).delete();
                        tempFile.renameTo(new File(addFiles.getString(j)));
                    }
                }
            }
            str = "<html>" + "<font color=\"#FF0000\">" + "<b>" + "Update completed." + "</b>" + "</font>" + "</html>";
            try {
                FileReader file = null;
                JSONObject configuration;
                file = new FileReader("codapack.conf");
                BufferedReader br = new BufferedReader(file);
                configuration = new JSONObject(br.readLine());
                file.close();
                configuration.put("codapack-version", last_version);
                PrintWriter printer = new PrintWriter("codapack.conf");
                configuration.write(printer);
                printer.close();
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
            } catch (JSONException ex) {
            }
            label.setText(str);
        }
