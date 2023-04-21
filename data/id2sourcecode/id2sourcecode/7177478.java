    public static String createLinkReference(final TVProgramme programme) {
        String reference = null;
        StringBuffer ref = new StringBuffer(LINK_DATE_FORMAT.format(new Date(programme.getStart())));
        ref.append(';');
        ref.append(programme.getChannel().getID());
        try {
            reference = URLEncoder.encode(ref.toString(), CHARSET);
        } catch (UnsupportedEncodingException e) {
        }
        return reference;
    }
