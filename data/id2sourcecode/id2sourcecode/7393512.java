    private String getWsseHeaderValue() {
        try {
            byte[] nonceB = new byte[8];
            SecureRandom.getInstance("SHA1PRNG").nextBytes(nonceB);
            SimpleDateFormat zulu = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            zulu.setTimeZone(TimeZone.getTimeZone("GMT"));
            Calendar now = Calendar.getInstance();
            now.setTimeInMillis(System.currentTimeMillis());
            String created = zulu.format(now.getTime());
            byte[] createdB = created.getBytes("utf-8");
            byte[] passwordB = password.getBytes("utf-8");
            byte[] v = new byte[nonceB.length + createdB.length + passwordB.length];
            System.arraycopy(nonceB, 0, v, 0, nonceB.length);
            System.arraycopy(createdB, 0, v, nonceB.length, createdB.length);
            System.arraycopy(passwordB, 0, v, nonceB.length + createdB.length, passwordB.length);
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(v);
            byte[] digest = md.digest();
            StringBuffer buf = new StringBuffer();
            buf.append("UsernameToken Username=\"");
            buf.append(username);
            buf.append("\", PasswordDigest=\"");
            buf.append(new String(Base64.encodeBase64(digest)));
            buf.append("\", Nonce=\"");
            buf.append(new String(Base64.encodeBase64(nonceB)));
            buf.append("\", Created=\"");
            buf.append(created);
            buf.append('"');
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
