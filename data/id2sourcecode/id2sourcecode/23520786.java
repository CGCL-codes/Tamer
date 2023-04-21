    @Override
    public void getConnection(String host, String dbname, String userid, String passwd) throws SukuException {
        schema = null;
        isConnected = false;
        String requri = this.codebase + "SukuServlet?userid=" + userid + "&passwd=" + passwd;
        int resu;
        try {
            URL url = new URL(requri);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            String encoding = uc.getContentEncoding();
            resu = uc.getResponseCode();
            if (resu == 200) {
                InputStream in;
                if ("gzip".equals(encoding)) {
                    in = new java.util.zip.GZIPInputStream(uc.getInputStream());
                } else {
                    in = uc.getInputStream();
                }
                byte b[] = new byte[1024];
                int pit = in.read(b);
                for (int i = 0; i < pit; i++) {
                    if (b[i] == '\n' || b[i] == '\r') {
                        pit = i;
                        break;
                    }
                }
                String aux = new String(b, 0, pit);
                String auxes[] = aux.split("/");
                this.userno = auxes[0];
                if (auxes.length > 1) {
                    Suku.serverVersion = auxes[1];
                }
                in.close();
                this.schema = userid;
                isConnected = true;
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new SukuException(Resurses.getString("ERR_NOT_CONNECTED") + " [" + e.toString() + "]");
        }
    }
