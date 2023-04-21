    public static boolean Authenticate(String user, String pass, Integer authtype, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String auth = request.getHeader("Authorization");
        if (authtype == null) {
            return true;
        }
        if (authtype == AUTHTYPE_BASIC) {
            if (auth == null) {
                response.setHeader("WWW-Authenticate", "Basic realm=\"OpenACS\"");
                response.setStatus(response.SC_UNAUTHORIZED);
                return false;
            }
            if (auth.startsWith("Basic ")) {
                String userPassBase64 = auth.substring(6);
                String userPassDecoded = null;
                try {
                    InputStream i = javax.mail.internet.MimeUtility.decode(new ByteArrayInputStream(userPassBase64.getBytes()), "base64");
                    byte[] d = new byte[i.available()];
                    i.read(d);
                    userPassDecoded = new String(d);
                } catch (MessagingException ex) {
                    Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (userPassBase64.endsWith("==")) {
                    userPassDecoded = userPassDecoded.substring(0, userPassDecoded.length() - 2);
                } else if (userPassBase64.endsWith("=")) {
                    userPassDecoded = userPassDecoded.substring(0, userPassDecoded.length() - 1);
                }
                String[] upa = userPassDecoded.split(":");
                System.out.println("CLIENT: up=" + userPassBase64 + " d=" + userPassDecoded + " user='" + upa[0] + "' pass='" + upa[1] + "'");
                System.out.println("CLIENT: user=" + user + " pass=" + pass);
                if (upa[0].equalsIgnoreCase(user) && upa[1].equals(pass)) {
                    return true;
                }
                Logger.getLogger(client.class.getName()).log(Level.WARNING, "Basic auth failed for user=" + upa[0] + " pass=" + upa[1]);
            }
            response.setStatus(response.SC_FORBIDDEN);
            return false;
        }
        if (authtype == AUTHTYPE_MD5) {
            if (auth == null) {
                byte[] nonce = new byte[16];
                Random r = new Random();
                r.nextBytes(nonce);
                response.setHeader("WWW-Authenticate", "Digest realm=\"OpenACS\",qop=\"auth\",nonce=\"" + cvtHex(nonce) + "\"");
                response.setStatus(response.SC_UNAUTHORIZED);
                return false;
            }
            if (auth.startsWith("Digest ")) {
                ByteArrayInputStream bi = new ByteArrayInputStream(auth.substring(6).replace(',', '\n').replaceAll("\"", "").getBytes());
                Properties p = new Properties();
                p.load(bi);
                p.setProperty("method", request.getMethod());
                for (Entry<Object, Object> e : p.entrySet()) {
                    System.out.println("Entry " + e.getKey() + " -> " + e.getValue());
                }
                MessageDigest digest = null;
                try {
                    digest = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
                }
                postDigest(digest, p, user, pass);
                String udigest = (String) p.getProperty("response");
                String d = cvtHex(digest.digest());
                System.out.println("respone: got='" + udigest + "' expected: '" + d + "'");
                if (d.equals(udigest)) return true;
                Logger.getLogger(client.class.getName()).log(Level.WARNING, "MD5 auth failed for user=" + user);
            }
            response.setStatus(response.SC_FORBIDDEN);
            return false;
        }
        return true;
    }
