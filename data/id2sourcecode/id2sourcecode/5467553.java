    private void writeThreads() throws IOException {
        int i = 0;
        for (final JavaInformations javaInformations : javaInformationsList) {
            write("<b>");
            writer.write(I18N.getFormattedString("Threads_sur", javaInformations.getHost()));
            write(": </b>");
            writeln(I18N.getFormattedString("thread_count", javaInformations.getThreadCount(), javaInformations.getPeakThreadCount(), javaInformations.getTotalStartedThreadCount()));
            writeln("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            final List<ThreadInformations> threadInformationsList = javaInformations.getThreadInformationsList();
            final HtmlThreadInformationsReport htmlThreadInformationsReport = new HtmlThreadInformationsReport(threadInformationsList, javaInformations.isStackTraceEnabled(), writer);
            if (threadInformationsList.size() <= MAX_THREADS_DISPLAYED_IN_MAIN_REPORT) {
                final String id = "threads_" + i;
                writeShowHideLink(id, "#Details#");
                htmlThreadInformationsReport.writeDeadlocks();
                writeln("<br/><br/><div id='" + id + "' style='display: none;'>");
                htmlThreadInformationsReport.toHtml();
                writeln("</div><br/>");
            } else {
                writeln("<a href='?part=threads'>#Details#</a><br/>");
            }
            i++;
        }
    }
