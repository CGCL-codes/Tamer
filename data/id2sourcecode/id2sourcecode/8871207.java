    @SuppressWarnings("unchecked")
    public void sync(Cipher anEncryptionCipher, String encoding) throws DataAccessException, JSONException, ClientProtocolException, IOException, InterruptedException, QCSynchronizationException {
        if (!loggedIn) {
            try {
                HttpPost httppost = new HttpPost(remoteURL);
                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(new BasicNameValuePair("cmd", "login"));
                nameValuePairList.add(new BasicNameValuePair("uname", this.remoteUname));
                nameValuePairList.add(new BasicNameValuePair("pword", this.remotePword));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairList, HTTP.UTF_8));
                System.out.println("executing login request " + httppost.getRequestLine());
                HttpResponse response = httpClient.execute(httppost, localContext);
                if (response.getStatusLine().getStatusCode() / 200 == 1) {
                    HttpEntity responseEntity = response.getEntity();
                    String JSONString = EntityUtils.toString(responseEntity, HTTP.UTF_8);
                    ArrayList<Object> resultList = (ArrayList<Object>) JSONUtilities.parse(JSONString);
                    HashMap<String, String> resultMap = (HashMap<String, String>) resultList.get(0);
                    String error = resultMap.get("sync_error");
                    if (error != null) {
                        throw new IOException(error);
                    } else {
                        loggedIn = true;
                        System.out.println("successful login");
                    }
                } else {
                    throw new IOException("Invalid user name or password");
                }
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
        System.out.println("login success.  about to query.");
        startTransaction();
        System.out.println("after transaction start");
        DataAccessResult syncValuesResult = DataAccessObject.getData(theActivity, dbName, "SELECT * FROM sync_values", null);
        ArrayList<ArrayList<String>> syncValues = syncValuesResult.getResults();
        DataAccessResult lastSyncResult = DataAccessObject.getData(theActivity, dbName, "SELECT last_sync FROM sync_info", null);
        String lastSync = "1970-01-01 00:00:00";
        if (lastSyncResult.getResults().size() > 0) {
            lastSync = lastSyncResult.getResults().get(0).get(0);
        }
        SyncData theDataToSync = new SyncData(lastSync, syncValues);
        String JSONString = null;
        if (anEncryptionCipher == null) {
            JSONString = JSONUtilities.stringify(theDataToSync);
        } else {
            JSONString = JSONUtilities.stringify(theDataToSync, anEncryptionCipher);
        }
        try {
            HttpPost httppost = new HttpPost(remoteURL);
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            nameValuePairList.add(new BasicNameValuePair("cmd", "sync"));
            nameValuePairList.add(new BasicNameValuePair("data", JSONString));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairList, HTTP.UTF_8));
            System.out.println("executing request " + httppost.getRequestLine());
            HttpResponse response = httpClient.execute(httppost, localContext);
            if (response.getStatusLine().getStatusCode() / 200 == 1) {
                HttpEntity responseEntity = response.getEntity();
                String result = EntityUtils.toString(responseEntity, HTTP.UTF_8);
                System.out.println("JSON: " + result);
                ArrayList<Object> resultList = (ArrayList<Object>) JSONUtilities.parse(result);
                HashMap<String, String> syncResultMap = (HashMap<String, String>) resultList.get(0);
                if (syncResultMap.get("sync_error") != null && !((Object) syncResultMap.get("sync_response")).equals("data_success")) {
                    throw new QCSynchronizationException((String) syncResultMap.get("sync_error"));
                }
                HashMap<String, Object> dataMap = (HashMap<String, Object>) resultList.get(1);
                String lastSyncTime = (String) dataMap.get("sync_time");
                ArrayList<HashMap<String, Object>> dataList = (ArrayList<HashMap<String, Object>>) dataMap.get("sync_data");
                Object[] preparedStatementParameters = new Object[1];
                preparedStatementParameters[0] = lastSyncTime;
                try {
                    DataAccessResult updateResult = DataAccessObject.setData(theActivity, dbName, "INSERT OR REPLACE INTO sync_info VALUES(0,?)", preparedStatementParameters);
                    for (HashMap<String, Object> syncDatum : dataList) {
                        String sqlKey = (String) syncDatum.get("key");
                        ArrayList<String> sqlParameterList = (ArrayList<String>) syncDatum.get("syncInfo");
                        String sql = registeredSQLStatements.get(sqlKey);
                        updateResult = DataAccessObject.setData(theActivity, dbName, sql, sqlParameterList.toArray());
                    }
                } catch (Exception e) {
                    throw new QCSynchronizationException(e.getLocalizedMessage() + " " + e.getCause());
                }
                this.clearSync();
            } else {
                throw new IOException(response.getStatusLine().toString());
            }
        } finally {
            endTransaction();
        }
    }
