    @Override
    public List<String> listUUIDTag(UUID boxUUID) {
        List<String> retTagList = new ArrayList<String>();
        try {
            URL url = new URL("http://feeler.bennu.tw/feeler-webapp/mvc/uuid/tags/listtag?box=" + boxUUID.toString().trim());
            BufferedReader urlBr = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
            String retJsonString = urlBr.readLine();
            urlBr.close();
            if (retJsonString != null && !retJsonString.isEmpty()) {
                JSONArray jsonArr = new JSONArray(retJsonString);
                for (int i = 0; i < jsonArr.length(); i++) {
                    retTagList.add(jsonArr.getString(i));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retTagList;
    }
