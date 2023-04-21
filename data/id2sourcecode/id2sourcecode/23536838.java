    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Locale locale = getLocale(request);
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            log.error("Session is missing or has expired for client from " + request.getRemoteAddr());
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.session.nouser"));
            saveErrors(request, errors);
            return (mapping.findForward("logon"));
        }
        if (!user.isAdministrator()) {
            log.error("Security alert admin functions requested by " + user.getLogin() + " from " + request.getRemoteAddr());
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.session.notadmin"));
            saveErrors(request, errors);
            return (mapping.findForward("logon"));
        }
        String group = (String) PropertyUtils.getSimpleProperty(form, "group");
        if (group == null || "".equals(group)) {
            log.error("Database name missing in the Ftp dump request");
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.dump.nogroup"));
            saveErrors(request, errors);
            return (mapping.getInputForward());
        }
        String ftp = Settings.get("viewGroups." + group + ".ftp");
        if (ftp == null || "".equals(ftp)) {
            log.error("Database export hyperlink missing in the Ftp dump request");
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.dump.noftp"));
            saveErrors(request, errors);
            return (mapping.getInputForward());
        }
        String local = Settings.get("viewGroups." + group + ".local");
        if (local == null || "".equals(local)) {
            log.error("Database local directory missing in the Ftp dump request");
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.dump.nolocal"));
            saveErrors(request, errors);
            return (mapping.getInputForward());
        }
        Basket userBasket = (Basket) user.getBasket();
        DateInterval dateInterval = null;
        Vector keys = null;
        if (userBasket != null) {
            keys = userBasket.getKeys("date", "ALL");
            if (keys != null) {
                dateInterval = (DateInterval) userBasket.get(keys.get(0));
            }
        }
        if (dateInterval == null) {
            log.error("Date interval not found in data basket");
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.query.nodateinterval"));
            saveErrors(request, errors);
            return (mapping.findForward("notime"));
        }
        if (log.isDebugEnabled()) {
            log.debug("Ftp database " + group + " dump requested for date interval " + dateInterval + " by User '" + user.getLogin() + "' from " + request.getRemoteAddr() + " in session " + session.getId());
        }
        try {
            LocalApi api = new LocalApi();
            WDCDay dateFrom = dateInterval.getDateFrom();
            WDCDay dateTo = dateInterval.getDateTo();
            String[] tableList = UpdateMetadata.getTablesForGroup(group);
            log.debug("tableList.size: " + tableList.length);
            if (tableList.length > 0) {
                for (int tableind = 0; tableind < tableList.length; tableind++) {
                    String table = tableList[tableind];
                    String serviceUrl = Settings.get(table + ".dataServiceUrl");
                    String serviceUser = Settings.get(table + ".dataServiceUser");
                    String servicePassword = Settings.get(table + ".dataServicePassword");
                    String ftpFormat = Settings.get(table + ".ftpFormat");
                    Service service = new Service();
                    Call call = (Call) service.createCall();
                    if (serviceUser != null) {
                        call.setUsername(serviceUser);
                        if (servicePassword != null) {
                            call.setPassword(servicePassword);
                        }
                        System.err.println("Info: authentication user=" + serviceUser + " passwd=" + servicePassword + " at " + serviceUrl);
                    }
                    call.setTargetEndpointAddress(new URL(serviceUrl));
                    call.setTimeout(new Integer(60 * 1000 * 30));
                    call.setOperationName("getMetadata");
                    System.err.println("Info: calling getMetadata()");
                    String metadata = (String) call.invoke(new Object[] { table, null, null, "" + dateFrom.getDayId(), "" + dateTo.getDayId() });
                    Vector stations = null;
                    if (metadata == null) {
                        System.out.println("Error: result metadata is null");
                        continue;
                    } else {
                        System.out.println("Metadata is " + metadata);
                        SAXBuilder build = new SAXBuilder();
                        Document myDoc = build.build(new StringReader(metadata));
                        stations = getElementAttribute(myDoc.getRootElement(), "station", "name");
                        for (Iterator it = stations.iterator(); it.hasNext(); ) {
                            System.out.println("Station " + it.next());
                        }
                    }
                    WDCDay day = dateInterval.getDateFrom();
                    int numdays = WDCDay.daysBetween(dateTo, dateFrom);
                    for (int daycount = 0; daycount < numdays; daycount++) {
                        DateInterval oneday = new DateInterval(day, day);
                        int yearint = day.getYear();
                        int monthint = day.getMonth();
                        int dayint = day.getDay();
                        String directoryName = local + yearint + File.separator + monthint + File.separator + dayint;
                        boolean success = (new File(directoryName)).exists();
                        if (!success) {
                            success = (new File(directoryName)).mkdirs();
                        }
                        if (!success) {
                            System.out.println("Failed creating directory " + directoryName);
                        }
                        System.out.println("Directory " + directoryName + " ready for dump");
                        if (stations != null && stations.size() > 0) {
                            for (Iterator it = stations.iterator(); it.hasNext(); ) {
                                String stn = (String) it.next();
                                if (stn.endsWith("_hr")) {
                                    stn = stn.substring(0, stn.length() - 3);
                                } else if (stn.endsWith("_min")) {
                                    stn = stn.substring(0, stn.length() - 4);
                                }
                                String fileName = directoryName + File.separator + table + "_" + stn + "_" + day.getDayId() + ".zip";
                                call.setOperationName("getData");
                                String url = (String) call.invoke(new Object[] { table, stn, null, "" + day.getDayId(), "" + day.getDayId(), ftpFormat, null });
                                if (url == null) {
                                    System.err.println("Error: result URL is null");
                                } else {
                                    System.err.println("Info: result URL is " + url);
                                    URL dataurl = new URL(url);
                                    System.err.println("Info: local file name is " + fileName);
                                    FileOutputStream file = new FileOutputStream(fileName);
                                    if (file == null) {
                                        throw new Exception("Error: file output stream is null");
                                    }
                                    InputStream strm = dataurl.openStream();
                                    if (strm == null) {
                                        throw new Exception("Error: data input stream is null");
                                    } else {
                                        int c;
                                        while ((c = strm.read()) != -1) {
                                            file.write(c);
                                        }
                                    }
                                }
                            }
                        }
                        day.nextDay();
                    }
                }
            }
        } catch (Exception e) {
            log.error("Ftp dump error for group " + group + " when requested by " + user.getLogin() + " from " + request.getRemoteAddr() + " with exception " + e.toString());
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.database.dump"));
            saveErrors(request, errors);
        }
        session.setAttribute("hlink", ftp);
        return (mapping.findForward("success"));
    }
