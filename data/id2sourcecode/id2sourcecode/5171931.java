        public void run() {
            try {
                String url = "http://img.eve.is/serv.asp?s=64&c=" + charId;
                try {
                    HttpConnection conn = null;
                    try {
                        conn = (HttpConnection) Connector.open(url);
                    } catch (SecurityException x) {
                        System.err.println(x);
                        return;
                    }
                    try {
                        conn.setRequestMethod(HttpsConnection.GET);
                        InputStream is = conn.openInputStream();
                        if (conn.getResponseCode() == HttpConnection.HTTP_OK) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            byte[] buffer = new byte[1000];
                            int read;
                            do {
                                read = is.read(buffer);
                                if (read < 0) {
                                    baos.flush();
                                    baos.close();
                                    data = baos.toByteArray();
                                }
                                if (read > 0) {
                                    baos.write(buffer, 0, read);
                                }
                            } while (data == null);
                        } else {
                            System.out.println("conn.getResponseCode():" + conn.getResponseCode());
                        }
                    } finally {
                        conn.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } finally {
                synchronized (this) {
                    notifyAll();
                }
            }
        }
