    public InformationIdentifier(String ul) throws Exception {
        try {
            if (ul == null || "".equals(ul)) {
                throw new Exception("underlier must not be null or empty");
            }
            if (!ul.equals(Utils.addEntities(ul))) {
                throw new Exception("no xml special characters may be present in the underlier! underlier: " + ul + "");
            }
            underlierLocator = new String(ul);
            digestValueAlgorithm = new String(ICWSConstants.sha1AlgorithmURL);
            MessageDigest sha = MessageDigest.getInstance("SHA1");
            digestValue = new String(Utils.toHex(sha.digest(ul.getBytes())));
            underlierType = new String(textType);
        } catch (java.lang.Exception e) {
            throw new Exception(e);
        }
    }
