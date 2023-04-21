    void writeConnections() throws IOException {
        if (connectionsInformations.isEmpty()) {
            writeln("#Aucune_connexion_jdbc_ouverte#");
            return;
        }
        writeln("<table class='sortable' width='100%' border='1' cellspacing='0' cellpadding='2' summary='#Connexions_jdbc_ouvertes#'>");
        write("<thead><tr><th class='sorttable_date'>#Date_et_stack_trace_ouverture#</th>");
        if (JavaInformations.STACK_TRACES_ENABLED) {
            write("<th>#Thread_et_stack_trace_actuelle#</th>");
        } else {
            write("<th>#Thread#</th>");
        }
        writeln("</tr></thead><tbody>");
        boolean odd = false;
        for (final ConnectionInformations connection : connectionsInformations) {
            if (odd) {
                write("<tr class='odd' onmouseover=\"this.className='highlight'\" onmouseout=\"this.className='odd'\">");
            } else {
                write("<tr onmouseover=\"this.className='highlight'\" onmouseout=\"this.className=''\">");
            }
            odd = !odd;
            writeConnection(connection);
            writeln("</tr>");
        }
        writeln("</tbody></table>");
        final int nbConnections = connectionsInformations.size();
        writeln("<div align='right'>" + I18N.getFormattedString("nb_connexions_ouvertes", nbConnections) + "</div>");
    }
