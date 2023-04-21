    public static Any signDoc(Connection con, Any values, Any retValue) throws SQLException, StandardException, NoSuchAlgorithmException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeyException, SignatureException, InvalidKeySpecException, IOException {
        HashMap val = (HashMap) values.extract_Value();
        String sID = (String) val.get("ID");
        String sVersion = (String) val.get("VER");
        HashMap[] attrs = (HashMap[]) val.get("ATTRS");
        byte[] data = (byte[]) val.get("DATA");
        java.util.Date tstamp = (java.util.Date) val.get("DATE");
        byte[] sign = (byte[]) val.get("SIGN");
        PublicKey pKey = getCurrentPublicKey(con);
        if (data != null) {
            byte oldData[] = getDocumentBLOB(con, sID, sVersion);
            if (!Arrays.equals(oldData, data)) {
                ObjUtil.throwStandardException(new StandardException(ResourceBundle.getBundle(Documents.class.getName()).getString("SIGN_ERROR")));
            }
            if (!RSA.signVerify(data, sign, pKey)) {
                ObjUtil.throwStandardException(new StandardException(ResourceBundle.getBundle(Documents.class.getName()).getString("SIGN_ERROR")));
            }
        }
        if (attrs != null) {
            if (!RSA.signVerify(getAttributesData(attrs), sign, pKey)) {
                ObjUtil.throwStandardException(new StandardException(ResourceBundle.getBundle(Documents.class.getName()).getString("SIGN_ERROR")));
            }
        }
        OracleCallableStatement ps = null;
        String sSql = ResourceBundle.getBundle(Documents.class.getName()).getString("signDoc");
        try {
            BLOB blb = null;
            ps = (OracleCallableStatement) con.prepareCall(sSql);
            ps.registerOutParameter(1, Types.NUMERIC);
            ps.setString(2, sID);
            ps.setString(3, sVersion);
            if (attrs != null) {
                ArrayList<fireteam.orb.server.processors.types.Attribute> arAttr = new ArrayList<fireteam.orb.server.processors.types.Attribute>();
                for (HashMap Attr : attrs) {
                    arAttr.add(new fireteam.orb.server.processors.types.Attribute((String) Attr.get("NAME"), (String) Attr.get("VALUE")));
                }
                fireteam.orb.server.processors.types.AttributeList arAttrs = new fireteam.orb.server.processors.types.AttributeList(arAttr.toArray(new fireteam.orb.server.processors.types.Attribute[arAttr.size()]));
                ps.setORAData(4, arAttrs);
                blb = BLOB.createTemporary(con, false, BLOB.DURATION_SESSION);
                blb.setBytes(1, data);
                ps.setBlob(5, blb);
                ps.setNull(6, Types.DATE);
            } else {
                ps.setNull(4, OracleTypes.ARRAY, "BC.ATTRIBUTE_LIST");
                blb = BLOB.createTemporary(con, false, BLOB.DURATION_SESSION);
                blb.setBytes(1, data);
                ps.setBlob(5, blb);
                ps.setTimestamp(6, new Timestamp(tstamp.getTime()));
            }
            ps.executeUpdate();
            long ret = ps.getLong(1);
            if (ret == 1) {
                con.commit();
            } else {
                con.rollback();
            }
            if (blb != null) BLOB.freeTemporary(blb);
        } finally {
            if (ps != null) ps.close();
        }
        return retValue;
    }
