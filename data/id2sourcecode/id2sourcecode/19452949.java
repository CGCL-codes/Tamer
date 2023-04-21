    @Override
    public String run(Macroprocessor mp, String[] params) {
        String encoding = mp.getMPMacros().getString(ConfigDirectives.INPUT_ENCODING);
        byte[] str;
        if (params.length < 2) {
            str = new byte[0];
        } else {
            if ("".equals(encoding)) {
                str = params[1].getBytes();
            } else {
                try {
                    str = params[1].getBytes(encoding);
                } catch (UnsupportedEncodingException ex) {
                    mp.warning("Unsupported encoding" + encoding);
                    str = params[1].getBytes();
                }
            }
        }
        if (md == null) {
            mp.error("Initialization of MD5 digest algorithm failed.");
            return "";
        }
        md.reset();
        md.update(str);
        byte[] result = md.digest();
        StringBuilder buffer = new StringBuilder(2 * result.length);
        for (int i = 0; i < result.length; i++) {
            appendHexByte(result[i], buffer);
        }
        return buffer.toString();
    }
