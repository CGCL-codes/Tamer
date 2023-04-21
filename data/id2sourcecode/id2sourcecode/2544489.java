    private void writeContexts(List<CounterRequestContext> contexts) throws IOException {
        boolean displayRemoteUser = false;
        for (final CounterRequestContext context : contexts) {
            if (context.getRemoteUser() != null) {
                displayRemoteUser = true;
                break;
            }
        }
        writeln("<table class='sortable' width='100%' border='1' cellspacing='0' cellpadding='2' summary='#Requetes_en_cours#'>");
        write("<thead><tr><th>#Thread#</th>");
        if (displayRemoteUser) {
            write("<th>#Utilisateur#</th>");
        }
        write("<th>#Requete#</th>");
        write("<th class='sorttable_numeric'>#Duree_ecoulee#</th><th class='sorttable_numeric'>#Temps_moyen#</th>");
        write("<th class='sorttable_numeric'>#Temps_cpu#</th><th class='sorttable_numeric'>#Temps_cpu_moyen#</th>");
        if (childHitsDisplayed) {
            final String childCounterName = contexts.get(0).getParentCounter().getChildCounterName();
            write("<th class='sorttable_numeric'>" + I18N.getFormattedString("hits_fils", childCounterName));
            write("</th><th class='sorttable_numeric'>" + I18N.getFormattedString("hits_fils_moyens", childCounterName));
            write("</th><th class='sorttable_numeric'>" + I18N.getFormattedString("temps_fils", childCounterName));
            write("</th><th class='sorttable_numeric'>" + I18N.getFormattedString("temps_fils_moyen", childCounterName) + "</th>");
        }
        if (stackTraceEnabled) {
            write("<th>#Methode_executee#</th>");
        }
        writeln("</tr></thead><tbody>");
        boolean odd = false;
        for (final CounterRequestContext context : contexts) {
            if (odd) {
                write("<tr class='odd' onmouseover=\"this.className='highlight'\" onmouseout=\"this.className='odd'\">");
            } else {
                write("<tr onmouseover=\"this.className='highlight'\" onmouseout=\"this.className=''\">");
            }
            odd = !odd;
            writeContext(context, displayRemoteUser);
            writeln("</tr>");
        }
        writeln("</tbody></table>");
    }
