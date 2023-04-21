    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpGet httpget = new HttpGet("https://openapi.baidu.com/oauth/2.0/token?grant_type=authorization_code&code=" + request.getParameter("code") + "&client_id=ot00cKk2xAAWsxHyCwmGC9Af&client_secret=xCY3cPnOreRq7eXwixFqOAjSlMIWdomk&redirect_uri=http%3A%2F%2Fwww.ever365.com%2Foauth%2Fbaidu");
            HttpResponse hr = httpclient.execute(httpget);
            String rawString = FileCopyUtils.copyToString(new InputStreamReader(hr.getEntity().getContent()));
            JSONObject jso = new JSONObject(rawString);
            if (!jso.isNull("access_token")) {
                String at = jso.getString("access_token");
                request.getSession().setAttribute("_baidu_access_token", jso);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("session_key", jso.getString("session_key"));
                params.put("timestamp", URLEncoder.encode(dateformat.format(new Date())));
                String sign = getSignature(params, jso.getString("session_secret"));
                HttpGet userInfoGet = new HttpGet("https://openapi.baidu.com/rest/2.0/passport/users/getLoggedInUser?" + "access_token=" + jso.getString("access_token") + "&format=json");
                HttpResponse userinfohr = httpclient.execute(userInfoGet);
                JSONObject userinfo = new JSONObject(FileCopyUtils.copyToString(new InputStreamReader(userinfohr.getEntity().getContent())));
                if (!userinfo.isNull("uname")) {
                    request.getSession().setAttribute(SetUserFilter.AUTHENTICATION_USER, userinfo.getString("uname") + "@baidu");
                    if (request.getSession().getAttribute("rediretTo") != null) {
                        response.sendRedirect((String) request.getSession().getAttribute("rediretTo"));
                        return;
                    } else {
                        response.sendRedirect("/");
                    }
                } else {
                    response.sendRedirect("/");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }
