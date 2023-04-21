    protected byte[] engineDoFinal() {
        byte[] result = mac.digest();
        engineReset();
        return result;
    }
