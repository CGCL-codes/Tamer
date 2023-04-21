    void writeAllThreadsAsPart() throws IOException {
        writeln("<div class='noPrint'>");
        writeln("<a href='javascript:history.back()'><img src='?resource=action_back.png' alt='#Retour#'/> #Retour#</a>");
        writeln("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ");
        writeln("<a href='?part=threads'><img src='?resource=action_refresh.png' alt='#Actualiser#'/> #Actualiser#</a>");
        writeln("</div> <br/>");
        writeln("<img src='?resource=threads.png' width='24' height='24' alt='#Threads#' />&nbsp;");
        writeln("<b>#Threads#</b>");
        writeln("<br/><br/>");
        for (final JavaInformations javaInformations : javaInformationsList) {
            write(" <b>");
            writer.write(I18N.getFormattedString("Threads_sur", javaInformations.getHost()));
            write(": </b>");
            writeln(I18N.getFormattedString("thread_count", javaInformations.getThreadCount(), javaInformations.getPeakThreadCount(), javaInformations.getTotalStartedThreadCount()));
            final HtmlThreadInformationsReport htmlThreadInformationsReport = new HtmlThreadInformationsReport(javaInformations.getThreadInformationsList(), javaInformations.isStackTraceEnabled(), writer);
            htmlThreadInformationsReport.writeDeadlocks();
            writeln("<br/><br/>");
            htmlThreadInformationsReport.toHtml();
        }
    }
