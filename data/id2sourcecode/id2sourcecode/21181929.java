    protected String processPath(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String path = super.processPath(req, res);
        HttpSession ses = req.getSession();
        String companyId = PortalUtil.getCompanyId(req);
        Map currentUsers = (Map) WebAppPool.get(companyId, WebKeys.CURRENT_USERS);
        UserTracker userTracker = (UserTracker) currentUsers.get(ses.getId());
        if ((userTracker != null) && ((path != null) && (!path.equals(_PATH_C)) && (path.indexOf(_PATH_J_SECURITY_CHECK) == -1) && (path.indexOf(_PATH_PORTAL_PROTECTED) == -1))) {
            StringBuffer fullPathSB = new StringBuffer();
            fullPathSB.append(path);
            fullPathSB.append(StringPool.QUESTION);
            fullPathSB.append(req.getQueryString());
            userTracker.addPath(new UserTrackerPath(userTracker.getUserTrackerId(), userTracker.getUserTrackerId(), fullPathSB.toString(), new Date()));
        }
        String userId = req.getRemoteUser();
        User user = null;
        try {
            user = PortalUtil.getUser(req);
        } catch (Exception e) {
        }
        if ((path != null) && (path.equals(_PATH_PORTAL_LAYOUT) || path.equals(_PATH_PORTAL_PUBLIC_LAYOUT))) {
            String strutsAction = req.getParameter("_2_struts_action");
            if (strutsAction == null || !strutsAction.equals(_PATH_MY_ACCOUNT_CREATE_ACCOUNT)) {
                Map parameterMap = new LinkedHashMap();
                if (req instanceof UploadServletRequest) {
                    UploadServletRequest uploadServletReq = (UploadServletRequest) req;
                    Enumeration paramNames = uploadServletReq.getParameterNames();
                    while (paramNames.hasMoreElements()) {
                        String paramName = (String) paramNames.nextElement();
                        if (uploadServletReq.getFile(paramName) == null) {
                            parameterMap.put(paramName, uploadServletReq.getParameterValues(paramName));
                        }
                    }
                } else {
                    parameterMap = req.getParameterMap();
                }
                ses.setAttribute(WebKeys.LAST_PATH, new ObjectValuePair(path, new LinkedHashMap(parameterMap)));
            }
        }
        if ((userId == null) && (ses.getAttribute("j_username") == null)) {
            try {
                String[] autoLogins = PropsUtil.getArray(PropsUtil.AUTO_LOGIN_HOOKS);
                for (int i = 0; i < autoLogins.length; i++) {
                    AutoLogin autoLogin = (AutoLogin) InstancePool.get(autoLogins[i]);
                    String[] credentials = autoLogin.login(req, res);
                    if ((credentials != null) && (credentials.length == 3)) {
                        String jUsername = credentials[0];
                        String jPassword = credentials[1];
                        boolean encPwd = GetterUtil.getBoolean(credentials[2]);
                        if (Validator.isNotNull(jUsername) && Validator.isNotNull(jPassword)) {
                            ses.setAttribute("j_username", jUsername);
                            if (encPwd) {
                                ses.setAttribute("j_password", jPassword);
                            } else {
                                ses.setAttribute("j_password", Encryptor.digest(jPassword));
                                ses.setAttribute(WebKeys.USER_PASSWORD, jPassword);
                            }
                            return _PATH_PORTAL_PUBLIC_LOGIN;
                        }
                    }
                }
            } catch (AutoLoginException ale) {
                Logger.error(this, ale.getMessage(), ale);
            }
        }
        if ((userId != null || user != null) && (path != null) && (path.equals(_PATH_PORTAL_LOGOUT))) {
            return _PATH_PORTAL_LOGOUT;
        }
        if ((userId != null || user != null) && (path != null) && (path.equals(_PATH_PORTAL_UPDATE_TERMS_OF_USE))) {
            return _PATH_PORTAL_UPDATE_TERMS_OF_USE;
        }
        if ((userId != null) && (user == null)) {
            return _PATH_PORTAL_LOGOUT;
        }
        if ((user != null) && (!user.isAgreedToTermsOfUse())) {
            boolean termsOfUseRequired = GetterUtil.get(PropsUtil.get(PropsUtil.TERMS_OF_USE_REQUIRED), true);
            if (termsOfUseRequired) {
                return _PATH_PORTAL_TERMS_OF_USE;
            }
        }
        if ((user != null) && (!user.isActive())) {
            SessionErrors.add(req, UserActiveException.class.getName());
            return _PATH_PORTAL_ERROR;
        }
        boolean simultaenousLogins = GetterUtil.get(PropsUtil.get(PropsUtil.AUTH_SIMULTANEOUS_LOGINS), true);
        if (!simultaenousLogins) {
            Boolean staleSession = (Boolean) ses.getAttribute(WebKeys.STALE_SESSION);
            if ((user != null) && (staleSession != null && staleSession.booleanValue() == true)) {
                return _PATH_PORTAL_ERROR;
            }
        }
        if ((user != null) && (user.isPasswordReset())) {
            return _PATH_PORTAL_CHANGE_PASSWORD;
        }
        if ((user != null) && (!user.hasLayouts())) {
            boolean universalPersonalization = GetterUtil.get(PropsUtil.get(PropsUtil.UNIVERSAL_PERSONALIZATION), false);
            boolean powerUser = true;
            try {
                if (!universalPersonalization) {
                    powerUser = RoleLocalManagerUtil.isPowerUser(user.getUserId());
                }
            } catch (Exception e) {
                powerUser = false;
            }
            boolean hasGroupPages = false;
            try {
                if (user.getAllLayouts().size() > 0) {
                    hasGroupPages = true;
                }
            } catch (Exception e) {
            }
            if ((powerUser && !hasGroupPages) || path.contains("/personalize")) {
                return _PATH_PORTAL_ADD_PAGE;
            } else {
                try {
                    if (user.getAllLayouts().size() > 0) {
                        hasGroupPages = true;
                    }
                } catch (Exception e) {
                }
                if (!hasGroupPages) {
                    SessionErrors.add(req, RequiredLayoutException.class.getName());
                    return _PATH_PORTAL_ERROR;
                }
            }
        }
        if (!isPublicPath(path)) {
            if (user == null) {
                SessionErrors.add(req, PrincipalException.class.getName());
                return _PATH_PORTAL_PUBLIC_LOGIN;
            }
        }
        ActionMapping mapping = (ActionMapping) moduleConfig.findActionConfig(path);
        if (path.startsWith(_PATH_WSRP)) {
            path = _PATH_WSRP;
        } else {
            path = mapping.getPath();
        }
        if (user != null) {
            try {
                if (false) {
                    SessionErrors.add(req, RequiredRoleException.class.getName());
                    return _PATH_PORTAL_ERROR;
                }
            } catch (Exception e) {
                Logger.error(this, e.getMessage(), e);
            }
        }
        if (isPortletPath(path)) {
            try {
                String strutsPath = path.substring(1, path.lastIndexOf(StringPool.SLASH));
                Portlet portlet = PortletManagerUtil.getPortletByStrutsPath(companyId, strutsPath);
                if (portlet != null && portlet.isActive()) {
                    defineObjects(req, res, portlet);
                }
            } catch (Exception e) {
                req.setAttribute(PageContext.EXCEPTION, e);
                path = _PATH_COMMON_ERROR;
            }
        }
        return path;
    }
