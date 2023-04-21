    public JSONObject sendAndReceive(JSONObject message) {
        try {
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            Writer request = new OutputStreamWriter(connection.getOutputStream());
            request.write(message.toString());
            request.close();
            StringBuffer builder = new StringBuffer(1024);
            char[] buffer = new char[1024];
            Reader reader = new InputStreamReader(connection.getInputStream());
            while (true) {
                int bytesRead = reader.read(buffer);
                if (bytesRead < 0) break;
                builder.append(buffer, 0, bytesRead);
            }
            reader.close();
            JSONTokener tokener = new JSONTokener(builder.toString());
            Object rawResponseMessage = tokener.nextValue();
            JSONObject responseMessage = (JSONObject) rawResponseMessage;
            if (responseMessage == null) throw new ClientError("Invalid response type - " + rawResponseMessage.getClass());
            return responseMessage;
        } catch (IOException ex) {
            throw new ClientError(ex);
        } catch (JSONException ex) {
            throw new ClientError(ex);
        }
    }
