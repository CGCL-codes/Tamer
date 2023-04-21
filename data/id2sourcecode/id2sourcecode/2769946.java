    private HashInfo createHash(String id) {
        try {
            URL url = new URL("http://api.jamendo.com/get2/stream/track/redirect/?id=" + id + "&streamencoding=mp31");
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(30 * 1000);
            connection.setReadTimeout(30 * 1000);
            connection.connect();
            int length = connection.getContentLength();
            if (length == 0) return new HashInfoFailure(id, UnknowHashReason.UNKNOWN_JAMENDO_ID);
            return new HashInfoSuccess(id, JamendoUtils.getHash(connection.getInputStream()));
        } catch (MalformedURLException e) {
            return new HashInfoFailure(id, UnknowHashReason.UNKNOWN_JAMENDO_ID);
        } catch (FileNotFoundException e) {
            return new HashInfoFailure(id, UnknowHashReason.UNKNOWN_JAMENDO_ID);
        } catch (IOException e) {
            return new HashInfoFailure(id, UnknowHashReason.UNKNOWN_JAMENDO_ID);
        }
    }
