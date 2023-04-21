    public String[] login(HttpServletRequest req, HttpServletResponse res) throws AutoLoginException {
        String[] credentials = null;
        try {
            long companyId = PortalUtil.getCompanyId(req);
            if (!PrefsPropsUtil.getBoolean(companyId, PropsUtil.OPEN_SSO_AUTH_ENABLED)) {
                return credentials;
            }
            HttpSession ses = req.getSession();
            String subjectId = (String) ses.getAttribute(WebKeys.OPEN_SSO_LOGIN);
            if (subjectId == null) {
                return credentials;
            }
            Map nameValues = new HashMap();
            String serviceUrl = PrefsPropsUtil.getString(companyId, PropsUtil.OPEN_SSO_SERVICE_URL);
            String url = serviceUrl + "/attributes?subjectid=" + HttpUtil.encodeURL(subjectId);
            URL urlObj = new URL(url);
            URLConnection con = urlObj.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream) con.getContent()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if ((parts == null) || (parts.length != 2)) {
                    continue;
                }
                String attrName = null;
                String attrValue = null;
                if (parts[0].endsWith("name")) {
                    attrName = parts[1];
                    line = reader.readLine();
                    if (line == null) {
                        throw new AutoLoginException("Error reading user attributes");
                    }
                    parts = line.split("=");
                    if ((parts == null) || (parts.length != 2) || (!parts[0].endsWith("value"))) {
                        attrValue = null;
                    } else {
                        attrValue = parts[1];
                    }
                    nameValues.put(attrName, attrValue);
                }
            }
            String firstName = (String) nameValues.get("cn");
            String lastName = (String) nameValues.get("sn");
            String screenName = (String) nameValues.get("givenname");
            String emailAddress = (String) nameValues.get("mail");
            User user = null;
            try {
                user = UserLocalServiceUtil.getUserByEmailAddress(companyId, emailAddress);
            } catch (NoSuchUserException nsue) {
                ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);
                user = addUser(companyId, firstName, lastName, emailAddress, screenName, themeDisplay.getLocale());
            }
            credentials = new String[3];
            credentials[0] = String.valueOf(user.getUserId());
            credentials[1] = user.getPassword();
            credentials[2] = Boolean.TRUE.toString();
        } catch (Exception e) {
            _log.error(e.getMessage());
        }
        return credentials;
    }
