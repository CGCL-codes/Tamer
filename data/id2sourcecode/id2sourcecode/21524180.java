    public static Map<String, String> getHash(File file, ArrayList<String> hashes) throws IOException {
        long temp = System.currentTimeMillis();
        
        final Map<String, IMessageDigest> mds = new HashMap<>();
        
        for (String h: hashes) {
            if (GNU_HASHES.contains(h)) {
                mds.put(h, HashFactory.getInstance(h));
            }
        }
        
        InputStream is = new FilterInputStream(new BufferedInputStream(
                new FileInputStream(file))){
                    @Override
                    public int read(byte[] b, int off, int len) throws IOException{
                        int leido = this.in.read(b, off, len);
                        if (leido != -1){
                            for (IMessageDigest md : mds.values()){
                                md.update(b, off, leido);
                            }
                        }
                        return leido;
                    }
                };
        
        byte[] buffer = new byte[65536]; // Buffer de 64Kb
        
        while (is.read(buffer) != -1){
            // No hay que hacer nada, el trabajo se hace en el FilterInputStream
        }
        
        Map<String, String> results = new HashMap<>();
        
        for (String algoritmo : hashes){
            results.put(algoritmo, StringHelper.getStringFromBytes(mds.get(algoritmo).digest()));
        }
        
        LOG.info(timeStampMessage(temp, System.currentTimeMillis(), "file (" + file.getName() + ")", hashes));
        
        return results;
    }
