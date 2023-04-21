    private void encode() throws IOException {
        IMode cipher = ModeFactory.getInstance("CBC", "AES", 16);
        HashMap cipherAttr = new HashMap();
        IMac mac = MacFactory.getInstance("HMAC-SHA1");
        HashMap macAttr = new HashMap();
        byte[] key = new byte[32];
        byte[] iv = new byte[16];
        byte[] mackey = new byte[20];
        byte[] salt = new byte[8];
        byte[] encryptedSecret = new byte[48];
        cipherAttr.put(IMode.KEY_MATERIAL, key);
        cipherAttr.put(IMode.IV, iv);
        cipherAttr.put(IMode.STATE, new Integer(IMode.ENCRYPTION));
        macAttr.put(IMac.MAC_KEY_MATERIAL, mackey);
        PrintStream out = null;
        if (compress) {
            out = new PrintStream(new GZIPOutputStream(new FileOutputStream(file)));
        } else {
            out = new PrintStream(new FileOutputStream(file));
        }
        out.println("<?xml version=\"1.0\"?>");
        out.println("<!DOCTYPE sessions [");
        out.println("  <!ELEMENT sessions (session*)>");
        out.println("  <!ATTLIST sessions size CDATA \"0\">");
        out.println("  <!ATTLIST sessions timeout CDATA \"86400\">");
        out.println("  <!ELEMENT session (peer, certificates?, secret)>");
        out.println("  <!ATTLIST session id CDATA #REQUIRED>");
        out.println("  <!ATTLIST session protocol (SSLv3|TLSv1|TLSv1.1) #REQUIRED>");
        out.println("  <!ATTLIST session suite CDATA #REQUIRED>");
        out.println("  <!ATTLIST session created CDATA #REQUIRED>");
        out.println("  <!ATTLIST session timestamp CDATA #REQUIRED>");
        out.println("  <!ELEMENT peer (certificates?)>");
        out.println("  <!ATTLIST peer host CDATA #REQUIRED>");
        out.println("  <!ELEMENT certificates (#PCDATA)>");
        out.println("  <!ATTLIST certificates type CDATA \"X.509\">");
        out.println("  <!ELEMENT secret (#PCDATA)>");
        out.println("  <!ATTLIST secret salt CDATA #REQUIRED>");
        out.println("]>");
        out.println();
        out.print("<sessions size=\"");
        out.print(cacheSize);
        out.print("\" timeout=\"");
        out.print(timeout);
        out.println("\">");
        for (Iterator it = sessions.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            Session.ID id = (Session.ID) entry.getKey();
            Session session = (Session) entry.getValue();
            if (!session.valid) {
                continue;
            }
            out.print("<session id=\"");
            out.print(Base64.encode(id.getId(), 0));
            out.print("\" suite=\"");
            out.print(session.getCipherSuite());
            out.print("\" protocol=\"");
            out.print(session.getProtocol());
            out.print("\" created=\"");
            out.print(session.getCreationTime());
            out.print("\" timestamp=\"");
            out.print(session.getLastAccessedTime());
            out.println("\">");
            out.print("<peer host=\"");
            out.print(session.getPeerHost());
            out.println("\">");
            Certificate[] certs = session.getPeerCertificates();
            if (certs != null && certs.length > 0) {
                out.print("<certificates type=\"");
                out.print(certs[0].getType());
                out.println("\">");
                for (int i = 0; i < certs.length; i++) {
                    out.println("-----BEGIN CERTIFICATE-----");
                    try {
                        out.print(Base64.encode(certs[i].getEncoded(), 70));
                    } catch (CertificateEncodingException cee) {
                        throw new IOException(cee.toString());
                    }
                    out.println("-----END CERTIFICATE-----");
                }
                out.println("</certificates>");
            }
            out.println("</peer>");
            certs = session.getLocalCertificates();
            if (certs != null && certs.length > 0) {
                out.print("<certificates type=\"");
                out.print(certs[0].getType());
                out.println("\">");
                for (int i = 0; i < certs.length; i++) {
                    out.println("-----BEGIN CERTIFICATE-----");
                    try {
                        out.print(Base64.encode(certs[i].getEncoded(), 70));
                    } catch (CertificateEncodingException cee) {
                        throw new IOException(cee.toString());
                    }
                    out.println("-----END CERTIFICATE-----");
                }
                out.println("</certificates>");
            }
            random.nextBytes(salt);
            pbekdf.init(Collections.singletonMap(IPBE.SALT, salt));
            try {
                pbekdf.nextBytes(key, 0, key.length);
                pbekdf.nextBytes(iv, 0, iv.length);
                pbekdf.nextBytes(mackey, 0, mackey.length);
                cipher.reset();
                cipher.init(cipherAttr);
                mac.init(macAttr);
            } catch (Exception ex) {
                throw new Error(ex.toString());
            }
            for (int i = 0; i < session.masterSecret.length; i += 16) {
                cipher.update(session.masterSecret, i, encryptedSecret, i);
            }
            mac.update(encryptedSecret, 0, encryptedSecret.length);
            byte[] macValue = mac.digest();
            out.print("<secret salt=\"");
            out.print(Base64.encode(salt, 0));
            out.println("\">");
            out.print(Base64.encode(Util.concat(encryptedSecret, macValue), 70));
            out.println("</secret>");
            out.println("</session>");
        }
        out.println("</sessions>");
        out.close();
    }
