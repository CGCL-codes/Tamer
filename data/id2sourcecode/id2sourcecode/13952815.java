                public void run() {
                    try {
                        LOGGER.info("---> Requesting " + url.toString());
                        URLConnection conn = url.openConnection();
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        byte[] buffer = new byte[2048];
                        int length;
                        while ((length = is.read(buffer)) != -1) {
                            out.write(buffer, 0, length);
                        }
                        is.close();
                        String output = out.toString();
                        if (newBrowserSession) {
                            if (output.startsWith("OK,")) {
                                lastSessionId[0] = output.substring(3);
                            }
                        }
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                        if (debugMode) {
                            e.printStackTrace();
                        }
                    }
                }
