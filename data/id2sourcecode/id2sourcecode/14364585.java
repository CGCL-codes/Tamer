    public void send(MimeBodyPart message, MmsHeaders headers) throws MmsException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Mm1Encoder.writeMessageToStream(baos, message, headers);
        baos.close();
        if (log_.isDebugEnabled()) {
            String str = StringUtil.bytesToHexString(baos.toByteArray());
            log_.debug("request [" + str + "]");
        }
        URL url = new URL(mmsProxyGatewayAddress_);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        urlConn.addRequestProperty("Content-Length", "" + baos.size());
        urlConn.addRequestProperty("Content-Type", CONTENT_TYPE_WAP_MMS_MESSAGE);
        urlConn.setDoOutput(true);
        urlConn.setDoInput(true);
        urlConn.setAllowUserInteraction(false);
        OutputStream out = urlConn.getOutputStream();
        baos.writeTo(out);
        out.flush();
        out.close();
        baos.reset();
        baos = new ByteArrayOutputStream();
        InputStream response = urlConn.getInputStream();
        int responsecode = urlConn.getResponseCode();
        log_.debug("HTTP response code : " + responsecode);
        IOUtil.copy(response, baos);
        baos.close();
        if (log_.isDebugEnabled()) {
            String str = StringUtil.bytesToHexString(baos.toByteArray());
            log_.debug("response [" + str + "]");
        }
    }
