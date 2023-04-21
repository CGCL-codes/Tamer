    public static String getRealLocation(String address, int depthToLocate) {
        String realAddress = address;
        if (depthToLocate == 0) {
            return realAddress;
        }
        try {
            URL url = new URL(address);
            URLConnection conn = url.openConnection();
            String location = conn.getHeaderField("Location");
            if (location == null) {
                return realAddress;
            } else {
                return getRealLocation(location, depthToLocate - 1);
            }
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
            logger.debug(e.getStackTrace());
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.debug(e.getStackTrace());
        }
        return realAddress;
    }
