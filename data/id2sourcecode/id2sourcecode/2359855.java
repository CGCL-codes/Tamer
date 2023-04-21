    protected boolean checkLink(URL context, Element element) {
        Attribute att = element.getAttribute("href");
        if (att != null) {
            try {
                URL url = new URL(context, att.getValue());
                URLConnection conn = url.openConnection();
                conn.connect();
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
