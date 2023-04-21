    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (!emailField.isLegitimate() || (emailField.getText().length() == 0)) {
            emailField.selectAll();
            emailField.requestFocus();
            return;
        }
        if (!langField.isLegitimate()) {
            langField.selectAll();
            langField.requestFocus();
            return;
        }
        String pass = new String(pw1Field.getPassword());
        if (!new String(pw2Field.getPassword()).equals(pass)) {
            pw1Field.setText("");
            pw2Field.setText("");
            pw1Field.requestFocus();
            return;
        }
        try {
            String[] lang = langField.getText().toUpperCase().split("[ ]*,[ ]*");
            String uid = "";
            if (nameField.getText().length() > 0) uid += "\"" + nameField.getText() + "\" ";
            if (lang.length > 1) {
                uid += "(";
                int i;
                for (i = 0; i < lang.length - 1; i++) uid += lang[i] + ",";
                uid += lang[lang.length - 1] + ") ";
            } else if (lang.length == 1) {
                if (lang[0].length() > 0) uid += "(" + lang[0] + ")";
            }
            uid += "<" + emailField.getText() + ">";
            String prng = getParameter("prng");
            if (prng == null) prng = "SHA1PRNG";
            SecureRandom rand = SecureRandom.getInstance(prng);
            String algo = getParameter("algo");
            if (algo == null) algo = "DSA";
            int bits = getParameter("bits") == null ? 1024 : Integer.parseInt(getParameter("bits"));
            KeyPairGenerator g = KeyPairGenerator.getInstance(algo);
            if (algo.equals("DSA")) ((DSAKeyPairGenerator) g).initialize(bits, true, rand); else g.initialize(bits, rand);
            ByteArrayOutputStream sk = new ByteArrayOutputStream();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PGPPrivateKey.write(g.genKeyPair(), new String(pw1Field.getPassword()), sk);
            SECKEYPacket kp = new SECKEYPacket(new ByteArrayInputStream(sk.toByteArray()), pass);
            PacketHeader.write(true, (byte) 0, 5, os);
            os.write(SIGNATUREPacket.SUB_CREATION);
            long date = kp.getDate().getTime() / 1000l;
            os.write((int) ((date >> 24) & 0xFF));
            os.write((int) ((date >> 16) & 0xFF));
            os.write((int) ((date >> 8) & 0xFF));
            os.write((int) (date & 0xFF));
            PacketHeader.write(true, (byte) 0, 2, os);
            os.write(SIGNATUREPacket.SUB_PRIMARY);
            os.write(0xFF);
            PacketHeader.write(true, (byte) 0, 2, os);
            os.write(SIGNATUREPacket.SUB_FLAGS);
            os.write(0x03);
            PacketHeader.write(true, (byte) 0, 2, os);
            os.write(SIGNATUREPacket.SUB_SYMMETRIC);
            os.write(Algo.DESEDE);
            PacketHeader.write(true, (byte) 0, 3, os);
            os.write(SIGNATUREPacket.SUB_HASH);
            os.write(Algo.SHA1);
            os.write(Algo.MD5);
            PacketHeader.write(true, (byte) 0, 2, os);
            os.write(SIGNATUREPacket.SUB_COMPRESSION);
            os.write(COMPRESSEDPacket.ZLIB);
            byte[] subpk = os.toByteArray();
            os.reset();
            PacketHeader.write(true, (byte) 0, 9, os);
            os.write(16);
            os.write(kp.getKeyID());
            PGPUserID.write(uid, sk);
            new PGPSignature(kp).update(kp.toByteArrayPUBKEY()).update(PGPUserID.cHead(uid.getBytes("UTF-8"))).update(uid.getBytes("UTF8")).write(SIGNATUREPacket.POSITIVEUID, subpk, os.toByteArray(), sk);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pw.println(Armor.BEGIN + Armor.SECKEY + "-----\n" + Armor.VERSION);
            Armor.write(pw, sk.toByteArray(), 0, sk.size());
            pw.println(Armor.END + Armor.SECKEY + "-----");
            pw.close();
            if (getParameter("action") != null) {
                URL url = new URL(getParameter("action"));
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("POST");
                c.setDoOutput(true);
                pw = new PrintWriter(c.getOutputStream());
                pw.println("key=" + URLEncoder.encode(sw.toString(), "UTF-8"));
                pw.close();
                InputStream in = c.getInputStream();
                int i;
                while ((i = in.available()) > 0) in.read(new byte[i]);
                in.close();
            } else System.out.println(sw.toString());
            if (getParameter("next") != null) {
                getAppletContext().showDocument(new URL(getParameter("next")));
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
