    private static String delete(String urlString, String user, String pass, boolean robustMode, boolean isResultString) {
        HttpURLConnection uc = null;
        try {
            uc = (HttpURLConnection) openConnection(urlString, user, pass, DELETE, null, robustMode);
            if (uc == null) return Boolean.FALSE.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return Boolean.FALSE.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return Boolean.FALSE.toString();
        }
        if (isResultString) {
            try {
                return convertStreamToString(uc.getInputStream());
            } catch (IOException e) {
                LOGGER.error("The convert from InputStream to String failed: " + e.getMessage());
                return Boolean.FALSE.toString();
            }
        } else {
            return Boolean.TRUE.toString();
        }
    }
