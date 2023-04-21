    private void checkServerStatus() throws IcdException {
        try {
            HttpResponse resp = executeHttpRequest(createHttpGet(Config.getServerStatusUrl()));
            int statusCode = resp.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String content = EntityUtils.toString(resp.getEntity());
                content = new String(content.getBytes("ISO8859-1"), "UTF-8");
                if (DEBUG) Log.d(TAG, content);
                JSONObject json = new JSONObject(content);
                if (json.getInt("serverStatus") != 1) {
                    String subject = json.getString("subject");
                    String message = json.getString("message");
                    throw new IcdException(subject + "\n" + message);
                }
            }
        } catch (ParseException e) {
        } catch (IOException e) {
        } catch (JSONException e) {
        }
    }
