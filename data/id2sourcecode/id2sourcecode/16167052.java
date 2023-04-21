    public static Property setCryptedProperty(final Node pNode, final String pPropName, final String pValue) throws RepositoryException, NoSuchAlgorithmException, UnsupportedEncodingException {
        final Property prop = pNode.setProperty(pPropName, CARS_PasswordService.getInstance().encrypt(pValue));
        if (pNode.isNodeType("jecars:User")) {
            if (!pNode.isNodeType("jecars:digestauth")) {
                pNode.addMixin("jecars:digestauth");
            }
            final MessageDigest md = MessageDigest.getInstance("MD5");
            final byte[] md5 = md.digest((pNode.getName() + ":" + JeCARS_RESTServlet.getRealm() + ":" + pValue).getBytes());
            final String ha1 = StringUtil.bytesToHexString(md5);
            pNode.setProperty("jecars:HA1", ha1);
        }
        return prop;
    }
