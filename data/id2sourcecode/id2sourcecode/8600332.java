    public final List<Program> getPrograms(final Channel channel, final Date date, final int day) {
        Date lastStartDate = null;
        Date lastEndDate = null;
        boolean startDateNextDay = false;
        boolean endDateNextDay = false;
        List<Program> programs = new ArrayList<Program>();
        String pageUrl = buildListUrl(channel, day);
        try {
            if (log.isDebugEnabled()) {
                log.debug("Fetch " + pageUrl);
            }
            String html = getTransport().getText(pageUrl);
            HtmlCleaner cleaner = new HtmlCleaner();
            CleanerProperties props = cleaner.getProperties();
            props.setOmitComments(true);
            props.setOmitUnknownTags(true);
            props.setAllowHtmlInsideAttributes(false);
            TagNode node = cleaner.clean(html);
            TagNode[] titleNodes = node.getElementsByAttValue("class", "programs", true, true);
            String dateText = "";
            if (titleNodes.length == 1) {
                dateText = titleNodes[0].getElementsByName("h2", true)[0].getText().toString();
                log.debug("Date " + dateText);
            }
            TagNode[] programNodes = node.getElementsByAttValue("class", "program", true, true);
            for (int i = 0; i < programNodes.length; i++) {
                TagNode progNode = programNodes[i];
                TagNode[] atag = progNode.getElementsByName("a", true);
                if (atag.length > 0) {
                    String url = atag[0].getAttributeByName("href");
                    log.debug(url);
                    TagNode[] spans = atag[0].getElementsByName("span", true);
                    if (spans.length > 0) {
                        String time = "";
                        String title = "";
                        String chan = "";
                        for (int j = 0; j < spans.length; j++) {
                            String cl = spans[j].getAttributeByName("class");
                            if ("time".equalsIgnoreCase(cl)) {
                                time = spans[j].getText().toString();
                            } else if ("title".equalsIgnoreCase(cl)) {
                                title = spans[j].getText().toString();
                            } else if ("channel".equalsIgnoreCase(cl)) {
                                chan = spans[j].getText().toString();
                            }
                        }
                        if (time.length() == 0) {
                            if (log.isWarnEnabled()) {
                                log.warn("Unusable program. No time defined.");
                            }
                            continue;
                        }
                        if (log.isDebugEnabled()) {
                            log.debug(time);
                            log.debug(title);
                            log.debug(chan);
                        }
                        if (title.endsWith("...")) {
                            if (log.isDebugEnabled()) {
                                log.debug("Found trimmed title: " + title);
                            }
                        }
                        Program p = new Program();
                        p.setUrl(getTvgidsurl() + url);
                        p.setFullyLoaded(false);
                        p.setTitle(title);
                        String startTime = "";
                        String endTime = "";
                        int minIndex = time.indexOf("-");
                        if (minIndex >= 0) {
                            startTime = time.substring(0, minIndex).trim();
                            endTime = time.substring(minIndex + 1).trim();
                        } else {
                            if (log.isWarnEnabled()) {
                                log.warn("Unusable program times. Empty.");
                            }
                            continue;
                        }
                        if (log.isDebugEnabled()) {
                            log.debug("StartTime " + dateText + " " + startTime);
                        }
                        try {
                            Date sd = convertDate(dateText, startTime, startDateNextDay);
                            if (lastStartDate == null) {
                                lastStartDate = sd;
                            } else {
                                if (((lastStartDate.getTime() - sd.getTime()) > TWALF_HOURS) && (!startDateNextDay)) {
                                    startDateNextDay = true;
                                    sd = convertDate(dateText, startTime, startDateNextDay);
                                }
                                lastStartDate = sd;
                            }
                            if (log.isDebugEnabled()) {
                                log.debug("START: " + sd);
                            }
                            p.setStartDate(sd);
                        } catch (ParseException e) {
                            if (log.isErrorEnabled()) {
                                log.error("Unable to parse date");
                            }
                        }
                        if (log.isDebugEnabled()) {
                            log.debug("EndTime " + dateText + " " + endTime);
                        }
                        try {
                            Date ed = convertDate(dateText, endTime, endDateNextDay);
                            if (lastEndDate == null) {
                                lastEndDate = ed;
                            } else {
                                if (((lastEndDate.getTime() - ed.getTime()) > TWALF_HOURS) && (!endDateNextDay)) {
                                    endDateNextDay = true;
                                    ed = convertDate(dateText, endTime, endDateNextDay);
                                }
                                lastEndDate = ed;
                            }
                            if (log.isDebugEnabled()) {
                                log.debug("END  : " + ed);
                            }
                            p.setEndDate(ed);
                        } catch (ParseException e) {
                            if (log.isErrorEnabled()) {
                                log.error("Unable to parse date ");
                            }
                        }
                        if (log.isDebugEnabled()) {
                            log.debug("Times: " + p.getStartDate() + " " + p.getEndDate());
                        }
                        p.setChannelId(channel.getChannelId());
                        if (p.getStartDate() == null) {
                            if (programs.size() > 0) {
                                if (log.isDebugEnabled()) {
                                    log.debug("Start time is unknown, getting one from previous program");
                                }
                                Date prevEnd = ((Program) programs.get(programs.size() - 1)).getEndDate();
                                if (prevEnd == null) {
                                    if (log.isWarnEnabled()) {
                                        log.warn("Unusable program. No start time.");
                                    }
                                    continue;
                                }
                                p.setStartDate(prevEnd);
                            } else {
                                if (log.isWarnEnabled()) {
                                    log.warn("Unusable program. No start time.");
                                }
                                continue;
                            }
                        } else {
                            if (programs.size() > 0) {
                                Program prevProgram = (Program) programs.get(programs.size() - 1);
                                Date prevEnd = prevProgram.getEndDate();
                                if (prevEnd == null) {
                                    if (log.isDebugEnabled()) {
                                        log.debug("Fixing previous program date");
                                    }
                                    prevProgram.setEndDate(p.getStartDate());
                                }
                            }
                        }
                        if ((p.getEndDate() == null) && (i == (programNodes.length - 1))) {
                            if (log.isWarnEnabled()) {
                                log.warn("Unusable program. No end time for last program");
                            }
                            continue;
                        }
                        if (log.isDebugEnabled()) {
                            log.debug("Add programm to the guide " + p.getLongTitle());
                        }
                        programs.add(p);
                    }
                }
            }
        } catch (TransportException e) {
            if (log.isErrorEnabled()) {
                log.error("Transport exception occured", e);
            }
        }
        fixProgramTimes(programs);
        return programs;
    }
