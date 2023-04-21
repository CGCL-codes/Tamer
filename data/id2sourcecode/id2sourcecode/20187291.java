    private void simpleSend() throws PositioningAPIException {
        try {
            URL url = new URL(theHost);
            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            OutputStreamWriter outputStream = new OutputStreamWriter(connection.getOutputStream());
            String data = "protocolData=" + request;
            outputStream.write(data, 0, data.length());
            outputStream.flush();
            outputStream.close();
            String responseLine;
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = "";
            while (true) {
                responseLine = inputStream.readLine();
                if (responseLine == null) break;
                response += responseLine;
            }
            if (response.equals("")) {
                LogServiceManager.getLog("positioningAPI, LASPositioningDataCollector").println(Log.ERROR, "No data returned in answer from LAS");
                throw new ExternalException(positioningSystem, "No data returned in answer from LAS");
            }
            handleResponse(response);
            inputStream.close();
        } catch (IOException ioe) {
            LogServiceManager.getLog("positioningAPI, LASPositioningDataCollector").println(Log.ERROR, "Error in communication with LAS positioning server: ", ioe);
            throw new PositioningAPIException(ioe.toString());
        }
    }
