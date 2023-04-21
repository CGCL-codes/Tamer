    @Override
    public synchronized String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
        JMeterVariables vars = getVariables();
        String str = ((CompoundVariable) values[0]).execute();
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException ex) {
            return "Error creating digest: " + ex;
        }
        String res = JOrphanUtils.baToHexString(digest.digest(str.getBytes()));
        if (vars != null && values.length > 1) {
            String varName = ((CompoundVariable) values[1]).execute().trim();
            vars.put(varName, res);
        }
        return res;
    }
