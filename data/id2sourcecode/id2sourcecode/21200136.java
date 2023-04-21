    public InputStream execute() throws ConnectorException {
        try {
            StringBuffer sUrl = new StringBuffer(this.url.toString());
            if (this.parametersGET.size() > 0) {
                sUrl.append("?");
                for (String key : this.parametersGET.keySet()) {
                    sUrl.append(key);
                    sUrl.append("=");
                    sUrl.append(this.parametersGET.get(key).getValue());
                    sUrl.append("&");
                }
                sUrl.deleteCharAt(sUrl.length() - 1);
            }
            this.url = new URL(sUrl.toString());
            HttpURLConnection connection = (HttpURLConnection) this.url.openConnection();
            if (this.parametersPOST.size() > 0) {
                connection.setRequestMethod("POST");
                for (POSTParameter parameter : this.parametersPOST.values()) {
                    connection.addRequestProperty(parameter.getKey(), parameter.getValue());
                }
            }
            try {
                return connection.getInputStream();
            } catch (IOException except) {
                int response = connection.getResponseCode();
                String rText = this.getErrorResponseText(connection);
                switch(response) {
                    case HttpURLConnection.HTTP_BAD_REQUEST:
                        rText = (rText == null) ? this.createErrorResponseText(this.HTTP_BAD_REQUEST_ERROR) : rText;
                        throw new BadRequestException(rText);
                    case HttpURLConnection.HTTP_UNAUTHORIZED:
                        rText = (rText == null) ? this.createErrorResponseText(this.HTTP_UNAUTHORIZED_ERROR) : rText;
                        throw new AuthenticationException(rText);
                    case HttpURLConnection.HTTP_FORBIDDEN:
                        rText = (rText == null) ? this.createErrorResponseText(this.HTTP_FORBIDDEN_ERROR) : rText;
                        throw new ConnectorException(rText);
                    case HttpURLConnection.HTTP_NOT_FOUND:
                        rText = (rText == null) ? this.createErrorResponseText(this.HTTP_NOT_FOUND_ERROR) : rText;
                        throw new URLNotFoundException(rText);
                    case HttpURLConnection.HTTP_INTERNAL_ERROR:
                        rText = (rText == null) ? this.createErrorResponseText(this.HTTP_INTERNAL_ERROR) : rText;
                        throw new InternalServerException(rText);
                    case HttpURLConnection.HTTP_BAD_GATEWAY:
                        rText = (rText == null) ? this.createErrorResponseText(this.HTTP_BAD_GATEWAY_ERROR) : rText;
                        throw new BadGatewayException(rText);
                    case HttpURLConnection.HTTP_UNAVAILABLE:
                        rText = (rText == null) ? this.createErrorResponseText(this.HTTP_SERVICE_UNAVAILABLE_ERROR) : rText;
                        throw new ServiceUnavailableException(rText);
                    default:
                        throw new ConnectorException("Something went wrong");
                }
            }
        } catch (ProtocolException e) {
            throw new ConnectorException(e);
        } catch (MalformedURLException e) {
            throw new ConnectorException(e);
        } catch (IOException e) {
            throw new ConnectorException(e);
        }
    }
