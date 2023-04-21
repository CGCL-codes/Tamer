    @Override
    public void run() {
        _taskStatus = ETaskStatus.Started;
        _startTimeMillis = System.currentTimeMillis();
        try {
            appendParamsToURL();
            HttpURLConnection connection = (HttpURLConnection) new URL(_url).openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("FAPUser", "fap_test@gmail.com");
            for (Entry<EServletReqParam, String> propertyMapEntry : _propertyMap.entrySet()) {
                connection.setRequestProperty(propertyMapEntry.getKey().getParamName(), propertyMapEntry.getValue());
            }
            connection.connect();
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(_messageBody == null ? "" : _messageBody);
            writer.close();
            _httpResponseCode = connection.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            do {
                inputLine = reader.readLine();
                if (inputLine != null) {
                    _responseLineList.add(inputLine);
                }
            } while (inputLine != null);
            reader.close();
            connection.disconnect();
        } catch (Exception e) {
            _resultException = e;
        }
        if (_httpResponseCode == HttpURLConnection.HTTP_OK && _resultException == null) {
            _finishTimeMillis = System.currentTimeMillis();
            _taskStatus = ETaskStatus.CompletedWithNoErrors;
        } else {
            GLLog.warning("Request failed for:" + toString() + (_httpResponseCode == HttpURLConnection.HTTP_OK ? " (exception thrown)" : " (response code:" + _httpResponseCode + ")"), _resultException);
            _taskStatus = ETaskStatus.CompletedWithErrors;
        }
        if (_clientRequestThread != null) {
            _clientRequestThread.processRequestCompletion(this);
        }
    }
