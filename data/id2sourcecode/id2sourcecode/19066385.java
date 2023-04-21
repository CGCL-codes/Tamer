    public void doDicomAuthentication() {
        if (!manageStudyPermissions && !useStudyPermissions) return;
        HttpURLConnection urlConnection = null;
        InputStream in = null;
        try {
            urlConnection = (HttpURLConnection) new URL(WebCfgDelegate.getInstance().getDicomSecurityServletUrl()).openConnection();
            WebRequest webRequest = ((WebRequest) RequestCycle.get().getRequest());
            Cookie[] cookies = webRequest.getCookies();
            if (cookies == null) return;
            StringBuffer cookieValue = new StringBuffer();
            for (Cookie cookie : Arrays.asList(cookies)) {
                cookieValue.append(cookie.getName()).append("=").append(cookie.getValue()).append("; ");
            }
            if (cookieValue.length() > 0) cookieValue.delete(cookieValue.length() - 2, cookieValue.length() - 1);
            urlConnection.setRequestProperty("Cookie", cookieValue.toString());
            urlConnection.connect();
            in = urlConnection.getInputStream();
            Reader reader = new InputStreamReader(in);
            char[] buffer = new char[1];
            StringBuffer returnValue = new StringBuffer();
            while ((reader.read(buffer)) >= 0) returnValue.append(buffer);
            reader.close();
            if (returnValue.length() > 0) setDicomRoles(returnValue.toString());
        } catch (IOException e) {
            log.error(getClass().getName() + ": ", e);
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
                log.error(getClass().getName() + ": Could not close input stream of url connection: ", e);
            }
            if (urlConnection != null) urlConnection.disconnect();
        }
    }
