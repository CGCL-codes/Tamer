                    public void run() {
                        int timeout = 10;
                        try {
                            String data = "u2=bnowshowing";
                            data += "&sub=Delete";
                            data += "&" + URLEncoder.encode("fsida(" + id + ")", "UTF-8") + "=on";
                            data += "&submit=Confirm_Delete";
                            HttpURLConnection c = (HttpURLConnection) url.openConnection();
                            c.setRequestMethod("POST");
                            c.setReadTimeout(timeout * 1000);
                            c.setDoOutput(true);
                            c.connect();
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
                            bw.write(data);
                            bw.flush();
                            bw.close();
                            String response = c.getResponseMessage();
                            if (response.equals("OK")) {
                                log.print(">> TivoWebPlus delete succeeded.");
                            } else {
                                log.error("TWP Delete: Received unexpected response for: " + urlString);
                                log.error(response);
                            }
                        } catch (Exception e) {
                            log.error("TWP Delete: connection failed: " + urlString);
                            log.error(e.toString());
                        }
                    }
