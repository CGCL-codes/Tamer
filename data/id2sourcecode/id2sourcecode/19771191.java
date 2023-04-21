    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("doPost");
        PrintWriter writer = response.getWriter();
        String globalActionName = request.getParameter("GlobalActionName");
        if (null != globalActionName) {
            handleGlobalAction(globalActionName, writer, request, response);
            return;
        }
        String ajaxActionName = request.getParameter("AjaxActionName");
        if (null != ajaxActionName) {
            handleAjaxAction(ajaxActionName, writer, request, response);
            return;
        }
        String pageClassName = request.getParameter("PageClass");
        LOG.debug("page class: " + pageClassName);
        if (null == pageClassName) {
            writer.println("need a PageClass parameter");
            return;
        }
        Class<?> pageClass = loadClass(pageClassName);
        String actionName = request.getParameter("ActionName");
        LOG.debug("action name: " + actionName);
        if (null == actionName) {
            writer.println("need an ActionName parameter");
            return;
        }
        Method actionMethod;
        if (-1 != actionName.indexOf("(")) {
            LOG.debug("table action requested");
            String tableFieldName = actionName.substring(actionName.indexOf("(") + 1, actionName.indexOf("."));
            LOG.debug("table field name: " + tableFieldName);
            Field tableField;
            try {
                tableField = pageClass.getDeclaredField(tableFieldName);
            } catch (Exception e) {
                writer.println("field not found: " + tableFieldName);
                return;
            }
            if (false == List.class.equals(tableField.getType())) {
                writer.println("field is not a list: " + tableFieldName);
                return;
            }
            Integer tableActionIdx = Integer.parseInt(actionName.substring(actionName.indexOf(".") + 1, actionName.indexOf(")")));
            LOG.debug("table action idx: " + tableActionIdx);
            String tableEntryType = request.getParameter(tableFieldName + "." + tableActionIdx);
            LOG.debug("table entry type: " + tableEntryType);
            Class<?> tableEntryClass = loadClass(tableEntryType);
            String actionMethodName = actionName.substring(0, actionName.indexOf("("));
            try {
                actionMethod = pageClass.getDeclaredMethod(actionMethodName, new Class[] { tableEntryClass });
            } catch (Exception e) {
                LOG.debug("no action method found for name: " + actionMethodName);
                writer.println("no action method found for name: " + actionMethodName);
                return;
            }
        } else {
            try {
                actionMethod = pageClass.getDeclaredMethod(actionName, new Class[] {});
            } catch (Exception e) {
                writer.println("no action method found for name: " + actionName);
                return;
            }
        }
        Action actionAnnotation = actionMethod.getAnnotation(Action.class);
        if (null == actionAnnotation) {
            writer.println("action method not annotated with @Action: " + actionName);
            return;
        }
        HttpSession session = request.getSession();
        Object page;
        try {
            page = createPage(pageClass, request, response);
        } catch (Exception e) {
            writer.println("could not init page: " + pageClassName + ". Missing default constructor?");
            return;
        }
        Map<String, String> conversionErrors;
        try {
            conversionErrors = restorePageFromRequest(request, pageClass, page);
        } catch (Exception e) {
            LOG.debug("error on restore: " + e.getMessage(), e);
            throw new ServletException("error on restore: " + e.getMessage(), e);
        }
        if (false == conversionErrors.isEmpty()) {
            LOG.debug("conversion errors: " + conversionErrors);
            outputPage(pageClass, page, session, null, conversionErrors, writer);
            return;
        }
        if (false == actionAnnotation.skipConstraints()) {
            Map<String, String> constraintViolationFieldNames = checkConstraints(pageClass, page);
            if (false == constraintViolationFieldNames.isEmpty()) {
                outputPage(pageClass, page, session, null, constraintViolationFieldNames, writer);
                return;
            }
        }
        Authenticated authenticatedAnnotation = actionMethod.getAnnotation(Authenticated.class);
        if (null != authenticatedAnnotation) {
            LOG.debug("authentication required");
            String username = (String) session.getAttribute(USERNAME_SESSION_ATTRIBUTE);
            if (null == username) {
                LOG.debug("login required");
                LoginPage loginPage;
                try {
                    loginPage = createPage(LoginPage.class, request, response);
                } catch (Exception e) {
                    writer.println("could not init login page");
                    return;
                }
                LoginPage.saveActionPage(page, actionName, session);
                outputPage(LoginPage.class, loginPage, writer, session);
                return;
            }
        }
        Object actionParam = null;
        if (-1 != actionName.indexOf("(")) {
            LOG.debug("table action requested");
            String tableFieldName = actionName.substring(actionName.indexOf("(") + 1, actionName.indexOf("."));
            LOG.debug("table field name: " + tableFieldName);
            Field tableField;
            try {
                tableField = pageClass.getDeclaredField(tableFieldName);
            } catch (Exception e) {
                writer.println("field not found: " + tableFieldName);
                return;
            }
            if (false == List.class.equals(tableField.getType())) {
                writer.println("field is not a list: " + tableFieldName);
                return;
            }
            Integer tableActionIdx = Integer.parseInt(actionName.substring(actionName.indexOf(".") + 1, actionName.indexOf(")")));
            tableField.setAccessible(true);
            List<?> table;
            try {
                table = (List<?>) tableField.get(page);
            } catch (Exception e) {
                writer.println("error reading list: " + tableFieldName);
                return;
            }
            actionParam = table.get(tableActionIdx);
        }
        performAction(page, actionName, actionParam, session, writer, request, response);
    }
