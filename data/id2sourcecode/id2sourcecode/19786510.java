    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UtilsManager utilsManager = (UtilsManager) this.getApplicationContext().getBean("utilsManager");
        response.setContentType("text/html");
        if (request.isSecure()) {
            response.setHeader("Pragma", "private");
            response.setHeader("Cache-Control", "private, must-revalidate");
        } else {
            response.setHeader("Cache-Control", "no-cache");
        }
        PrintWriter out = response.getWriter();
        MessageSourceAccessor text = getMessageSourceAccessor();
        String mdl = (String) model.get("mdl");
        if (mdl.equals("10")) {
            Long number = (Long) model.get("instancesNotMonitored");
            out.println(text.getMessage("instancesNotMonitored", request.getLocale()) + " " + number);
        } else if (mdl.equals("11")) {
            response.setContentType("text/html");
            List data = (List) model.get("data");
            DateTime dt = new DateTime(utilsManager.getActualTimestamp().getTime());
            DateTime prevDet = null;
            Vector vData = new Vector();
            for (int i = 0; i < data.size(); i++) {
                OsmHistSysmark osmHistSysmark = (OsmHistSysmark) data.get(i);
                vData.add(osmHistSysmark.getId().getNumMark());
            }
            OsmSysmark osmSysmark = (OsmSysmark) model.get("activeData");
            if (osmSysmark != null) {
                vData.add(osmSysmark.getId().getNumMark());
                vData.add(osmSysmark.getId().getNumMark());
            }
            StringBuffer strTmp = new StringBuffer("[");
            for (int i = 0; i < vData.size(); i++) {
                Object o = vData.elementAt(i);
                if (i > 0) strTmp.append(",").append(o); else strTmp.append(o);
            }
            strTmp.append("]");
            response.setContentType("application/json");
            response.setHeader("Cache-Control", "no-cache");
            String test = strTmp.toString();
            GlobalNote miNote = new GlobalNote(test);
            JSONObject json = JSONObject.fromObject(miNote);
            out.print(json);
        } else if (mdl.equals("12")) {
            response.setContentType("text/plain");
            Integer days = (Integer) model.get("days");
            List data = (List) model.get("data");
            String filter = (String) model.get("filterSrv");
            if (data.size() == 0) {
                DateTime dateIni = new DateTime(utilsManager.getActualTimestamp().getTime()).minusDays(days);
                Date dateFin = new Date(utilsManager.getActualTimestamp().getTime());
                out.println(new StringBuilder().append(StringUtil.formatDateTime(dateIni)).append("-1").toString());
                out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(dateFin.getTime()))).append("-1").toString());
            } else {
                DateTime now = new DateTime(utilsManager.getActualTimestamp().getTime());
                for (int i = 0; i < data.size(); i++) {
                    if (i == 0) {
                        Date date = ((Date) ((Object[]) data.get(i))[1]);
                        if (filter.equals("1")) {
                            DateMidnight todayMidnight = new DateMidnight(utilsManager.getActualTimestamp().getTime());
                            if (date.getTime() < todayMidnight.getMillis()) {
                                out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(todayMidnight.getMillis()))).append(((Object[]) data.get(i))[2]).toString());
                            } else {
                                out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(date.getTime()))).append(((Object[]) data.get(i))[2]).toString());
                            }
                        } else if (filter.equals("7")) {
                            DateMidnight todayMidnight = new DateMidnight(utilsManager.getActualTimestamp().getTime());
                            DateMidnight firstDayWeek = todayMidnight.minusDays(now.dayOfWeek().get() - 1);
                            if (date.getTime() < firstDayWeek.getMillis()) {
                                out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(firstDayWeek.getMillis()))).append(((Object[]) data.get(i))[2]).toString());
                            } else {
                                out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(date.getTime()))).append(((Object[]) data.get(i))[2]).toString());
                            }
                        } else if (filter.equals("30")) {
                            DateMidnight todayMidnight = new DateMidnight(utilsManager.getActualTimestamp().getTime());
                            DateMidnight firstDayMonth = todayMidnight.minusDays(now.dayOfMonth().get() - 1);
                            if (date.getTime() < firstDayMonth.getMillis()) {
                                out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(firstDayMonth.getMillis()))).append(((Object[]) data.get(i))[2]).toString());
                            } else {
                                out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(date.getTime()))).append(((Object[]) data.get(i))[2]).toString());
                            }
                        }
                        continue;
                    }
                    if (i > 0) {
                        out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(((Date) ((Object[]) data.get(i))[1]).getTime()).minusMillis(1))).append(((Object[]) data.get(i - 1))[2]).toString());
                        out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(((Date) ((Object[]) data.get(i))[1]).getTime()))).append(((Object[]) data.get(i))[2]).toString());
                    }
                }
                if (data.size() > 0) {
                    Date date = ((Date) ((Object[]) data.get(data.size() - 1))[3]);
                    if (date.getTime() > now.getMillis()) {
                        out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(utilsManager.getActualTimestamp().getTime()))).append(((Object[]) data.get(data.size() - 1))[2]).toString());
                    }
                }
            }
        } else if (mdl.equals("15")) {
            response.setContentType("text/plain");
            List data = (List) model.get("data");
            if (data.size() == 0) {
                Date dateIni = (Date) model.get("dateIni");
                Date dateFin = (Date) model.get("dateFin");
                out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(dateIni.getTime()))).append("-1").toString());
                out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(dateFin.getTime()))).append("-1").toString());
            } else {
                DateTime dt = new DateTime(utilsManager.getActualTimestamp().getTime());
                DateTime prevDet = null;
                Date dateIni = (Date) model.get("dateIni");
                for (int i = 0; i < data.size(); i++) {
                    OsmHistSysmark osmHistSysmark = (OsmHistSysmark) data.get(i);
                    dt = new DateTime(osmHistSysmark.getId().getDtiInimark().getTime());
                    if (i > 0) {
                        OsmHistSysmark prevOsmHistSysmark = (OsmHistSysmark) data.get(i - 1);
                        prevDet = new DateTime(dt.minusMillis(1));
                        out.println(new StringBuilder().append(StringUtil.formatDateTime(prevDet)).append(prevOsmHistSysmark.getId().getNumMark()).toString());
                    }
                    if (i == 0) {
                        if (dt.getMillis() < dateIni.getTime()) {
                            out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(dateIni.getTime()))).append(osmHistSysmark.getId().getNumMark()).toString());
                        } else {
                            out.println(new StringBuilder().append(StringUtil.formatDateTime(dt)).append(osmHistSysmark.getId().getNumMark()).toString());
                        }
                    } else {
                        out.println(new StringBuilder().append(StringUtil.formatDateTime(dt)).append(osmHistSysmark.getId().getNumMark()).toString());
                    }
                }
                if (data != null && data.size() > 0) {
                    Date dateFin = (Date) model.get("dateFin");
                    OsmHistSysmark lastHistSysmark = (OsmHistSysmark) data.get(data.size() - 1);
                    OsmSysmark osmSysmark = (OsmSysmark) model.get("activeData");
                    String month = (String) model.get("month");
                    if (osmSysmark != null) {
                        out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(osmSysmark.getId().getDtiMark().getTime()).minusMillis(1))).append(lastHistSysmark.getId().getNumMark()).toString());
                        if (osmSysmark.getId().getDtiMark().getTime() > dateFin.getTime()) {
                            out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(dateFin.getTime()))).append(osmSysmark.getId().getNumMark()).toString());
                        } else {
                            out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(osmSysmark.getId().getDtiMark()))).append(osmSysmark.getId().getNumMark()).toString());
                        }
                    }
                }
            }
        } else if (mdl.equals("16")) {
            response.setContentType("text/plain");
            List data = (List) model.get("data");
            if (data.size() == 0) {
                Date dateIni = (Date) model.get("dateIni");
                Date dateFin = (Date) model.get("dateFin");
                out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(dateIni.getTime()))).append("-1").toString());
                out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(dateFin.getTime()))).append("-1").toString());
            } else {
                DateTime dt = new DateTime(utilsManager.getActualTimestamp().getTime());
                DateTime prevDet = null;
                Date dateIni = (Date) model.get("dateIni");
                for (int i = 0; i < data.size(); i++) {
                    OsmHistSlamarks osmHistSlamark = (OsmHistSlamarks) data.get(i);
                    dt = new DateTime(osmHistSlamark.getId().getDtiInimark().getTime());
                    if (i > 0) {
                        OsmHistSlamarks prevOsmHistSlamark = (OsmHistSlamarks) data.get(i - 1);
                        prevDet = new DateTime(dt.minusMillis(1));
                        out.println(new StringBuilder().append(StringUtil.formatDateTime(prevDet)).append(prevOsmHistSlamark.getId().getNumMark()).toString());
                    }
                    if (i == 0) {
                        if (dt.getMillis() < dateIni.getTime()) {
                            out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(dateIni.getTime()))).append(osmHistSlamark.getId().getNumMark()).toString());
                        } else {
                            out.println(new StringBuilder().append(StringUtil.formatDateTime(dt)).append(osmHistSlamark.getId().getNumMark()).toString());
                        }
                    } else {
                        out.println(new StringBuilder().append(StringUtil.formatDateTime(dt)).append(osmHistSlamark.getId().getNumMark()).toString());
                    }
                }
                if (data != null && data.size() > 0) {
                    Date dateFin = (Date) model.get("dateFin");
                    OsmHistSlamarks lastHistSlamark = (OsmHistSlamarks) data.get(data.size() - 1);
                    if (lastHistSlamark.getId().getDtiFinmark().getTime() > dateFin.getTime()) {
                        out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(dateFin.getTime()))).append(lastHistSlamark.getId().getNumMark()).toString());
                    } else {
                        out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(lastHistSlamark.getId().getDtiFinmark().getTime()))).append(lastHistSlamark.getId().getNumMark()).toString());
                    }
                }
            }
        } else if (mdl.equals("17")) {
            response.setContentType("text/plain");
            List data = (List) model.get("data");
            if (data.size() == 0) {
                Date dateIni = (Date) model.get("dateIni");
                Date dateFin = (Date) model.get("dateFin");
                out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(dateIni.getTime()))).append("-1").toString());
                out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(dateFin.getTime()))).append("-1").toString());
            } else {
                DateTime dt = new DateTime(utilsManager.getActualTimestamp().getTime());
                DateTime prevDet = null;
                Date dateIni = (Date) model.get("dateIni");
                long time, prevTime;
                Integer value = null, prevValue = null;
                for (int i = 0; i < data.size(); i++) {
                    time = ((DatesValue) data.get(i)).getDateIni().getTime();
                    value = ((DatesValue) data.get(i)).getValue();
                    dt = new DateTime(time);
                    if (i > 0) {
                        prevValue = ((DatesValue) data.get(i - 1)).getValue();
                        out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(time).minusMillis(1))).append(prevValue));
                    }
                    if (i == 0) {
                        if (dt.getMillis() < dateIni.getTime()) {
                            out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(dateIni.getTime()))).append(value).toString());
                        } else {
                            out.println(new StringBuilder().append(StringUtil.formatDateTime(dt)).append(value).toString());
                        }
                    } else {
                        out.println(new StringBuilder().append(StringUtil.formatDateTime(dt)).append(value).toString());
                    }
                }
                if (data != null && data.size() > 0) {
                    Date dateFin = (Date) model.get("dateFin");
                    DatesValue lastDatesValue = (DatesValue) data.get(data.size() - 1);
                    if (lastDatesValue.getDateIni().getTime() > dateFin.getTime()) {
                        out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(dateFin.getTime()))).append(lastDatesValue.getValue()).toString());
                    } else {
                        out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(lastDatesValue.getDateFin().getTime()))).append(lastDatesValue.getValue()).toString());
                    }
                }
            }
        } else if (mdl.equals("18")) {
            response.setContentType("text/plain");
            List data = (List) model.get("data");
            if (data.size() == 0) {
                Date dateIni = (Date) model.get("dateIni");
                Date dateFin = (Date) model.get("dateFin");
                out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(dateIni.getTime()))).append("-1").toString());
                out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(dateFin.getTime()))).append("-1").toString());
            } else {
                DateTime dt = new DateTime(utilsManager.getActualTimestamp().getTime());
                DateTime prevDet = null;
                Date dateIni = (Date) model.get("dateIni");
                for (int i = 0; i < data.size(); i++) {
                    EventValue value = (EventValue) data.get(i);
                    dt = new DateTime(value.dtiInievent.getTime());
                    if (i > 0) {
                        EventValue prevEventValue = (EventValue) data.get(i - 1);
                        prevDet = new DateTime(dt.minusMillis(1));
                        out.println(new StringBuilder().append(StringUtil.formatDateTime(prevDet)).append(prevEventValue.numValue).toString());
                    }
                    if (i == 0) {
                        if (dt.getMillis() < dateIni.getTime()) {
                            out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(dateIni.getTime()))).append(value.numValue).toString());
                        } else {
                            out.println(new StringBuilder().append(StringUtil.formatDateTime(dt)).append(value.numValue).toString());
                        }
                    } else {
                        out.println(new StringBuilder().append(StringUtil.formatDateTime(dt)).append(value.numValue).toString());
                    }
                }
                if (data != null && data.size() > 0) {
                    Date dateFin = (Date) model.get("dateFin");
                    Date now = new Date(utilsManager.getActualTimestamp().getTime());
                    EventValue lastEventValue = (EventValue) data.get(data.size() - 1);
                    if (dateFin.getTime() > now.getTime()) {
                        out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(now.getTime()))).append(lastEventValue.numValue).toString());
                    } else {
                        out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(dateFin.getTime()))).append(lastEventValue.numValue).toString());
                    }
                }
            }
        } else if (mdl.equals("19")) {
            response.setContentType("text/plain");
            Integer days = (Integer) model.get("days");
            List data = (List) model.get("data");
            if (data.size() == 0) {
                DateTime dateIni = new DateTime().minusDays(days);
                Date dateFin = new Date(utilsManager.getActualTimestamp().getTime());
                out.println(new StringBuilder().append(StringUtil.formatDateTime(dateIni)).append("-1").toString());
                out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(dateFin.getTime()))).append("-1").toString());
            } else {
                DateTime dt = new DateTime(utilsManager.getActualTimestamp().getTime());
                DateTime prevDet = null;
                Date dateIni = new Date(new DateTime().minusDays(days).getMillis());
                for (int i = 0; i < data.size(); i++) {
                    EventValue value = (EventValue) data.get(i);
                    dt = new DateTime(value.dtiInievent.getTime());
                    if (i > 0) {
                        EventValue prevEventValue = (EventValue) data.get(i - 1);
                        prevDet = new DateTime(dt.minusMillis(1));
                        out.println(new StringBuilder().append(StringUtil.formatDateTime(prevDet)).append(prevEventValue.numValue).toString());
                    }
                    if (i == 0) {
                        if (dt.getMillis() < dateIni.getTime()) {
                            out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(dateIni.getTime()))).append(value.numValue).toString());
                        } else {
                            out.println(new StringBuilder().append(StringUtil.formatDateTime(dt)).append(value.numValue).toString());
                        }
                    } else {
                        out.println(new StringBuilder().append(StringUtil.formatDateTime(dt)).append(value.numValue).toString());
                    }
                }
                if (data != null && data.size() > 0) {
                    Date now = new Date(utilsManager.getActualTimestamp().getTime());
                    EventValue lastEventValue = (EventValue) data.get(data.size() - 1);
                    out.println(new StringBuilder().append(StringUtil.formatDateTime(new DateTime(now.getTime()))).append(lastEventValue.numValue).toString());
                }
            }
        } else if (mdl.equals("20")) {
            String id = request.getParameter("id");
            List data = (List) model.get("data");
            out.println("<div id=\"pieChartWrapper" + id + "\" class=\"pieChartWrapper\" align=\"center\">");
            out.println("</div>");
            out.println("<table id='mydata" + id + "' class=\"pieChart\">");
            out.println("<tr style='height:15px'><th ></th><th></th><th></th></tr>");
            List alarms = (List) model.get("alarms");
            HashMap hashOsmTypalarms = new HashMap(alarms.size());
            for (int i = 0; i < alarms.size(); i++) {
                hashOsmTypalarms.put(((OsmTypcriticity) alarms.get(i)).getTypCriticity(), alarms.get(i));
            }
            for (int i = 0; i < data.size(); i++) {
                long prcValue = (Long) ((Object[]) data.get(i))[0];
                StringBuffer tmp = new StringBuffer();
                OsmTypcriticity typCri = (OsmTypcriticity) hashOsmTypalarms.get((Integer) ((Object[]) data.get(i))[2]);
                tmp.append("<tr><td >").append("<img src=\"").append(request.getContextPath()).append(typCri.getPthLogocriticity()).append("\"></td><td>").append(decodeCriticity(data, i)).append("</td><td style='width:40px' align='right'>").append(prcValue).append(" %</td></tr>");
                out.println(tmp.toString());
            }
            out.println("</table>");
            out.println("<map id=\"mymap" + id + "\" name=\"mymap" + id + "\" class=\"piechartmap\">");
            out.println("</map>");
            out.println("<script type=\"text/javascript\">");
            out.println("pieCharts('prcCriSrv', " + id + ");");
            out.print("</script>");
        } else if (mdl.equals("21")) {
            String id = request.getParameter("id");
            Integer[] data = (Integer[]) model.get("dataPrc");
            int total = 0;
            out.println("<div id=\"pieChartWrapper" + id + "\" class=\"pieChartWrapper\" align=\"center\">");
            out.println("</div>");
            out.println("<table id='mydata" + id + "' class=\"pieChart\">");
            out.println("<tr style='height:15px'><th ></th><th></th><th></th></tr>");
            for (int i = 0; i < data.length; i++) {
                total += data[i];
            }
            for (int i = 0; i < data.length; i++) {
                int value = data[i];
                long prcValue = Math.round(100 * value / total);
                StringBuffer tmp = new StringBuffer();
                tmp.append("<tr><td >").append("<img src=\"").append(request.getContextPath()).append(i == 0 ? "/images/logoalmalr.png" : "/images/logoalminfo.png").append("\"/></td><td>").append(i == 0 ? text.getMessage("graphs.NotAvailable") : text.getMessage("graphs.Available")).append("</td><td style='width:40px' align='right'>").append(prcValue).append(" %</td></tr>");
                out.println(tmp.toString());
            }
            out.println("</table>");
            out.println("<map id=\"mymap" + id + "\" name=\"mymap" + id + "\" class=\"piechartmap\">");
            out.println("</map>");
            out.println("<script type=\"text/javascript\">");
            out.println("pieCharts('prcAvaIns'," + id + ");");
            out.print("</script>");
        } else if (mdl.equals("22")) {
            String id = request.getParameter("id");
            List data = (List) model.get("data");
            double total = 0;
            out.println("<div id=\"pieChartWrapper" + id + "\" class=\"pieChartWrapper\" align=\"center\">");
            out.println("</div>");
            out.println("<table id='mydata" + id + "' class=\"pieChart\">");
            out.println("<tr style='height:15px'><th></th><th></th><th></th></tr>");
            for (int i = 0; i < data.size(); i++) {
                total += (Long) ((Object[]) data.get(i))[0];
            }
            List alarms = (List) model.get("alarms");
            HashMap hashOsmTypalarms = new HashMap(alarms.size());
            for (int i = 0; i < alarms.size(); i++) {
                hashOsmTypalarms.put(((OsmTypcriticity) alarms.get(i)).getTypCriticity(), alarms.get(i));
            }
            for (int i = 0; i < data.size(); i++) {
                Long value = (Long) ((Object[]) data.get(i))[0];
                long prcValue = Math.round(100 * value / total);
                StringBuffer tmp = new StringBuffer();
                OsmTypcriticity typCri = (OsmTypcriticity) hashOsmTypalarms.get((Integer) ((Object[]) data.get(i))[2]);
                tmp.append("<tr><td >").append("<img src=\"").append(request.getContextPath()).append(typCri.getPthLogocriticity()).append("\"/></td><td>").append(decodeCriticity(data, i)).append("</td><td style='width:40px' align='right'>").append(prcValue).append(" %</td></tr>");
                out.println(tmp.toString());
            }
            out.println("</table>");
            out.println("<map id=\"mymap" + id + "\" name=\"mymap" + id + "\" class=\"piechartmap\">");
            out.println("</map>");
            out.println("<script type=\"text/javascript\">");
            out.println("pieCharts('prcCriIns', " + id + ");");
            out.print("</script>");
        } else if (mdl.equals("23")) {
            out.println("<select name=\"users\" id=\"users\" class=\"form OsmMediumCbo\">");
            List users = (List) model.get("users");
            if (users.size() > 0) {
                out.println("<option value='ALL'>ALL</option>");
            }
            for (int i = 0; i < users.size(); i++) {
                OsmUser osmUser = (OsmUser) users.get(i);
                out.println(new StringBuilder().append("<option value=\'").append(osmUser.getIdnUser()).append("'>").append(osmUser.getIdnUser()).append("</option>").toString());
            }
            out.println("</select>");
            out.println("<script type=\"text/javascript\">");
            out.println("testUsers();");
            out.print("</script>");
        } else if (mdl.equals("24")) {
            TypInstancesInfo[] info = (TypInstancesInfo[]) model.get("info");
            out.println("<table border=\"1\">");
            out.print("<tr>");
            out.println("<th>");
            out.print("Type");
            out.println("</th>");
            out.println("<th>");
            out.print("#");
            out.println("</th>");
            out.println("<th>");
            out.print("OK");
            out.println("</th>");
            out.println("<th>");
            out.print("WR");
            out.println("</th>");
            out.println("<th>");
            out.print("CR");
            out.println("</th>");
            out.println("<th>");
            out.print("ER");
            out.println("</th>");
            out.println("<th>");
            out.print("UP");
            out.println("</th>");
            out.println("<th>");
            out.print("DOWN");
            out.println("</th>");
            out.println("</tr>");
            for (int i = 0; i < info.length; i++) {
                out.println("<tr>");
                TypInstancesInfo typInstancesInfo = info[i];
                out.print("<td>");
                out.print(typInstancesInfo.getTypInstance());
                out.print("</td>");
                out.print("<td>");
                out.print(typInstancesInfo.getTotal());
                out.print("</td>");
                out.print("<td>");
                out.print(typInstancesInfo.getOK());
                out.print("</td>");
                out.print("<td>");
                out.print(typInstancesInfo.getWR());
                out.print("</td>");
                out.print("<td>");
                out.print(typInstancesInfo.getCR());
                out.print("</td>");
                out.print("<td>");
                out.print(typInstancesInfo.getER());
                out.print("</td>");
                out.print("<td>");
                out.print(typInstancesInfo.getAva());
                out.print("</td>");
                out.print("<td>");
                out.print(typInstancesInfo.getNotAva());
                out.print("</td>");
                out.println("</tr>");
            }
            out.println("</table>");
        } else if (mdl.equals("28")) {
            String id = request.getParameter("id");
            Integer[] data = (Integer[]) model.get("dataPrc");
            int total = 0;
            out.println("<div id=\"pieChartWrapper" + id + "\" class=\"pieChartWrapper\" align=\"center\">");
            out.println("</div>");
            out.println("<table id='mydata" + id + "' class=\"pieChart\">");
            out.println("<tr style='height:15px'><th ></th><th></th><th></th></tr>");
            for (int i = 0; i < data.length; i++) {
                total += data[i];
            }
            for (int i = 0; i < data.length; i++) {
                int value = data[i];
                long prcValue = Math.round(100 * value / total);
                StringBuffer tmp = new StringBuffer();
                tmp.append("<tr><td >").append("<img src=\"").append(request.getContextPath()).append(i == 0 ? "/images/logoalmalr.png" : "/images/logoalminfo.png").append("\"/></td><td>").append(i == 0 ? text.getMessage("graphs.NotAvailable") : text.getMessage("graphs.Available")).append("</td><td style='width:40px' align='right'>").append(prcValue).append(" %</td></tr>");
                out.println(tmp.toString());
            }
            out.println("</table>");
            out.println("<map id=\"mymap" + id + "\" name=\"mymap" + id + "\" class=\"piechartmap\">");
            out.println("</map>");
            out.println("<script type=\"text/javascript\">");
            out.println("pieCharts('prcAvaSrv'," + id + ");");
            out.print("</script>");
        } else if (mdl.equals("100")) {
            StringBuilder json = new StringBuilder("[{data:[");
            List data = (List) model.get("data");
            for (int i = 0; i < data.size(); i++) {
                EventValue value = (EventValue) data.get(i);
                if (i > 1) {
                    json.append("[").append(((EventValue) data.get(i - 1)).dtiInievent.getTime()).append(",").append(value.numValue).append("],");
                }
                json.append("[").append(value.dtiInievent.getTime()).append(",").append(value.numValue).append("],");
            }
            List activeData = (List) model.get("activeData");
            for (int i = 0; i < activeData.size(); i++) {
                OsmActiveevent osmActiveevent = (OsmActiveevent) activeData.get(i);
                json.append("[").append(osmActiveevent.getDtiInievent().getTime()).append(",").append(osmActiveevent.getNumValue()).append("],");
            }
            json.deleteCharAt(json.length() - 1);
            json.append("]}]");
            out.print(json.toString());
        } else if (mdl.equals("50")) {
            Float[] data = (Float[]) model.get("dataPrc");
            float total = 0;
            for (int i = 0; i < data.length; i++) {
                total += data[i];
            }
            StringBuilder tmp = new StringBuilder();
            double noAva = 100 * data[0] / total;
            noAva = noAva > 0 ? Math.floor(noAva * 100) / 100.0 : Math.ceil(noAva * 100) / 100.0;
            double ava = 100 * data[1] / total;
            ava = ava > 0 ? Math.floor(ava * 100) / 100.0 : Math.ceil(ava * 100) / 100.0;
            if (ava == 100) {
                ava = 99.9;
                noAva = 0.1;
            }
            if (noAva == 100) {
                noAva = 99.9;
                ava = 0.1;
            }
            tmp.append("[{ label: \"\",  data:").append(noAva).append(", color:'#f00'},{ label: \"\",  data:").append(ava).append(", color:'#0f0'}]");
            out.print(tmp.toString());
        } else if (mdl.equals("51")) {
            List data = (List) model.get("data");
            List alarms = (List) model.get("alarms");
            HashMap hashOsmTypalarms = new HashMap(alarms.size());
            for (int i = 0; i < alarms.size(); i++) {
                hashOsmTypalarms.put(((OsmTypcriticity) alarms.get(i)).getTypCriticity(), alarms.get(i));
            }
            double[] values = new double[4];
            for (int i = 0; i < data.size(); i++) {
                OsmTypcriticity typCri = (OsmTypcriticity) hashOsmTypalarms.get((Integer) ((Object[]) data.get(i))[2]);
                double prcValue = (Double) ((Object[]) data.get(i))[0];
                prcValue = prcValue > 0 ? Math.floor(prcValue * 100) / 100.0 : Math.ceil(prcValue * 100) / 100.0;
                if (typCri.getTypCriticity() == 0) values[0] = prcValue; else if (typCri.getTypCriticity() == 1) values[1] = prcValue; else if (typCri.getTypCriticity() == 2) values[2] = prcValue; else if (typCri.getTypCriticity() == 99) values[3] = prcValue;
            }
            StringBuilder tmp = new StringBuilder();
            if (values[0] == 100) {
                values[0] = 99.9;
                values[1] = 0.1;
                values[2] = 0;
                values[3] = 0;
            }
            if (values[1] == 100) {
                values[1] = 99.9;
                values[0] = 0.1;
                values[2] = 0;
                values[3] = 0;
            }
            if (values[2] == 100) {
                values[2] = 99.9;
                values[0] = 0.1;
                values[1] = 0;
                values[3] = 0;
            }
            if (values[3] == 100) {
                values[3] = 99.9;
                values[0] = 0.1;
                values[1] = 0;
                values[2] = 0;
            }
            tmp.append("[{ label: \"\",  data:").append(values[0]).append(", color:'#0f0'},{ label: \"\",  data:").append(values[1]).append(", color:'#fd0'},{ label: \"\",  data:").append(values[2]).append(", color:'#f00'},{ label: \"\",  data:").append(values[3]).append(", color:'#00f'}]");
            out.print(tmp.toString());
        } else if (mdl.equals("52")) {
            String id = request.getParameter("id");
            Integer[] data = (Integer[]) model.get("dataPrc");
            int total = 0;
            for (int i = 0; i < data.length; i++) {
                total += data[i];
            }
            StringBuilder tmp = new StringBuilder();
            double noAva = 100 * data[0] / total;
            noAva = noAva > 0 ? Math.floor(noAva * 100) / 100.0 : Math.ceil(noAva * 100) / 100.0;
            double ava = 100 * data[1] / total;
            ava = ava > 0 ? Math.floor(ava * 100) / 100.0 : Math.ceil(ava * 100) / 100.0;
            if (ava == 100) {
                ava = 99.9;
                noAva = 0.1;
            }
            if (noAva == 100) {
                noAva = 99.9;
                ava = 0.1;
            }
            tmp.append("[{ label: \"\",  data:").append(noAva).append(", color:'#f00'},{ label: \"\",  data:").append(ava).append(", color:'#0f0'}]");
            out.print(tmp.toString());
        } else if (mdl.equals("53")) {
            List data = (List) model.get("data");
            List alarms = (List) model.get("alarms");
            HashMap hashOsmTypalarms = new HashMap(alarms.size());
            for (int i = 0; i < alarms.size(); i++) {
                hashOsmTypalarms.put(((OsmTypcriticity) alarms.get(i)).getTypCriticity(), alarms.get(i));
            }
            double[] values = new double[4];
            for (int i = 0; i < data.size(); i++) {
                OsmTypcriticity typCri = (OsmTypcriticity) hashOsmTypalarms.get((Integer) ((Object[]) data.get(i))[2]);
                double prcValue = (Double) ((Object[]) data.get(i))[0];
                prcValue = prcValue > 0 ? Math.floor(prcValue * 100) / 100.0 : Math.ceil(prcValue * 100) / 100.0;
                if (typCri.getTypCriticity() == 0) values[0] = prcValue; else if (typCri.getTypCriticity() == 1) values[1] = prcValue; else if (typCri.getTypCriticity() == 2) values[2] = prcValue; else if (typCri.getTypCriticity() == 99) values[3] = prcValue;
            }
            StringBuilder tmp = new StringBuilder();
            if (values[0] == 100) {
                values[0] = 99.9;
                values[1] = 0.1;
                values[2] = 0;
                values[3] = 0;
            }
            if (values[1] == 100) {
                values[1] = 99.9;
                values[0] = 0.1;
                values[2] = 0;
                values[3] = 0;
            }
            if (values[2] == 100) {
                values[2] = 99.9;
                values[0] = 0.1;
                values[1] = 0;
                values[3] = 0;
            }
            if (values[3] == 100) {
                values[3] = 99.9;
                values[0] = 0.1;
                values[1] = 0;
                values[2] = 0;
            }
            tmp.append("[{ label: \"\",  data:").append(values[0]).append(", color:'#0f0'},{ label: \"\",  data:").append(values[1]).append(", color:'#fd0'},{ label: \"\",  data:").append(values[2]).append(", color:'#f00'},{ label: \"\",  data:").append(values[3]).append(", color:'#00f'}]");
            out.print(tmp.toString());
        } else if (mdl.equals("54")) {
            List data = (List) model.get("data");
            int[] values = new int[4];
            int max = 0;
            int step = 0;
            for (int i = 0; i < data.size(); i++) {
                if (((Criticity) data.get(i)).getCriticty() == 0) {
                    values[0] = ((Criticity) data.get(i)).getValue();
                } else if (((Criticity) data.get(i)).getCriticty() == 1) {
                    values[1] = ((Criticity) data.get(i)).getValue();
                } else if (((Criticity) data.get(i)).getCriticty() == 2) {
                    values[2] = ((Criticity) data.get(i)).getValue();
                } else if (((Criticity) data.get(i)).getCriticty() == 99) {
                    values[3] = ((Criticity) data.get(i)).getValue();
                }
                if (((Criticity) data.get(i)).getValue() > max) {
                    max = ((Criticity) data.get(i)).getValue();
                }
            }
            if (max > 0) {
                step = max / 4;
            }
            int total = (step * 5) > max ? step * 5 : max;
            StringBuilder tmp = new StringBuilder();
            tmp.append("[{label:'', data:[[1,").append(values[0]).append("]], color:'#0f0'},").append(" {label:'', data:[[2,").append(values[1]).append("]], color:'#fc7'},").append(" {label:'', data:[[3,").append(values[2]).append("]], color:'#f00'},").append(" {label:'', data:[[4,").append(values[3]).append("]], color:'#888'}]");
            out.print(tmp.toString());
        } else if (mdl.equals("55")) {
            List alarms = (List) model.get("alarms");
            Integer[] states = (Integer[]) model.get("states");
            HashMap hashOsmTypalarms = new HashMap(alarms.size());
            int[] values = new int[4];
            int max = 0;
            int step = 1;
            for (int i = 0; i < alarms.size(); i++) {
                hashOsmTypalarms.put(((OsmTypcriticity) alarms.get(i)).getTypCriticity(), alarms.get(i));
            }
            OsmTypcriticity osmTypcriticity;
            for (int i = 0; i < states.length; i += 2) {
                osmTypcriticity = (OsmTypcriticity) hashOsmTypalarms.get(states[i]);
                if (osmTypcriticity.getTypCriticity() == 0) {
                    values[0] = states[i + 1];
                } else if (osmTypcriticity.getTypCriticity() == 1) {
                    values[1] = states[i + 1];
                } else if (osmTypcriticity.getTypCriticity() == 2) {
                    values[2] = states[i + 1];
                } else if (osmTypcriticity.getTypCriticity() == 99) {
                    values[3] = states[i + 1];
                }
                if (states[i + 1] > max) {
                    max = states[i + 1];
                }
            }
            if (max > 0) {
                step = max / 4;
            }
            int total = (step * 5) > max ? step * 5 : max;
            StringBuilder tmp = new StringBuilder();
            tmp.append("[{label:'', data:[[1,").append(values[0]).append("]], color:'#0f0'},").append(" {label:'', data:[[2,").append(values[1]).append("]], color:'#fc7'},").append(" {label:'', data:[[3,").append(values[2]).append("]], color:'#f00'},").append(" {label:'', data:[[4,").append(values[3]).append("]], color:'#888'}]");
            out.print(tmp.toString());
        } else if (mdl.equals("56")) {
            List states = (List) model.get("states");
            long[] values = new long[2];
            long max = 0;
            long step = 1;
            for (int i = 0; i < states.size(); i++) {
                long _value = (Long) ((Object[]) states.get(i))[0];
                if (((Integer) ((Object[]) states.get(i))[1]) == 0) {
                    values[0] = _value;
                } else {
                    values[1] = _value;
                }
                if (_value > max) {
                    max = _value;
                }
            }
            if (max > 0) {
                step = max / 2;
            }
            long total = (step * 3) > max ? step * 3 : max;
            StringBuilder tmp = new StringBuilder();
            tmp.append("[{label:'', data:[[1,").append(values[0]).append("]], color:'#f00'},").append(" {label:'', data:[[2,").append(values[1]).append("]], color:'#0f0'}]");
            out.print(tmp.toString());
        } else if (mdl.equals("57")) {
            List alarms = (List) model.get("alarms");
            Integer[] states = (Integer[]) model.get("states");
            HashMap hashOsmTypalarms = new HashMap(alarms.size());
            int[] values = new int[4];
            int max = 0;
            int step = 1;
            for (int i = 0; i < alarms.size(); i++) {
                hashOsmTypalarms.put(((OsmTypcriticity) alarms.get(i)).getTypCriticity(), alarms.get(i));
            }
            OsmTypcriticity osmTypcriticity;
            for (int i = 0; i < states.length; i += 2) {
                osmTypcriticity = (OsmTypcriticity) hashOsmTypalarms.get(states[i]);
                if (osmTypcriticity.getTypCriticity() == 0) {
                    values[0] = states[i + 1];
                } else if (osmTypcriticity.getTypCriticity() == 1) {
                    values[1] = states[i + 1];
                } else if (osmTypcriticity.getTypCriticity() == 2) {
                    values[2] = states[i + 1];
                } else if (osmTypcriticity.getTypCriticity() == 99) {
                    values[3] = states[i + 1];
                }
                if (states[i + 1] > max) {
                    max = states[i + 1];
                }
            }
            if (max > 0) {
                step = max / 4;
            }
            int total = (step * 5) > max ? step * 5 : max;
            StringBuilder tmp = new StringBuilder();
            tmp.append("[{label:'', data:[[1,").append(values[0]).append("]], color:'#0f0'},").append(" {label:'', data:[[2,").append(values[1]).append("]], color:'#fc7'},").append(" {label:'', data:[[3,").append(values[2]).append("]], color:'#f00'},").append(" {label:'', data:[[4,").append(values[3]).append("]], color:'#888'}]");
            out.print(tmp.toString());
        } else if (mdl.equals("58")) {
            List states = (List) model.get("states");
            long[] values = new long[2];
            long max = 0;
            long step = 1;
            for (int i = 0; i < states.size(); i++) {
                long _value = (Long) ((Object[]) states.get(i))[0];
                if (((Integer) ((Object[]) states.get(i))[1]) == 0) {
                    values[0] = _value;
                } else {
                    values[1] = _value;
                }
                if (_value > max) {
                    max = _value;
                }
            }
            if (max > 0) {
                step = max / 2;
            }
            long total = (step * 3) > max ? step * 3 : max;
            StringBuilder tmp = new StringBuilder();
            tmp.append("[{label:'', data:[[1,").append(values[0]).append("]], color:'#f00'},").append(" {label:'', data:[[2,").append(values[1]).append("]], color:'#0f0'}]");
            out.print(tmp.toString());
        } else if (mdl.equals("59")) {
            String mib = (String) model.get("mib");
            out.print("{\"mib\":\"" + (mib == null ? "" : mib) + "\"}");
        } else if (mdl.equals("60")) {
            List mibs = (List) model.get("mibs");
            if (mibs.size() > 0) {
                StringBuilder str = new StringBuilder("{\"mibs\":[");
                for (int i = 0; i < mibs.size(); i++) {
                    String data = (String) mibs.get(i);
                    if (i == 0) {
                        str.append("{\"mib\":\"").append(data).append("\"}");
                    } else {
                        str.append(",{\"mib\":\"").append(data).append("\"}");
                    }
                }
                str.append("]}");
                out.print(str.toString());
            } else {
                out.print("{\"mibs\":[]}");
            }
        } else if (mdl.equals("61")) {
            response.setContentType("application/json");
            response.setHeader("Cache-Control", "no-cache");
            Boolean ok = (Boolean) model.get("ok");
            String error = (String) model.get("error");
            TestGenericErrors test = new TestGenericErrors(ok, new String[] { error });
            JSONObject json = JSONObject.fromObject(test);
            out.print(json);
        } else if (mdl.equals("62")) {
            response.setContentType("application/json");
            response.setHeader("Cache-Control", "no-cache");
            Boolean exists = (Boolean) model.get("exists");
            TestGeneric test = new TestGeneric(exists);
            JSONObject json = JSONObject.fromObject(test);
            out.print(json);
        } else if (mdl.equals("63")) {
            response.setContentType("application/json");
            response.setHeader("Cache-Control", "no-cache");
            Boolean isHoliday = (Boolean) model.get("isHoliday");
            TestGeneric test = new TestGeneric(isHoliday);
            JSONObject json = JSONObject.fromObject(test);
            out.print(json);
        } else if (mdl.equals("64")) {
            response.setContentType("application/json");
            response.setHeader("Cache-Control", "no-cache");
            List typInstances = (List) model.get("typInstances");
            List[] instances = (List[]) model.get("instances");
            String id = (String) model.get("id");
            StringBuilder str = new StringBuilder();
            str.append("{\"id\":\"").append(id).append("_Osmius\",\"name\":\"Osmius\",\"children\":[");
            for (int i = 0; i < typInstances.size(); i++) {
                OsmTypinstance osmTypinstance = (OsmTypinstance) typInstances.get(i);
                if (i > 0) {
                    str.append(",");
                }
                str.append("{\"id\":\"").append(id).append("_typ_").append(osmTypinstance.getTypInstance()).append("\",");
                str.append("\"name\":\"").append(osmTypinstance.getTypInstance()).append("\",");
                str.append("\"data\":{");
                str.append("\"description\":\"").append(osmTypinstance.getDesTypinstance()).append("\",");
                str.append("\"typInst\":\"").append(osmTypinstance.getTypInstance()).append("\",");
                str.append("\"imgIndex\":\"").append(osmTypinstance.getTypInstance()).append("\",");
                str.append("\"img\":\"").append(osmTypinstance.getPthLogoinstance()).append("\"");
                str.append("}, \"children\":[");
                List tmpInst = instances[i];
                for (int j = 0; j < tmpInst.size(); j++) {
                    OsmInstance osmInstance = (OsmInstance) tmpInst.get(j);
                    if (j > 0) {
                        str.append(",");
                    }
                    str.append("{\"id\":\"").append(id).append("_").append(osmInstance.getIdnInstance()).append("\",");
                    str.append("\"name\":\"").append(osmInstance.getIdnInstance()).append("\",");
                    str.append("\"data\":{");
                    str.append("\"description\":\"").append(osmInstance.getDesInstance()).append("\",");
                    str.append("\"severity\":\"").append(osmInstance.getOsmTypcriticity().getTypCriticity()).append("\",");
                    str.append("\"availability\":\"").append(osmInstance.getIndAvailability()).append("\"");
                    str.append("},\"children\":[]}");
                }
                str.append("]}");
            }
            str.append("],\"data\":\"\"}");
            out.print(str.toString());
        } else if (mdl.equals("65") || mdl.equals("66") || mdl.equals("67")) {
            response.setContentType("application/json");
            response.setHeader("Cache-Control", "no-cache");
            String json = (String) model.get("json");
            if (json != null) out.print(json);
        } else if (mdl.equals("70") || mdl.equals("74") || mdl.equals("75")) {
            response.setContentType("application/json");
            response.setHeader("Cache-Control", "no-cache");
            String json = (String) model.get("JSONInstances");
            if (json != null) out.print(json);
        } else if (mdl.equals("71")) {
            response.setContentType("application/json");
            response.setHeader("Cache-Control", "no-cache");
            List acte = (List) model.get("activeevents");
            List trape = (List) model.get("trapactiveevents");
            StringBuilder json = new StringBuilder("{ \"events\":[");
            for (int i = 0; i < acte.size(); i++) {
                OsmTypevent obj = (OsmTypevent) acte.get(i);
                if (i > 0) {
                    json.append(",");
                }
                json.append("{\"typEvent\":\"").append(obj.getId().getTypEvent()).append("\",\"desEvent\":\"").append(obj.getDesTypevent()).append("\",\"defGraph\":\"").append(obj.getDefGraph()).append("\"}");
            }
            json.append("],\"traps\":[");
            for (int i = 0; i < trape.size(); i++) {
                String[] obj = (String[]) trape.get(i);
                if (i > 0) {
                    json.append(",");
                }
                json.append("{\"typEvent\":\"").append(obj[0]).append("\",\"desEvent\":\"").append(obj[1]).append("\"}");
            }
            json.append("]}");
            out.print(json.toString());
        } else if (mdl.equals("72")) {
            response.setContentType("application/json");
            response.setHeader("Cache-Control", "no-cache");
            List typInstances = (List) model.get("typInstances");
            StringBuilder json = new StringBuilder();
            json.append("[");
            for (int i = 0; i < typInstances.size(); i++) {
                OsmTypinstance osmTypinstance = (OsmTypinstance) typInstances.get(i);
                if (i > 0) {
                    json.append(",");
                }
                json.append("{\"typInstance\":\"").append(osmTypinstance.getTypInstance()).append("\",\"desTypinstance\":\"").append(osmTypinstance.getDesTypinstance()).append("\"}");
            }
            json.append("]");
            out.print(json.toString());
        } else if (mdl.equals("73")) {
            response.setContentType("application/json");
            response.setHeader("Cache-Control", "no-cache");
            List typEvents = (List) model.get("typEvents");
            List trapEvents = (List) model.get("trapEvents");
            StringBuilder json = new StringBuilder("{ \"events\":[");
            for (int i = 0; i < typEvents.size(); i++) {
                OsmTypevent obj = (OsmTypevent) typEvents.get(i);
                if (i > 0) {
                    json.append(",");
                }
                json.append("{\"typEvent\":\"").append(obj.getId().getTypEvent()).append("\",\"desEvent\":\"").append(obj.getDesTypevent()).append("\"}");
            }
            json.append("],\"traps\":[");
            for (int i = 0; i < trapEvents.size(); i++) {
                String[] obj = (String[]) trapEvents.get(i);
                if (i > 0) {
                    json.append(",");
                }
                json.append("{\"typEvent\":\"").append(obj[0]).append("\",\"desEvent\":\"").append(obj[1]).append("\"}");
            }
            json.append("]}");
            out.print(json.toString());
        }
    }
