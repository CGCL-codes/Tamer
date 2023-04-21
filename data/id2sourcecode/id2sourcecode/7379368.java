    public static String Signature(Map params, String secret) {
        String result = null;
        params.remove(Constants.PARAMETER_SIGN);
        try {
            Map treeMap = new TreeMap();
            treeMap.putAll(params);
            Iterator iter = treeMap.keySet().iterator();
            StringBuffer orgin = new StringBuffer(secret);
            while (iter.hasNext()) {
                String name = (String) iter.next();
                orgin.append(name).append(params.get(name));
            }
            MessageDigest md = MessageDigest.getInstance("MD5");
            result = byte2hex(md.digest(orgin.toString().getBytes("utf-8")));
        } catch (Exception ex) {
            throw new java.lang.RuntimeException("sign error !");
        }
        return result;
    }
