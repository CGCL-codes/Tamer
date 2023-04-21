    public static ReturnString sendData(SendableData iSendData) {
        InputStream is = null;
        StringBuffer sb = null;
        HttpConnection http = null;
        String URL = SERVER_URL + "sf.php?a=" + iSendData.mobileNumber + "&b=i";
        OutputStream os = null;
        try {
            URL = encodeURL(URL);
            http = (HttpConnection) (Connector.open(URL));
            http.setRequestMethod(HttpConnection.POST);
            http.setRequestProperty("User-Agent", "Profile/MIDP-2.0 Configuration/CLDC-1.0");
            http.setRequestProperty("Content-Language", "en-US");
            os = http.openDataOutputStream();
            os.write(iSendData.readData);
            if (http.getResponseCode() == HttpConnection.HTTP_OK) {
                sb = new StringBuffer();
                int ch;
                is = http.openInputStream();
                while ((ch = is.read()) != -1) sb.append((char) ch);
            } else {
                return new ReturnString(false, null);
            }
        } catch (IOException e) {
            return new ReturnString(false, null);
        } finally {
            if (os != null) try {
                os.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (is != null) try {
                is.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (sb != null) {
                return new ReturnString(true, sb.toString());
            }
            if (http != null) try {
                http.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return new ReturnString(false, null);
    }
