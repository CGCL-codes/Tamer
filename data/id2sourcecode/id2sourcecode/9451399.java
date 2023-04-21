    private static Object doHttpRequest(String URL, String httpMethod, String postValue) throws IOException {
        System.out.println("doHttpRequest: " + URL + " " + httpMethod + " " + postValue);
        HttpConnection connection = null;
        DataInputStream input = null;
        DataOutputStream output = null;
        if (httpMethod.equals(HttpConnection.GET) || httpMethod.equals("DELETE")) {
            System.out.println("  " + httpMethod + " = readonly");
            connection = (HttpConnection) Connector.open(URL);
            connection.setRequestMethod(httpMethod);
        } else {
            System.out.println("  " + httpMethod + " = read+write");
            connection = (HttpConnection) Connector.open(URL, Connector.READ_WRITE);
            connection.setRequestMethod(httpMethod);
            connection.setRequestProperty("Content-Type", "application/xml; charset=utf-8");
            if (postValue != null) {
                output = connection.openDataOutputStream();
                byte[] request_body = postValue.getBytes();
                for (int i = 0; i < request_body.length; i++) {
                    output.writeByte(request_body[i]);
                }
            }
        }
        input = new DataInputStream(connection.openInputStream());
        Object returnObject;
        returnObject = processXmlDom(input);
        try {
            if (connection != null) connection.close();
            if (input != null) input.close();
            if (output != null) output.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return returnObject;
    }
