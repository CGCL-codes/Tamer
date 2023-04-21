    public InformationCurrencySeries certifyInformation(final String certRequest) throws AxisFault {
        if (log.isDebugEnabled()) {
            log.debug("certifyInformation: received certificationRequest\n" + certRequest + "\n");
        }
        Connection conn = null;
        try {
            String userString = null;
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            SecureRandom sr = new SecureRandom();
            conn = getConnection();
            PreparedStatement keyIns = conn.prepareStatement("insert into keyPair values (?,?)");
            PreparedStatement srsIns = conn.prepareStatement(" insert into certificateSeries ( seriesInt, seriesUniq, type, " + " issuer, createdTime, expiresTime, numBytes, certNum, keyPair, title) values " + " ( ?,?,?,'1',now(),now() + '1 year'::interval,?,?,?,?)");
            PreparedStatement pSrsIns = conn.prepareStatement("insert into primarySeries (primarySeriesInt, seriesInt) " + " values (?, ?)");
            PreparedStatement pInfoIdIns = conn.prepareStatement("insert into informationIdentifiers " + " (infoId, seriesInt, digestValue, underlierLocator) " + " values (?, ?, ?, ?)");
            PreparedStatement certIns = conn.prepareStatement("insert into certificates (certificateID, " + "certInfo, certDigest, certSignature, seriesInt, " + " owner, reserve, valid) values (?, ?,?,?,?,?,?,'1')");
            PreparedStatement iiSel = conn.prepareStatement(" select seriesInt from informationidentifiers where " + " underlierLocator=? and digestValue=? ");
            PreparedStatement numIISel = conn.prepareStatement(" select count(*) from informationidentifiers where " + " seriesInt=? ");
            CertificationRequest cr = new CertificationRequest(certRequest);
            if (cr == null) throw new AxisFault("CertificationRequest is null");
            InformationIdentifier[] crIdent = cr.getIdentifiers();
            if ((crIdent == null) || (crIdent.length == 0) || (cr.getTitle() == null)) {
                releaseConnection(conn);
                throw new AxisFault("Certification request must have a title and identifier element(s) present.");
            }
            this.authorize(cr, this.CERTIFY_INFORMATION);
            String allowMultiple = icwsProps.getProperty("allowMultipleIssuance");
            if (allowMultiple != null && ("1".equals(allowMultiple) || "true".equals(allowMultiple))) {
            } else {
                HashSet totalResultSet = null;
                InformationIdentifier[] iiArray = cr.getIdentifiers();
                for (int i = 0; i < iiArray.length; i++) {
                    HashSet tmpSet = new HashSet();
                    String dvString = iiArray[i].getDigestValue();
                    String ulString = iiArray[i].getUnderlierLocator();
                    if (ulString == null) {
                        iiSel.setNull(1, Types.VARCHAR);
                    } else {
                        iiSel.setString(1, ulString);
                    }
                    if (dvString == null) {
                        iiSel.setNull(2, Types.VARCHAR);
                    } else {
                        iiSel.setString(2, dvString);
                    }
                    ResultSet iiRS = iiSel.executeQuery();
                    if (iiRS != null) {
                        while (iiRS.next()) {
                            int resultInt = iiRS.getInt(1);
                            if (resultInt != 0) {
                                tmpSet.add(new Integer(resultInt));
                            }
                        }
                    }
                    if (totalResultSet == null) {
                        totalResultSet = tmpSet;
                    } else {
                        totalResultSet.retainAll(tmpSet);
                    }
                }
                Iterator totalResultIt = totalResultSet.iterator();
                while (totalResultIt.hasNext()) {
                    Integer resultInt = (Integer) totalResultIt.next();
                    numIISel.setInt(1, resultInt.intValue());
                    ResultSet iiRS = numIISel.executeQuery();
                    if (iiRS != null) {
                        if (iiRS.next()) {
                            int numRows = iiRS.getInt(1);
                            if (numRows == iiArray.length) {
                                throw new AxisFault(" invalid set of information identifiers - already present in system ");
                            }
                        }
                    }
                }
            }
            int userID = -1;
            try {
                userID = this.getUserID(cr);
            } catch (java.lang.Exception e) {
            }
            String titleStr = cr.getTitle();
            KeyPair kp = icwsKPG.genKeyPair();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(kp);
            int randVal = -1;
            while (true) {
                randVal = sr.nextInt(Integer.MAX_VALUE);
                keyIns.setInt(1, randVal);
                keyIns.setBytes(2, baos.toByteArray());
                int resCode = keyIns.executeUpdate();
                if (resCode == 1) break;
            }
            icwsDSA.initSign(kp.getPrivate());
            Integer srsInt = null;
            String seriesID = null;
            Integer pSrsInt = null;
            while (true) {
                srsInt = new Integer(sr.nextInt(Integer.MAX_VALUE));
                byte[] objectBytes = new byte[128];
                sr.nextBytes(objectBytes);
                byte[] digestBytes = sha.digest(objectBytes);
                String digestStr = Utils.byteArrayToHexString(digestBytes).replaceAll(" ", "").toLowerCase();
                seriesID = ICWSUtils.getSeriesID(icwsProps, digestStr);
                srsIns.setInt(1, srsInt.intValue());
                srsIns.setString(2, seriesID);
                srsIns.setInt(3, this.PRIMARY_SERIES);
                srsIns.setInt(4, numBytes.intValue());
                srsIns.setInt(5, certNum.intValue());
                srsIns.setInt(6, randVal);
                srsIns.setString(7, titleStr);
                int updated = srsIns.executeUpdate();
                if (updated == 1) {
                    break;
                }
            }
            while (true) {
                pSrsInt = new Integer(sr.nextInt(Integer.MAX_VALUE));
                pSrsIns.setInt(1, pSrsInt.intValue());
                pSrsIns.setInt(2, srsInt.intValue());
                int updated = pSrsIns.executeUpdate();
                if (updated == 1) {
                    break;
                }
            }
            for (int r = 0; r < crIdent.length; r++) {
                while (true) {
                    pInfoIdIns.setInt(1, sr.nextInt(Integer.MAX_VALUE));
                    pInfoIdIns.setInt(2, srsInt.intValue());
                    if (crIdent[r].getDigestValue() != null) {
                        pInfoIdIns.setString(3, crIdent[r].getDigestValue());
                    } else if ("".equals(crIdent[r].getDigestValue())) {
                        pInfoIdIns.setNull(3, Types.VARCHAR);
                    } else {
                        pInfoIdIns.setNull(3, Types.VARCHAR);
                    }
                    if (crIdent[r].getUnderlierLocator() != null) {
                        pInfoIdIns.setString(4, crIdent[r].getUnderlierLocator());
                    } else if ("".equals(crIdent[r].getUnderlierLocator())) {
                        pInfoIdIns.setNull(4, Types.VARCHAR);
                    } else {
                        pInfoIdIns.setNull(4, Types.VARCHAR);
                    }
                    int updated = pInfoIdIns.executeUpdate();
                    if (updated == 1) break;
                }
            }
            InformationCurrencySeries ics = new InformationCurrencySeries(seriesID);
            Float whNum = new Float(withholding.floatValue() * certNum.floatValue());
            Integer whNumber = new Integer(whNum.intValue());
            if (whNumber.intValue() == certNum.intValue()) {
                whNumber = new Integer(certNum.intValue() - 1);
            }
            Integer distNumber = new Integer(certNum.intValue() - whNumber.intValue());
            try {
                this.authorize(cr, this.SET_PARAMS);
                if ((cr.getCertificateNumber() == -1) || (cr.getWithholdingFraction() == -1)) {
                    releaseConnection(conn);
                    throw new PermissionException("didn't have values " + "cr.getCertificateNumber() is " + cr.getCertificateNumber() + " cr.getWithholdingFraction() " + cr.getWithholdingFraction());
                }
                withholding = Float.valueOf(cr.getWithholdingFraction());
                if ((withholding.floatValue() < 0) || (withholding.floatValue() > 1)) {
                    withholding = new Float(".025");
                }
                certNum = Integer.valueOf(cr.getCertificateNumber());
                if ((certNum.intValue() < 0) || (certNum.intValue() > 1000)) {
                    certNum = new Integer("40");
                }
                whNum = new Float(withholding.floatValue() * certNum.floatValue());
                whNumber = new Integer(whNum.intValue());
                if (whNumber.intValue() == certNum.intValue()) {
                    whNumber = new Integer(certNum.intValue() - 1);
                }
                distNumber = new Integer(certNum.intValue() - whNumber.intValue());
            } catch (PermissionException e) {
            }
            for (int i = 0; i < distNumber.intValue(); i++) {
                byte[] byteArray = new byte[numBytes.intValue()];
                sr.nextBytes(byteArray);
                String certStr = Base64.encode(byteArray);
                byte[] certDigestBytes = sha.digest(byteArray);
                String certDigestStr = Base64.encode(certDigestBytes);
                icwsDSA.update(byteArray);
                byte[] certSigBytes = icwsDSA.sign();
                String certSigStr = Base64.encode(certSigBytes);
                while (true) {
                    certIns.setInt(1, sr.nextInt(Integer.MAX_VALUE));
                    certIns.setBytes(2, byteArray);
                    certIns.setBytes(3, certDigestBytes);
                    certIns.setBytes(4, certSigBytes);
                    certIns.setInt(5, srsInt.intValue());
                    certIns.setInt(6, userID);
                    certIns.setBoolean(7, false);
                    int certUpdate = certIns.executeUpdate();
                    if (certUpdate == 1) break;
                }
                ics.addICU(new InformationCurrencyUnit(seriesID, byteArray, certSigBytes));
            }
            for (int j = 0; j < whNumber.intValue(); j++) {
                byte[] byteArray = new byte[numBytes.intValue()];
                sr.nextBytes(byteArray);
                String certStr = Base64.encode(byteArray);
                byte[] certDigestBytes = sha.digest(byteArray);
                String certDigestStr = Base64.encode(certDigestBytes);
                icwsDSA.update(byteArray);
                byte[] certSigBytes = icwsDSA.sign();
                String certSigStr = Base64.encode(certSigBytes);
                while (true) {
                    certIns.setInt(1, sr.nextInt(Integer.MAX_VALUE));
                    certIns.setBytes(2, byteArray);
                    certIns.setBytes(3, certDigestBytes);
                    certIns.setBytes(4, certSigBytes);
                    certIns.setInt(5, srsInt.intValue());
                    certIns.setInt(6, 0);
                    certIns.setBoolean(7, true);
                    int certUpdate = certIns.executeUpdate();
                    if (certUpdate == 1) break;
                }
            }
            releaseConnection(conn);
            try {
                if (icwsIncludeKeyInfo) {
                    if (icwsPrivateKey != null) ics.sign((PrivateKey) icwsPrivateKey, false, icwsIncludeKeyInfo);
                } else {
                    if (icwsPrivateKey != null) ics.sign((PrivateKey) icwsPrivateKey);
                }
            } catch (java.lang.Exception e) {
            }
            return ics;
        } catch (PermissionException e) {
            releaseConnection(conn);
            throw new AxisFault("certifyInformation: PermissionException ", e);
        } catch (java.security.SignatureException e) {
            releaseConnection(conn);
            throw new AxisFault("certifyInformation: SignatureException ", e);
        } catch (java.sql.SQLException e) {
            releaseConnection(conn);
            throw new AxisFault("certifyInformation: SQL Exception ", e);
        } catch (Exception e) {
            releaseConnection(conn);
            throw new AxisFault("certifyInformation: Exception ", e);
        }
    }
