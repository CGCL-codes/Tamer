    protected void makeDbReport() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Temporizador temporizador = new Temporizador(progressBar);
        StringBuilder sb = new StringBuilder("<html><head>");
        sb.append("<title>Análise Operacional do Banco de Dados</title>");
        sb.append("<style>").append(loadText("dbreport.css"));
        sb.append("</style></head><body>");
        sb.append("<h1>Análise Operacional do Banco de Dados</h1>");
        DateFormat f = useSQLite ? new SimpleDateFormat("yyyy-MM-dd") : null;
        try {
            sb.append("<div>RDBMS: <b>");
            sb.append(properties.getProperty("nameAndVersion"));
            sb.append("</b></div>");
            sb.append("<div><table cellpadding=2 cellspacing=0>");
            sb.append("<tr><td class=first>Nome Catálogo:</td><td>");
            sb.append(peek(properties, "dbCatalogName"));
            sb.append("</td></tr>");
            sb.append("<tr><td class=first>No.Tabelas:</td><td>");
            sb.append(dbtables.length).append("</td></tr></table></div>");
            Statement stm = connection.createStatement();
            Calendar calendar = Calendar.getInstance();
            int n = 1;
            temporizador.start();
            Properties r = loadProperties(INDICES_MENSAIS_PROPERTIES);
            Properties p = loadProperties(DBUPDATE_PROPERTIES);
            for (DBTable table : dbtables) {
                sb.append("<div>[").append(n).append("] <b>");
                sb.append(table.name);
                sb.append("</b><table class=info cellpadding=2 cellspacing=1 border=0>");
                String bcbcode = p.getProperty(table.name);
                if (bcbcode != null) {
                    sb.append("<tr><td nowrap class=first>Código no SGS do BCB:</td><td>");
                    sb.append(bcbcode).append("</td></tr>");
                    sb.append("<tr><td nowrap class=first>Descrição no SGS do BCB:</td><td valign=top>");
                    sb.append(r.getProperty(bcbcode)).append("</td></tr>");
                }
                sb.append("<tr><td nowrap class=first>No.Registros:</td><td>");
                sb.append(table.size).append("</td></tr>");
                if (table.size > 0) {
                    sb.append("<tr><td nowrap class=first>Período:</td><td>");
                    sb.append(table.getComment(false)).append("</td></tr>");
                    String q = buildSQL("allDates", table.name);
                    ResultSet result = stm.executeQuery(q);
                    if (result.next()) {
                        sb.append("<tr><td nowrap valign=top class=first>Conclusão:</td>");
                        StringBuilder errList = null;
                        int errCounter = 0;
                        long previous = (useSQLite ? f.parse(result.getString(1)) : result.getDate(1)).getTime();
                        while (result.next()) {
                            long current = (useSQLite ? f.parse(result.getString(1)) : result.getDate(1)).getTime();
                            calendar.setTimeInMillis(current);
                            calendar.add(Calendar.MONTH, -1);
                            if (calendar.getTimeInMillis() != previous) {
                                if (errCounter++ == 0) {
                                    errList = new StringBuilder();
                                    errList.append("Descontinuidade no(s) período(s):").append("<table cellpadding=5 cellspacing=5 border=0>");
                                }
                                errList.append("<tr><td nowrap class=error>&raquo;&nbsp;").append(String.format(DateUtils.localeBR, "%1$tB de %1$tY", previous)).append("</td></tr>");
                            }
                            previous = current;
                        }
                        if (errCounter > 0) {
                            sb.append("<td nowrap class=warn>").append(errList).append("</table>");
                        } else {
                            sb.append("<td nowrap>série temporal sem descontinuidade.");
                        }
                        sb.append("</td></tr>");
                    }
                    result.close();
                }
                sb.append("</table></div>");
                temporizador.update(((double) (n++)) / dbtables.length);
            }
            stm.close();
        } catch (ParseException e) {
            System.err.println(e);
        } catch (SQLException e) {
            System.err.println(e);
        }
        sb.append("<div class=footer>");
        sb.append(String.format(DateUtils.localeBR, "Gerado em %tc.", System.currentTimeMillis()));
        sb.append("</div>").append("</body></html>");
        clearReport();
        report.getEditorKit().createDefaultDocument();
        report.setContentType("text/html");
        report.setText(sb.toString());
        assureSplitPaneDisplay(true);
        report.setCaretPosition(0);
        report.requestFocusInWindow();
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        temporizador.terminate();
    }
