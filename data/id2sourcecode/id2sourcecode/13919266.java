    @Override
    public List<UUID> searchUUIDWithTag(String tag) {
        List<UUID> retUUIDList = new ArrayList<UUID>();
        try {
            URL url = new URL("http://feeler.bennu.tw/feeler-webapp/mvc/uuid/tags/listuuid?tag=" + tag.trim());
            BufferedReader urlBr = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
            String retJsonString = urlBr.readLine();
            urlBr.close();
            if (retJsonString != null && !retJsonString.isEmpty()) {
                JSONArray jsonArr = new JSONArray(retJsonString);
                for (int i = 0; i < jsonArr.length(); i++) {
                    UUID uuid = UUID.fromString(jsonArr.getString(i));
                    retUUIDList.add(uuid);
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
        return retUUIDList;
    }
