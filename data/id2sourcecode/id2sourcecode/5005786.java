    public static SukuData getSukuData(String uri, String userno, SukuData request, String... params) throws SukuException {
        SukuData errr = new SukuData();
        errr.resu = "ERROR";
        if (userno == null) {
            return errr;
        }
        if (request == null) {
            return getSukuData(uri, userno, params);
        }
        StringBuilder query = new StringBuilder();
        String paras[] = params;
        try {
            query.append("userno=" + userno);
            for (int i = 0; i < paras.length; i++) {
                query.append("&" + URLEncoder.encode(paras[i], "UTF-8"));
            }
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            DataOutputStream dos = null;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buff = null;
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(request);
            buff = bos.toByteArray();
            oos.close();
            InputStream gis = new ByteArrayInputStream(buff);
            String urix = uri + "SukuServlet";
            byte[] bytes = query.toString().getBytes();
            URL url = new URL(urix);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            con.setRequestProperty("Referer", "/SSS/" + userno + "/");
            con.setRequestProperty("Content-Length", String.valueOf(bytes.length));
            con.setRequestMethod("POST");
            dos = new DataOutputStream(con.getOutputStream());
            dos.write(bytes);
            dos.writeBytes(lineEnd);
            dos.writeBytes(lineEnd);
            int nextByte;
            StringBuilder rivi = new StringBuilder();
            while ((nextByte = gis.read()) >= 0) {
                if (rivi.length() > 64) {
                    dos.writeBytes(rivi.toString() + lineEnd);
                    rivi = new StringBuilder();
                }
                rivi.append(hexi.charAt((nextByte >> 4) & 0xf));
                rivi.append(hexi.charAt((nextByte) & 0xf));
            }
            if (rivi.length() > 0) {
                dos.writeBytes(rivi.toString() + lineEnd);
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            dos.flush();
            dos.close();
            String coding = con.getHeaderField("Content-Encoding");
            InputStream in = null;
            if ("gzip".equals(coding)) {
                in = new GZIPInputStream(con.getInputStream());
            } else {
                in = con.getInputStream();
            }
            ObjectInputStream ois = new ObjectInputStream(in);
            SukuData fam = null;
            try {
                fam = (SukuData) ois.readObject();
                ois.close();
            } catch (Exception e) {
                throw new SukuException(e);
            }
            return fam;
        } catch (Throwable e) {
            throw new SukuException(e);
        }
    }
