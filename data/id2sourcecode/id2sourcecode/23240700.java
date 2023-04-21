    void toHtml() throws IOException {
        writeln("<table class='sortable' width='100%' border='1' cellspacing='0' cellpadding='2' summary='#Threads#'>");
        write("<thead><tr><th>#Thread#</th>");
        write("<th>#Demon#</th><th class='sorttable_numeric'>#Priorite#</th><th>#Etat#</th>");
        if (stackTraceEnabled) {
            write("<th>#Methode_executee#</th>");
        }
        if (cpuTimeEnabled) {
            write("<th class='sorttable_numeric'>#Temps_cpu#</th><th class='sorttable_numeric'>#Temps_user#</th>");
        }
        if (systemActionsEnabled) {
            writeln("<th class='noPrint'>#Tuer#</th>");
        }
        writeln("</tr></thead><tbody>");
        boolean odd = false;
        for (final ThreadInformations threadInformations : threadInformationsList) {
            if (odd) {
                write("<tr class='odd' onmouseover=\"this.className='highlight'\" onmouseout=\"this.className='odd'\">");
            } else {
                write("<tr onmouseover=\"this.className='highlight'\" onmouseout=\"this.className=''\">");
            }
            odd = !odd;
            writeThreadInformations(threadInformations);
            writeln("</tr>");
        }
        writeln("</tbody></table>");
        writeln("<div align='right'>");
        writeln("#Temps_threads#");
        if (stackTraceEnabled) {
            writeln("<br/><a href='?part=threadsDump'><img src='?resource=text.png' alt='#Dump_threads_en_texte#'/>&nbsp;#Dump_threads_en_texte#</a>");
        }
        writeln("</div>");
    }
