    @Override
    protected boolean authenticate(Request request, Response response, LoginConfig loginConfig) throws IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Principal principal = httpRequest.getUserPrincipal();
        if (principal != null && principal.getName() != null) {
            return true;
        }
        String codeParameter = httpRequest.getParameter("code");
        if (codeParameter == null) {
            String login = httpRequest.getParameter("mk_login");
            if (login == null) {
                String currentUri = httpRequest.getRequestURL().toString();
                if ("/vk/private/login/".equalsIgnoreCase(httpRequest.getRequestURI())) {
                    response.sendRedirect("/vk/index.jsp");
                } else {
                    String page = "/vk/login.jsp?url=" + currentUri;
                    response.sendRedirect(page);
                }
                return false;
            } else {
                String currentUri = httpRequest.getRequestURL().toString();
                String queryString = httpRequest.getQueryString();
                if (queryString != null && !"".equals(queryString) && queryString.contains("&")) {
                    String query = "?";
                    String[] paramPairArray = queryString.split("&");
                    for (String paramPair : paramPairArray) {
                        if (!paramPair.startsWith("login=")) {
                            if (query.length() != 1) query += "&";
                            query += paramPair;
                        }
                    }
                    currentUri += query;
                }
                String encodedCurrentUri = URLEncoder.encode(currentUri, "UTF-8");
                String myuriredirect = httpRequest.getParameter("myuriredirect");
                if (myuriredirect != null) {
                    encodedCurrentUri = URLEncoder.encode(myuriredirect, "UTF-8");
                }
                String redirectUri = "moyakarta.dyndns.org/vk/private/login/%3F" + "myuriredirect" + "%3D" + encodedCurrentUri;
                String uri = "https://api.vkontakte.ru/oauth/authorize?" + "client_id=" + APP_ID + "&scope=" + SCOPE + "&redirect_uri=" + redirectUri + "&display=" + DISPLAY_TYPE + "&response_type=code";
                response.sendRedirect(uri);
                return false;
            }
        } else {
            String accessTokenUri = "https://api.vkontakte.ru/oauth/access_token" + "?" + "client_id=" + APP_ID + "&" + "client_secret=" + APP_SECRET + "&" + "code=" + codeParameter;
            String responseAsString = null;
            try {
                URL url = new URL(accessTokenUri);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");
                InputStream inputStream = conn.getInputStream();
                responseAsString = convertStreamToString(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Map<String, String> params = parseJsonAsParams(responseAsString);
            String vkUserId = params.get("user_id");
            if (vkUserId != null) {
                String username = "vk_" + vkUserId;
                principal = new GenericPrincipal(null, username, "N/P", roles);
                register(request, response, principal, "", principal.getName(), "N/P");
                return true;
            } else {
                String page = "/vk/index.jsp";
                response.sendRedirect(page);
                return false;
            }
        }
    }
