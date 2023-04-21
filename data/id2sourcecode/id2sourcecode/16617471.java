    private void writeTomcatInformations(List<TomcatInformations> tomcatInformationsList) throws IOException {
        final List<TomcatInformations> list = new ArrayList<TomcatInformations>();
        for (final TomcatInformations tomcatInformations : tomcatInformationsList) {
            if (tomcatInformations.getRequestCount() > 0) {
                list.add(tomcatInformations);
            }
        }
        final boolean onlyOne = list.size() == 1;
        for (final TomcatInformations tomcatInformations : list) {
            writer.write("<tr><td valign='top'>Tomcat " + I18N.htmlEncode(tomcatInformations.getName(), false) + ": </td><td>");
            final int currentThreadsBusy = tomcatInformations.getCurrentThreadsBusy();
            writeln("#busyThreads# = ");
            if (onlyOne) {
                writeGraph("tomcatBusyThreads", integerFormat.format(currentThreadsBusy));
            } else {
                writeln(integerFormat.format(currentThreadsBusy));
            }
            writeln(" /  " + integerFormat.format(tomcatInformations.getMaxThreads()));
            writeln("&nbsp;&nbsp;&nbsp;");
            writeln(toBar(100d * currentThreadsBusy / tomcatInformations.getMaxThreads()));
            writeln("<br/>#bytesReceived# = ");
            if (onlyOne) {
                writeGraph("tomcatBytesReceived", integerFormat.format(tomcatInformations.getBytesReceived()));
            } else {
                writeln(integerFormat.format(tomcatInformations.getBytesReceived()));
            }
            writeln("<br/>#bytesSent# = ");
            if (onlyOne) {
                writeGraph("tomcatBytesSent", integerFormat.format(tomcatInformations.getBytesSent()));
            } else {
                writeln(integerFormat.format(tomcatInformations.getBytesSent()));
            }
            writeln("<br/>#requestCount# = ");
            writeln(integerFormat.format(tomcatInformations.getRequestCount()));
            writeln("<br/>#errorCount# = ");
            writeln(integerFormat.format(tomcatInformations.getErrorCount()));
            writeln("<br/>#processingTime# = ");
            writeln(integerFormat.format(tomcatInformations.getProcessingTime()));
            writeln("<br/>#maxProcessingTime# = ");
            writeln(integerFormat.format(tomcatInformations.getMaxTime()));
            writeln("</td> </tr>");
        }
    }
