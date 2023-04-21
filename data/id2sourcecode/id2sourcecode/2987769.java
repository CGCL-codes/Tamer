    private boolean doBeforeProcessing(RequestWrapper request, ResponseWrapper response) throws IOException, ServletException {
        if (!request.isSecure()) return true;
        try {
            UserHandler uh = (UserHandler) request.getSession().getAttribute("userHandler");
            if (uh == null) {
                uh = new UserHandler();
                request.getSession().setAttribute("userHandler", uh);
            }
            if (!uh.isLoggedIn()) {
                Security.addProvider(new BouncyCastleProvider());
                InputStreamReader rd = new InputStreamReader(SSLAutoAuthFilter.class.getResourceAsStream("/desk.pem"));
                PEMReader reader = new PEMReader(rd);
                Object oo = reader.readObject();
                KeyPair kp = (KeyPair) oo;
                X509Certificate[] certs = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");
                if (certs != null) {
                    for (X509Certificate cert : certs) {
                        try {
                            cert.checkValidity();
                            cert.verify(kp.getPublic());
                            byte[] pwdMD5 = Hex.encode(MessageDigest.getInstance("MD5").digest(cert.getEncoded()));
                            String code = new String(pwdMD5);
                            if (code.length() < 32) {
                                for (int i = (32 - code.length()); i > 0; i--) {
                                    code = "0" + code;
                                }
                            }
                            Query q = SessionHolder.currentSession().getSess().createQuery(" FROM " + CertificateEntry.class.getName() + "  WHERE md5Key = ? AND person IS NOT NULL AND valid = true ");
                            q.setString(0, code);
                            CertificateEntry ce = (CertificateEntry) q.uniqueResult();
                            uh.loginNoPw(ce.getPerson());
                        } catch (Exception ex) {
                        }
                    }
                }
                SessionHolder.closeSession();
                if (!uh.isLoggedIn()) {
                    return true;
                }
                if (request.getRequestURI().length() <= request.getContextPath().length() + 1 && uh.getUser().getCompany() == 0) {
                    response.sendRedirect(request.getContextPath() + "/intranet/");
                    SessionHolder.closeSession();
                    return false;
                } else {
                    SessionHolder.closeSession();
                    return true;
                }
            } else {
                SessionHolder.closeSession();
                return true;
            }
        } catch (Exception excasdasd) {
            SessionHolder.closeSession();
            return true;
        }
    }
