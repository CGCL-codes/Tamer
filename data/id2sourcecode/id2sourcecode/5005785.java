    public static SukuData getSukuData(String uri, String userno, String... params) throws SukuException {
        StringBuilder sb = new StringBuilder();
        sb.append(uri);
        String requri;
        int resu;
        int i;
        SukuData errr = new SukuData();
        errr.resu = "ERROR";
        if (userno == null) {
            return errr;
        }
        String paras[] = params;
        sb.append("SukuServlet?userno=" + userno);
        for (i = 0; i < paras.length; i++) {
            sb.append("&" + paras[i]);
        }
        requri = sb.toString();
        try {
            logger.fine("URILOG: " + requri);
            URL url = new URL(requri);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            resu = uc.getResponseCode();
            if (resu == 200) {
                String coding = uc.getHeaderField("Content-Encoding");
                StringBuilder xx = new StringBuilder();
                xx.append("Content-Encoding: " + coding);
                xx.append(";");
                for (int j = 0; j < params.length; j++) {
                    xx.append(params[j]);
                    xx.append(";");
                }
                InputStream in = null;
                if ("gzip".equals(coding)) {
                    in = new GZIPInputStream(uc.getInputStream());
                } else {
                    in = uc.getInputStream();
                }
                ObjectInputStream ois = new ObjectInputStream(in);
                SukuData fam = null;
                try {
                    fam = (SukuData) ois.readObject();
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new SukuException(e);
                }
                return fam;
            }
            throw new SukuException("Network error " + resu);
        } catch (Exception e) {
            throw new SukuException(e);
        }
    }
