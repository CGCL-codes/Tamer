            public java.io.InputStream sendXmlRpc(byte[] request) throws IOException {
                con = url.openConnection();
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
                con.setAllowUserInteraction(false);
                con.setRequestProperty("Content-Length", Integer.toString(request.length));
                con.setRequestProperty("Content-Type", "text/xml");
                if (auth != null) {
                    con.setRequestProperty("Authorization", "Basic " + auth);
                }
                con.setConnectTimeout(1000);
                OutputStream out = con.getOutputStream();
                out.write(request);
                out.flush();
                out.close();
                return con.getInputStream();
            }
