    private void writeThreadInformations(ThreadInformations threadInformations) throws IOException {
        write("<td>");
        writeThreadWithStackTrace(threadInformations);
        write("</td> <td align='center'>");
        if (threadInformations.isDaemon()) {
            write("#oui#");
        } else {
            write("#non#");
        }
        write("</td> <td align='right'>");
        write(integerFormat.format(threadInformations.getPriority()));
        write("</td> <td>");
        write("<img src='?resource=bullets/");
        write(getStateIcon(threadInformations));
        write("' alt='");
        write(String.valueOf(threadInformations.getState()));
        write("'/>");
        write(String.valueOf(threadInformations.getState()));
        if (stackTraceEnabled) {
            write("</td> <td>");
            writeExecutedMethod(threadInformations);
        }
        if (cpuTimeEnabled) {
            write("</td> <td align='right'>");
            write(integerFormat.format(threadInformations.getCpuTimeMillis()));
            write("</td> <td align='right'>");
            write(integerFormat.format(threadInformations.getUserTimeMillis()));
        }
        if (systemActionsEnabled) {
            write("</td> <td align='center' class='noPrint'>");
            write("<a href='?action=kill_thread&amp;threadId=");
            write(threadInformations.getGlobalThreadId());
            final String confirmKillThread = I18N.javascriptEncode(I18N.getFormattedString("confirm_kill_thread", threadInformations.getName()));
            writer.write("' onclick=\"javascript:return confirm('" + confirmKillThread + "');\">");
            final String title = htmlEncode(I18N.getFormattedString("kill_thread", threadInformations.getName()));
            writer.write("<img width='16' height='16' src='?resource=stop.png' alt='" + title + "' title='" + title + "' />");
            write("</a>");
        }
        write("</td>");
    }
