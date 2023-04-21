    @Override
    public void modUUIDTag(UUID boxUUID, String type, String tag) {
        try {
            URL url = new URL("http://feeler.bennu.tw/feeler-webapp/mvc/uuid/tags/modtag?box=" + boxUUID.toString().trim() + "&type=" + type.toString() + "&tag=" + URLEncoder.encode(tag.trim(), "utf-8"));
            BufferedReader urlBr = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
            urlBr.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
