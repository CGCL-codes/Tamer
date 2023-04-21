    private void addAuthorization(final HttpURLConnection conn, final String method, final byte[] data) throws IOException {
        String contentType = getContentType(conn);
        String contentMD5 = "";
        if (data != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(data);
                contentMD5 = Base64.encodeBytes(md.digest());
            } catch (Exception e) {
                throw new IllegalArgumentException("unable to compute content-md5", e);
            }
            conn.addRequestProperty("Content-MD5", contentMD5);
        }
        final String DateFormat = "EEE, dd MMM yyyy HH:mm:ss ";
        final SimpleDateFormat format = new SimpleDateFormat(DateFormat, Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        final String date = format.format(new Date()) + "GMT";
        conn.addRequestProperty("Date", date);
        final StringBuilder buf = new StringBuilder();
        buf.append(method).append("\n");
        buf.append(contentMD5).append("\n");
        buf.append(contentType).append("\n");
        buf.append(date).append("\n");
        final String headers = getHeaders(conn);
        if (headers.length() > 0) {
            buf.append(headers);
        }
        buf.append(conn.getURL().getPath());
        String auth;
        try {
            final SecretKeySpec signingKey = new SecretKeySpec(m_password.getBytes(), SIGNATURE_ALGORITHM);
            final Mac mac = Mac.getInstance(SIGNATURE_ALGORITHM);
            mac.init(signingKey);
            auth = Base64.encodeBytes(mac.doFinal(buf.toString().getBytes()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to calculate digest", e);
        }
        conn.setRequestProperty("Authorization", "AWS " + m_username + ":" + auth);
    }
