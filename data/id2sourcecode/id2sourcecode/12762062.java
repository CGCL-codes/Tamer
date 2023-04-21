    public static URL findSourceURLName(String spath) {
        String[] ext = { "", EXT_COMPRESSED, EXT_DEFAULT };
        for (int i = 0; i < ext.length; i++) {
            try {
                URL turl = new URL(spath + ext[i]);
                if (turl.openStream() != null) {
                    return turl;
                }
            } catch (Exception e) {
            }
        }
        return null;
    }
