    private String signRequestParameters(List<Pair<String, String>> parameters) {
        StringBuffer buf = new StringBuffer();
        buf.append(secret);
        SortedMap<String, String> sortedParams = new TreeMap<String, String>(new Comparator<String>() {

            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        Iterator<Pair<String, String>> pit = parameters.iterator();
        while (pit.hasNext()) {
            Pair<String, String> p = pit.next();
            sortedParams.put(p.getFirst(), p.getSecond());
        }
        Iterator<String> kit = sortedParams.keySet().iterator();
        while (kit.hasNext()) {
            String key = kit.next();
            buf.append(key);
            buf.append(sortedParams.get(key));
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        String hashBase = buf.toString();
        byte[] digestBytes = md.digest(hashBase.getBytes());
        StringBuffer hashString = new StringBuffer();
        for (int i = 0; i < digestBytes.length; i++) {
            hashString.append(Integer.toString((digestBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return hashString.toString();
    }
