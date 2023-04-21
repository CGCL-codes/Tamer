    public static void TivoWebPlusDelete(String download_url) {
        if (download_url == null) return;
        int port = 8080;
        Pattern p = Pattern.compile("http://(\\S+):.+&id=(.+)$");
        Matcher m = p.matcher(download_url);
        if (m.matches()) {
            String ip = m.group(1);
            final String id = m.group(2);
            final String urlString = "http://" + ip + ":" + port + "/confirm/del/" + id;
            log.warn(">> Issuing TivoWebPlus show delete request: " + urlString);
            try {
                final URL url = new URL(urlString);
                class AutoThread implements Runnable {

                    AutoThread() {
                    }

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
                }
                AutoThread t = new AutoThread();
                Thread thread = new Thread(t);
                thread.start();
            } catch (Exception e) {
                log.error("TWP Delete: connection failed: " + urlString);
                log.error(e.toString());
            }
        }
    }
