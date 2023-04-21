    private void generateUserAccount(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.debug("UserServlet: Generating hash...");
        HexBinaryAdapter hexConverter = new HexBinaryAdapter();
        String saltHex = adapter.getSaltHex();
        byte[] saltBytes = hexConverter.unmarshal(saltHex);
        byte[] passwordBytes = req.getParameter(HttpParams.UP2P_PASSWORD).getBytes("UTF-8");
        byte[] saltedPassBytes = new byte[saltBytes.length + passwordBytes.length];
        System.arraycopy(saltBytes, 0, saltedPassBytes, 0, 8);
        System.arraycopy(passwordBytes, 0, saltedPassBytes, 8, passwordBytes.length);
        try {
            MessageDigest sha1Encrypter = MessageDigest.getInstance("SHA-1");
            byte[] sha1Hash = sha1Encrypter.digest(saltedPassBytes);
            for (int i = 1; i < 1000; i++) {
                sha1Hash = sha1Encrypter.digest(sha1Hash);
            }
            String hashHex = hexConverter.marshal(sha1Hash);
            adapter.setUser(req.getParameter(HttpParams.UP2P_USERNAME), hashHex);
            LOG.info("Generated new user: " + adapter.getUsername());
        } catch (NoSuchAlgorithmException e) {
            LOG.error("SHA-1 digest not supported on this platform, could not create user account.");
        }
    }
