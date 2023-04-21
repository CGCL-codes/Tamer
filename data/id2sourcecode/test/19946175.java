    static void main1(String[] args) throws Exception {
        String methodArg = args[0];
        String[] args1 = new String[args.length - 1];
        for (int i = 0; i < args1.length; i++) {
            args1[i] = args[i + 1];
        }
        Method method = OxygenTest.class.getMethod(methodArg, new Class[] { String[].class });
        if (method == null) {
            method = Try.class.getMethod(methodArg, new Class[] { String[].class });
        }
        method.invoke(null, new Object[] { args1 });
    }
