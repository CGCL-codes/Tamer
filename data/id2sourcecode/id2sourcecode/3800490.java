    private void createAdminReport() {
        RunningSystemBean.writeLog(logBean, "createAdminReport", LogBean.MESSAGE, "started");
        log.info("createAdminReport");
        sf.getCurrentSession().beginTransaction();
        GregorianCalendar date = new GregorianCalendar();
        date.setTime(new Date());
        date.add(Calendar.MONTH, -1);
        Date dateFrom = date.getTime();
        date.add(Calendar.DATE, 1);
        Collection<Event> events = EventManager.getInstance().getEvents(Boolean.TRUE, null, dateFrom, date.getTime(), null, null, null, null, null);
        String receiverEMailAddress;
        for (Event event : events) {
            Locale locale = Locale.GERMAN;
            int homepageVistis = event.getHomepageVisits();
            int regVisits = event.getRegistrationVisits();
            int runnerViews = event.getViewed();
            int myCalendarEntries = event.getMyCalendarEntries();
            double bewertung = event.getBewertungAvg();
            if (runnerViews < 5 && myCalendarEntries < 3) {
                continue;
            }
            receiverEMailAddress = event.getContactEmail();
            String text = RunningSystemBean.getLocalizedText(Locale.GERMAN, "dear.informal");
            if (RunningSystemBean.getStringValue(event.getContactPerson()).length() > 0) {
                text += " " + RunningSystemBean.getStringValue(event.getContactPerson());
            }
            text += ",<p>";
            text += RunningSystemBean.getLocalizedTextStatic(locale, "email_admin_eventstatistics_intro", new String[] { event.getName() }) + ":<br/><ul>";
            text += "<li>" + RunningSystemBean.getLocalizedTextStatic(locale, "email_admin_eventstatistics_viewed", new String[] { "" + runnerViews }) + "</li>";
            if (myCalendarEntries > 1) {
                text += "<li>" + RunningSystemBean.getLocalizedTextStatic(locale, "email_admin_eventstatistics_reminder", new String[] { "" + myCalendarEntries }) + "</li>";
            }
            if (event.getHomepage() != null && event.getHomepage().length() > 0 && homepageVistis > 1) {
                text += "<li>" + RunningSystemBean.getLocalizedTextStatic(locale, "email_admin_eventstatistics_homepage", new String[] { "" + homepageVistis }) + "</li>";
            }
            if (event.getOnlineRegistrationUrlFormatted() != null && event.getOnlineRegistrationUrlFormatted().length() > 0 && regVisits > 1) {
                text += "<li>" + RunningSystemBean.getLocalizedTextStatic(locale, "email_admin_eventstatistics_registration_link", new String[] { "" + regVisits }) + "</li>";
            }
            int bewertungsCount = BenchmarkManager.getInstance().getBewertungsCount(event);
            if (bewertung > 0.0 && bewertungsCount > 1) {
                text += "<li>" + RunningSystemBean.getLocalizedTextStatic(locale, "email_admin_eventstatistics_benchmark", new String[] { "" + bewertung, "" + bewertungsCount }) + "</li>";
            }
            text += "</ul><br/>";
            Set<EventRoute> eventRoutes = event.getEventRoutes();
            Iterator iter = eventRoutes.iterator();
            Collection<EventRoute> resultRoutes = new ArrayList<EventRoute>();
            while (iter.hasNext()) {
                EventRoute eventRoute = (EventRoute) iter.next();
                if (eventRoute.getResultAvailable()) {
                    resultRoutes.add(eventRoute);
                }
            }
            if (resultRoutes.size() > 0) {
                if (resultRoutes.size() == 1) text += RunningSystemBean.getLocalizedText(locale, "result_imported_route"); else text += RunningSystemBean.getLocalizedText(locale, "results_imported_routes");
                text += ":" + "<ul>";
                for (EventRoute eventRoute : resultRoutes) {
                    text += "<li>" + eventRoute.getRoute().getName() + "</li>";
                }
                text += "</ul><p>";
            } else {
                text += RunningSystemBean.getLocalizedText(locale, "email_admin_eventstatistics_noresults") + "<p>";
            }
            Set<Admin> admins = event.getAdmins();
            for (Admin admin : admins) {
                text += RunningSystemBean.getLocalizedText(locale, "logindata_yours") + ":<br/>" + RunningSystemBean.getLocalizedText(locale, "web_adress") + ": http://admin.myrunning.de<br/>" + RunningSystemBean.getLocalizedText(locale, "runnerid_username") + ": " + admin.getRunner().getFrontendRunnerID() + "<br/>" + RunningSystemBean.getLocalizedText(locale, "password") + ": " + admin.getPassword() + "<p>";
            }
            Mail mail = new Mail();
            if (!RunningSystemBean.checkEMail(receiverEMailAddress)) {
                receiverEMailAddress = RunningSystemBean.getNewsletterSenderMyRunning();
            } else {
                mail.setBccReceiver(RunningSystemBean.getNewsletterSenderMyRunning());
            }
            text += RunningSystemBean.getLocalizedTextStatic(locale, "email_admin_eventstatistics_advanced", new String[] { event.getName() }) + "<p>" + runningMasterBean.getRunningSystemBean().getSalutationContactFooter(locale) + "<p>" + runningMasterBean.getRunningSystemBean().getAutomaticMailFooter(locale, receiverEMailAddress);
            mail.setSender(RunningSystemBean.getSenderMyRunning());
            mail.setReceiver(receiverEMailAddress);
            String subject = RunningSystemBean.getLocalizedTextStatic(locale, "analysis_mr_for", new String[] { event.getName() });
            mail.setSubject(subject);
            mail.setMessage(text);
            mail.setEmailType(EmailManager.getInstance().getEmailTypeById(Constants.Values.EmailTypes.EVENT_ANALYSIS));
            mail.setSmtpServer(RunningSystemBean.getSMTPHostMyRunning());
            mail.setPopServer(RunningSystemBean.getPopHostMyRunning());
            mail.setSmtpUser(RunningSystemBean.getSMTPUserMyRunning());
            mail.setSmtpPassword(RunningSystemBean.getSMTPPasswordMyRunning());
            if (!EmailManager.getInstance().isMailSent(mail)) {
                counterAdminReport++;
                mailQueueBean.addMail(mail);
                RunningSystemBean.writeLog(logBean, new Date() + " - Try to send Admin-Report in ReminderTask to " + receiverEMailAddress, LogBean.MESSAGE, "");
            } else {
                RunningSystemBean.writeLog(logBean, new Date() + " - Admin-Report for  >" + event.getName() + "< in ReminderTask already sent to " + receiverEMailAddress, LogBean.ERROR, "");
            }
        }
        sf.getCurrentSession().getTransaction().commit();
        sf.getCurrentSession().close();
        RunningSystemBean.writeLog(logBean, "createAdminReport", LogBean.MESSAGE, "ended!");
    }
