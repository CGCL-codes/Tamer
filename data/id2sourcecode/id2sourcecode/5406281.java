    public static final int getChannel(String param, int deflt) {
        int i;
        try {
            i = Integer.parseInt(Parameters.getOptionalParameter(param, "" + deflt));
            return ((i >= channels) || (i < 0)) ? deflt : i;
        } catch (NumberFormatException e) {
            printException("DEBUG", e);
            return deflt;
        }
    }
