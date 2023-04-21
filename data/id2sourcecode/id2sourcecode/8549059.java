        public List<String> execute(List<List<String>> arguments, String hint) {
            List<String> resultList = new ArrayList<String>();
            String value = arguments.get(0).get(0);
            try {
                String hash = new BigInteger(1, MessageDigest.getInstance("MD5").digest(value.getBytes())).toString(16);
                if (hash.length() < 32) hash = "0" + hash;
                resultList.add(hash);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return resultList;
        }
