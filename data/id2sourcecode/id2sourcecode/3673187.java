    public void login(String username, String password) throws MalformedURLException, IOException, UnexpectedResponseCodeException, UnauthorizedResponseException, ForbiddenResponseException, BadRequestResponseException, ServiceUnavailableException {
        HttpPost postRequest = new HttpPost("https://store.playstation.com/external/login.action");
        addDefaultHeaders(postRequest);
        List<NameValuePair> parametersBody = new ArrayList<NameValuePair>();
        parametersBody.add(new BasicNameValuePair("loginName", username));
        parametersBody.add(new BasicNameValuePair("password", password));
        parametersBody.add(new BasicNameValuePair("returnURL", "https://us.playstation.com/uwps/PSNTicketRetrievalGenericServlet"));
        postRequest.setEntity(new UrlEncodedFormEntity(parametersBody, HTTP.UTF_8));
        HttpResponse response = httpClient.execute(postRequest, context);
        response.getEntity().consumeContent();
        String location;
        switch(response.getStatusLine().getStatusCode()) {
            case 302:
                location = response.getHeaders("Location")[0].getValue();
                sessionId = location.substring(location.indexOf(SESSION_ID) + SESSION_ID.length());
                HttpGet request = new HttpGet("http://us.playstation.com/uwps/PSNTicketRetrievalGenericServlet?psnAuth=true&sessionId=" + sessionId);
                addDefaultHeaders(request);
                response = httpClient.execute(request, context);
                response.getEntity().consumeContent();
                isLoggedIn = isLoggedIn(response);
                break;
            case 200:
                throw new UnauthorizedResponseException();
            case 401:
            case 403:
                throw new ForbiddenResponseException();
            case 400:
            case 402:
            case 404:
            case 405:
            case 406:
            case 410:
            case 501:
            case 502:
                throw new BadRequestResponseException();
            case 500:
            case 503:
                throw new ServiceUnavailableException();
            default:
                throw new UnexpectedResponseCodeException(response.getStatusLine().getStatusCode());
        }
    }
