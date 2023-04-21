    void toHtml(String message, String anchorNameForRedirect) throws IOException {
        if (collectorServer != null) {
            writeApplicationsLinks();
        }
        writeln("<h3><a name='top'></a><img width='24' height='24' src='?resource=systemmonitor.png' alt='#Stats#'/>");
        writeSummary();
        writeln("</h3>");
        writeln("<div align='center'>");
        writeRefreshAndPeriodLinks(null, null);
        writeGraphs();
        writeln(END_DIV);
        final Map<String, HtmlCounterReport> counterReportsByCounterName = writeCounters();
        if (range.getPeriod() == Period.TOUT && counterReportsByCounterName.size() > 1) {
            writeln("<div align='right'>");
            writeln("<a href='?action=clear_counter&amp;counter=all' title='#Vider_toutes_stats#'");
            writeln("class='noPrint' onclick=\"javascript:return confirm('" + I18N.javascriptEncode(I18N.getString("confirm_vider_toutes_stats")) + "');\">#Reinitialiser_toutes_stats#</a>");
            writeln(END_DIV);
        }
        if (collectorServer == null) {
            write("<h3><a name='currentRequests'></a>");
            writeln("<img width='24' height='24' src='?resource=hourglass.png' alt='#Requetes_en_cours#'/>#Requetes_en_cours#</h3>");
            writeCurrentRequests(javaInformationsList.get(0), counterReportsByCounterName);
        }
        writeln("<h3><a name='systeminfo'></a><img width='24' height='24' src='?resource=systeminfo.png' alt='#Informations_systemes#'/>");
        writeln("#Informations_systemes#</h3>");
        if (collectorServer != null) {
            writeln("<div align='center' class='noPrint'><a href='?part=currentRequests'>");
            writeln("<img src='?resource=hourglass.png' width='20' height='20' alt=\"#Voir_requetes_en_cours#\" /> #Voir_requetes_en_cours#</a>");
            writeln(END_DIV);
            writeln("<br/>");
        }
        if (Parameters.isSystemActionsEnabled()) {
            writeSystemActionsLinks();
        }
        new HtmlJavaInformationsReport(javaInformationsList, writer).toHtml();
        write("<h3 style='clear:both;'><a name='threads'></a>");
        writeln("<img width='24' height='24' src='?resource=threads.png' alt='#Threads#'/>");
        writeln("#Threads#</h3>");
        writeThreads();
        if (isJobEnabled()) {
            writeln("<h3><a name='jobs'></a><img width='24' height='24' src='?resource=jobs.png' alt='#Jobs#'/>");
            writeln("#Jobs#</h3>");
            final Counter rangeJobCounter = collector.getRangeCounter(range, Counter.JOB_COUNTER_NAME);
            writeJobs(rangeJobCounter);
            writeCounter(rangeJobCounter);
        }
        if (isCacheEnabled()) {
            writeln("<h3><a name='caches'></a><img width='24' height='24' src='?resource=caches.png' alt='#Caches#'/>");
            writeln("#Caches#</h3>");
            writeCaches();
        }
        writeMessageIfNotNull(message, null, anchorNameForRedirect);
        writePoweredBy();
        writeDurationAndOverhead();
    }
