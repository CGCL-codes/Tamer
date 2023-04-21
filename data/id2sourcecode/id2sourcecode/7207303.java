    public static synchronized URLConnection openURLConnection(URL url, boolean allowUserInteraction, boolean doInput, boolean doOutput, boolean useCaches, String userAgent, String contentType) throws IOException {
        URLConnection uc;
        uc = url.openConnection();
        uc.setAllowUserInteraction(allowUserInteraction);
        uc.setDoInput(doInput);
        uc.setDoOutput(doOutput);
        uc.setUseCaches(useCaches);
        if (userAgent != null) uc.setRequestProperty("User-Agent", userAgent);
        if (contentType != null) uc.setRequestProperty("Content-Type", contentType);
        return uc;
    }
