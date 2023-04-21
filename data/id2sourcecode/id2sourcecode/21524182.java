    public static Map<String, String> getHash(byte[] byteIn, ArrayList<String> hashes) {
        long temp = System.currentTimeMillis();
        
        final Map<String, IMessageDigest> mds = new HashMap<>();
        
        for (String h: hashes) {
            if (GNU_HASHES.contains(h)) {
                mds.put(h, HashFactory.getInstance(h));
            }
        }
        
        for (IMessageDigest md : mds.values()){
            for (int i = 0; i < byteIn.length; i++){
                md.update(byteIn[i]);
            }
        }
        
        Map<String, String> results = new HashMap<>();
        
        for (String algoritmo : hashes){
            results.put(algoritmo, StringHelper.getStringFromBytes(mds.get(algoritmo).digest()));
        }
        
        LOG.info(timeStampMessage(temp, System.currentTimeMillis(), "byte String", hashes));
        
        return results;
    }
