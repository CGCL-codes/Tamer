    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        try {
            String routeCount = request.getParameter("numroutes");
            String hostname = request.getParameter("hostname");
            String version = request.getParameter("versionnumber");
            String expday = request.getParameter("expday");
            String expmonth = request.getParameter("expmonth");
            String expyear = request.getParameter("expyear");
            long created = new java.util.Date().getTime();
            String exp = expmonth + "/" + expday + "/" + expyear;
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
            java.util.Date d = sdf.parse(exp, new ParsePosition(0));
            long expirydate = d.getTime();
            String licKey = hostname + "|" + routeCount + "|" + version + "|" + expirydate + "|" + created;
            java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger("96142334799ba0417560828127f1fef7682059b107f828dfbc953cf5840f5d02433303f43f0743bb253a7ac5539ef9c1e9cc1038c3210315d331c8593d977ce70d9790ba98e565fe5158166dc1b36ae6587d8b8c4fded7687cf2e2983dcc98f81b6e79c11282fe65697b6f52844a9700331a151283681a726f85b8483f012e7e32040a4d0d7e6f6d21008afc5263ddd3ab638b7e6d74f68614752cb3a06f1d5dd416b7a46497e082a7da21e4b151edfbb69e1a9bfb7a77e2ffc048e5df76fb4d6855a2ca8a9865ecf8bcd47848fd104f67f91581151ef59b1f9483bdd2d2596484ba3c10ab679fe4c37b41d96a239ed4316079969beec783714e0aba8bdc96a5", 16), new BigInteger("10001", 16));
            KeyFactory fact = KeyFactory.getInstance("RSA", "BC");
            PublicKey pubKey = fact.generatePublic(pubKeySpec);
            Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
            c.init(Cipher.ENCRYPT_MODE, pubKey, new SecureRandom());
            byte[] encrypted = c.doFinal(licKey.getBytes());
            BASE64Encoder encoder = new BASE64Encoder();
            String licenseText = encoder.encode(encrypted);
            String sql = "insert into BIELicense";
            sql += " (hostName, routeCount, expiration, created, version, LicenseKey)";
            sql += " values ('" + hostname + "'," + routeCount + "," + expirydate + "," + created + "," + version + ",'" + licenseText + "')";
            Class.forName(dbDriver).newInstance();
            Connection conn = DriverManager.getConnection(dbURL);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            conn.close();
            response.setContentType("text/html");
            java.io.PrintWriter out = response.getWriter();
            out.println("<html><body><form><textarea name='hostkey' rows='15' cols='60'>" + licenseText + "</textarea></form></body></html>");
            out.close();
        } catch (Exception e) {
            try {
                java.io.PrintWriter out = response.getWriter();
                out.println(e.getMessage());
                out.close();
            } catch (Exception e2) {
                e.printStackTrace();
            }
        }
    }
