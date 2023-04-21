            public void run() {
                try {
                    final FileInputStream gpxInputStream = new FileInputStream(file);
                    String tagsToUse = tags;
                    if (addDateTags || tagsToUse == null) {
                        if (tagsToUse == null) {
                            tagsToUse = AUTO_TAG_FORMAT.format(new GregorianCalendar().getTime());
                        } else {
                            tagsToUse = tagsToUse + " " + AUTO_TAG_FORMAT.format(new GregorianCalendar().getTime());
                        }
                    }
                    Log.d("UPLOAD", "Uploading " + filename + " to openstreetmap.org");
                    final String urlDesc = (description == null) ? DEFAULT_DESCRIPTION : description.replaceAll("\\.;&?,/", "_");
                    final String urlTags = (tagsToUse == null) ? DEFAULT_TAGS : tagsToUse.replaceAll("\\\\.;&?,/", "_");
                    final URL url = new URL("http://www.openstreetmap.org/api/" + API_VERSION + "/gpx/create");
                    Log.d("UPLOAD", "Destination Url: " + url);
                    final HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setConnectTimeout(15000);
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.addRequestProperty("Authorization", "Basic " + encodeBase64(username + ":" + password));
                    con.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
                    con.addRequestProperty("Connection", "close");
                    con.addRequestProperty("Expect", "");
                    con.connect();
                    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(con.getOutputStream()));
                    writeContentDispositionFile(out, "file", gpxInputStream);
                    writeContentDisposition(out, "description", urlDesc);
                    writeContentDisposition(out, "tags", urlTags);
                    writeContentDisposition(out, "public", "1");
                    out.writeBytes("--" + BOUNDARY + "--" + LINE_END);
                    Log.i("UPLOAD", "data : " + out.size());
                    out.flush();
                    final int retCode = con.getResponseCode();
                    String retMsg = con.getResponseMessage();
                    Log.d("UPLOAD", "return code: " + retCode + " " + retMsg);
                    if (retCode != 200) {
                        if (con.getHeaderField("Error") != null) {
                            retMsg += "\n" + con.getHeaderField("Error");
                        }
                        out.close();
                        con.disconnect();
                        throw new IOException(ctx.getString(R.string.errorLogin));
                    }
                    out.close();
                    con.disconnect();
                } catch (Exception e) {
                    Log.e("UPLOAD", "OSMUpload Error", e);
                    mess = e.getMessage();
                }
            }
