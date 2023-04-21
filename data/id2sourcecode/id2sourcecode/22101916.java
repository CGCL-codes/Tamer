    public static URL getLocalizedResource(ClassLoader cl, String front, String back, Locale locale, boolean tryRead) {
        URL url;
        for (Enumeration tails = getCandidates(locale); tails.hasMoreElements(); ) {
            String tail = (String) tails.nextElement();
            String name = (new StringBuffer(front)).append(tail).append(back).toString();
            if (cl == null) {
                url = ClassLoader.getSystemResource(name);
            } else {
                url = cl.getResource(name);
            }
            if (url != null) {
                if (tryRead) {
                    try {
                        InputStream is = url.openConnection().getInputStream();
                        if (is != null) {
                            int i = is.read();
                            is.close();
                            if (i != -1) {
                                return url;
                            }
                        }
                    } catch (Throwable t) {
                    }
                } else {
                    return url;
                }
            }
        }
        return null;
    }
