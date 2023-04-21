    public InformationCurrencyUnit exchangeCertificate(final String inputCertificate) throws AxisFault {
        Connection conn = null;
        try {
            ObjectInputStream ois = null;
            ByteArrayInputStream bais = null;
            KeyPair kp = null;
            SecureRandom sr = new SecureRandom();
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            conn = getConnection();
            Signature dsa = Signature.getInstance(ICWSConstants.SIGNATURE_ALGORITHM);
            int userID = -1;
            Integer certificateSeriesInt = null;
            String seriesUniq = null;
            InformationCurrencyUnit icuOld = new InformationCurrencyUnit(new ByteArrayInputStream(inputCertificate.getBytes()));
            PreparedStatement seriesSel = conn.prepareStatement(" select seriesUniq, type, certificateSeries.seriesInt, " + " numBytes, createdTime, expiresTime from " + " certificates, certificateSeries where " + " certificates.certInfo=? and certificateSeries.issuer='1' and " + " certificates.seriesInt=certificateSeries.seriesInt ");
            PreparedStatement keyPairSel = conn.prepareStatement(" select keyPairBytes from certificateSeries, keyPair where " + " keyPair.keyPairID=certificateSeries.keypair and " + " certificateSeries.seriesInt=?");
            PreparedStatement invalidUpdate = conn.prepareStatement(" update certificates set valid='f' where certInfo=? ");
            PreparedStatement newCertIns = conn.prepareStatement(" insert into certificates (certificateID, certInfo, certDigest, " + " certSignature, seriesInt, owner, reserve, valid) values " + " (?, ?, ?, ?, ?, ?, 'f', 't')");
            PreparedStatement certSel = conn.prepareStatement(" select certsignature from certificates where certdigest=? and valid='t'");
            seriesSel.setBytes(1, icuOld.getCertBytes());
            ResultSet rs = seriesSel.executeQuery();
            if (rs != null) {
                if (rs.next()) {
                    seriesUniq = rs.getString("seriesUniq");
                    certificateSeriesInt = new Integer(rs.getInt(3));
                    Timestamp expiresTime = rs.getTimestamp("expiresTime");
                    Date nowTime = new Date();
                    Timestamp nowTS = new Timestamp(nowTime.getTime());
                    if (expiresTime.compareTo(nowTS) < 0) {
                        releaseConnection(conn);
                        return icuOld;
                    }
                    boolean certificateAbsent = true;
                    certSel.setBytes(1, icuOld.getDigestBytes());
                    ResultSet certSelRS = certSel.executeQuery();
                    if (certSelRS != null) {
                        if (certSelRS.next()) {
                            certificateAbsent = false;
                        }
                    }
                    if (certificateAbsent) {
                        releaseConnection(conn);
                        return icuOld;
                    }
                    keyPairSel.setInt(1, certificateSeriesInt.intValue());
                    ResultSet rskp = keyPairSel.executeQuery();
                    if ((rskp != null) && rskp.next()) {
                        byte[] kpBytes = rskp.getBytes("keyPairBytes");
                        bais = new ByteArrayInputStream(kpBytes);
                        ois = new ObjectInputStream(bais);
                        kp = (KeyPair) ois.readObject();
                    } else {
                        releaseConnection(conn);
                        return icuOld;
                    }
                    try {
                        userID = this.getUserID(icuOld);
                    } catch (java.lang.Exception e) {
                    }
                    byte[] certBytes = new byte[numBytes.intValue()];
                    sr.nextBytes(certBytes);
                    String newCertStr = Base64.encode(certBytes);
                    byte[] certDigest = sha.digest(certBytes);
                    String certDigestStr = Base64.encode(certDigest);
                    dsa.initSign(kp.getPrivate());
                    dsa.update(certBytes);
                    byte[] certSig = dsa.sign();
                    String certSigStr = Base64.encode(certSig);
                    invalidUpdate.setBytes(1, icuOld.getCertBytes());
                    int cInvalid = invalidUpdate.executeUpdate();
                    while (true) {
                        newCertIns.setInt(1, sr.nextInt(Integer.MAX_VALUE));
                        newCertIns.setBytes(2, certBytes);
                        newCertIns.setBytes(3, certDigest);
                        newCertIns.setBytes(4, certSig);
                        newCertIns.setInt(5, certificateSeriesInt.intValue());
                        newCertIns.setInt(6, userID);
                        int certUpdate = newCertIns.executeUpdate();
                        if (certUpdate == 1) break;
                    }
                    InformationCurrencyUnit icu = new InformationCurrencyUnit(seriesUniq, certBytes, certSig);
                    try {
                        if (icwsIncludeKeyInfo) {
                            if (icwsPrivateKey != null) icu.sign((PrivateKey) icwsPrivateKey, false, icwsIncludeKeyInfo);
                        } else {
                            if (icwsPrivateKey != null) icu.sign((PrivateKey) icwsPrivateKey);
                        }
                    } catch (java.lang.Exception e) {
                    }
                    releaseConnection(conn);
                    return icu;
                }
            }
            releaseConnection(conn);
            return icuOld;
        } catch (java.security.NoSuchAlgorithmException e) {
            releaseConnection(conn);
            throw new AxisFault("exchangeCertificate: NSA Exception ", e);
        } catch (java.sql.SQLException e) {
            releaseConnection(conn);
            throw new AxisFault("exchangeCertificate: SQL Exception ", e);
        } catch (Exception e) {
            releaseConnection(conn);
            throw new AxisFault("exchangeCertificate: Exception ", e);
        }
    }
