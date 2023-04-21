    private void writeSummary(JavaInformations javaInformations) throws IOException {
        final String lineEnd = "</td> </tr>";
        final String columnAndLineEnd = "</td><td>" + lineEnd;
        writeln("<table align='left' border='0' cellspacing='0' cellpadding='2' summary='#Informations_systemes#'>");
        writeln("<tr><td>#Host#: </td><td><b>" + javaInformations.getHost() + "</b>" + lineEnd);
        final MemoryInformations memoryInformations = javaInformations.getMemoryInformations();
        final long usedMemory = memoryInformations.getUsedMemory();
        final long maxMemory = memoryInformations.getMaxMemory();
        write("<tr><td>#memoire_utilisee#: </td><td>");
        writeGraph("usedMemory", integerFormat.format(usedMemory / 1024 / 1024));
        writeln(" #Mo# / " + integerFormat.format(maxMemory / 1024 / 1024) + " #Mo#&nbsp;&nbsp;&nbsp;</td><td>");
        writeln(toBar(memoryInformations.getUsedMemoryPercentage()));
        writeln(lineEnd);
        if (javaInformations.getSessionCount() >= 0) {
            write("<tr><td>#nb_sessions_http#: </td><td>");
            writeGraph("httpSessions", integerFormat.format(javaInformations.getSessionCount()));
            writeln(columnAndLineEnd);
        }
        write("<tr><td>#nb_threads_actifs#<br/>(#Requetes_http_en_cours#): </td><td>");
        writeGraph("activeThreads", integerFormat.format(javaInformations.getActiveThreadCount()));
        writeln(columnAndLineEnd);
        if (!noDatabase) {
            write("<tr><td>#nb_connexions_actives#: </td><td>");
            writeGraph("activeConnections", integerFormat.format(javaInformations.getActiveConnectionCount()));
            writeln(columnAndLineEnd);
            final int usedConnectionCount = javaInformations.getUsedConnectionCount();
            final int maxConnectionCount = javaInformations.getMaxConnectionCount();
            write("<tr><td>#nb_connexions_utilisees#<br/>(#ouvertes#): </td><td>");
            writeGraph("usedConnections", integerFormat.format(usedConnectionCount));
            if (maxConnectionCount > 0) {
                writeln(" / " + integerFormat.format(maxConnectionCount) + "&nbsp;&nbsp;&nbsp;</td><td>");
                writeln(toBar(javaInformations.getUsedConnectionPercentage()));
            }
            writeln(lineEnd);
        }
        if (javaInformations.getSystemLoadAverage() >= 0) {
            write("<tr><td>#Charge_systeme#</td><td>");
            writeGraph("systemLoad", decimalFormat.format(javaInformations.getSystemLoadAverage()));
            writeln(columnAndLineEnd);
        }
        writeln("</table>");
    }
