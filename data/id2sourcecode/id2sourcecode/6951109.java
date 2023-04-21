    public ValidEPoint exchange(EPoint o, EPoint e) throws MalformedURLException, IOException, NoSuchAlgorithmException, InvalidEPointCertificateException, InvalidKeyException, SignatureException {
        URLConnection u = new URL(url, "action").openConnection();
        OutputStream os;
        InputStream is;
        u.setDoOutput(true);
        u.setDoInput(true);
        u.setAllowUserInteraction(false);
        ((HttpURLConnection) u).setInstanceFollowRedirects(false);
        os = u.getOutputStream();
        os.write(("B=" + URLEncoder.encode(o.toString(), "UTF-8") + "&D=" + Base16.encode(e.getMD())).getBytes());
        os.close();
        is = u.getInputStream();
        int res = ((HttpURLConnection) u).getResponseCode();
        if ((res >= 300) && (res < 400)) {
            String r = u.getHeaderField("Location");
            is.close();
            is = new URL(r).openStream();
        }
        ValidEPoint v = new ValidEPoint(this, e, is);
        is.close();
        return v;
    }
