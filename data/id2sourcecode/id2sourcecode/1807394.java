        public void perform(Argument args[], Context context) throws ExtensionException, LogoException {
            try {
                String rname = args[0].getString();
                Object[] input = new Object[args.length - 1];
                LinkedHashMap<String, Object> hm = new LinkedHashMap<String, Object>();
                hm.put(rname, null);
                hm.put("putnamedvector", null);
                for (int i = 0; i < args.length - 1; i++) {
                    String varname = ((Integer) i).toString();
                    input[i] = args[i + 1].get();
                    hm.put(varname, input[i]);
                }
                rConn.storeObject(hm);
            } catch (Exception ex) {
                throw new ExtensionException("Error in PutVector: \n" + ex);
            }
        }
