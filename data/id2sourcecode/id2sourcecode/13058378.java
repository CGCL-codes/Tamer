    protected void doConfiguredRequest(Melati melati) throws ServletException, IOException {
        melati.getResponse().setContentType("text/html");
        MelatiWriter output = melati.getWriter();
        Date now = new Date();
        output.write("<html>\n" + "<head>\n" + "<title>Transaction Analysis</title>\n");
        String repeat = melati.getRequest().getParameter("repeat");
        if (repeat != null) output.write("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"" + repeat + "; URL=" + melati.getRequest().getRequestURI() + "?repeat=" + repeat + "\">");
        output.write("</head>\n" + "<body>\n" + "<h1>Transactions Analysis</h1>" + "<p>Run at " + now + "</p>\n" + "<p>JVM Free memory: " + NumberFormat.getInstance().format(Runtime.getRuntime().freeMemory()) + "</p>\n" + "<p>JVM Total memory: " + NumberFormat.getInstance().format(Runtime.getRuntime().totalMemory()) + "</p>\n" + "<form>Reload every <input name=repeat size=5 value=" + repeat + "> seconds <input type=submit></form>\n" + "<h2>Poem sessions in use</h2>\n");
        Enumeration e = PoemThread.openSessions().elements();
        while (e.hasMoreElements()) {
            SessionToken token = (SessionToken) e.nextElement();
            output.write("<table border=1 cellspacing=0 cellpadding=1>\n" + " <tr><th colspan=2>Session: " + token + "</td></tr>\n" + " <tr><th>Running for</th><td>" + (now.getTime() - token.started) + " ms</td></tr>\n" + " <tr><th>Thread</th><td>" + token.thread + "</td></tr>\n" + " <tr><th>PoemTransaction</th><td>" + token.transaction + "<br>(Database:" + token.transaction.getDatabase() + ")</td></tr>\n" + " <tr><th>PoemTask</th><td>" + token.task + "</td></tr>\n");
            Enumeration o = token.toTidy().elements();
            if (o.hasMoreElements()) {
                output.write("<tr><th>Open: </th><td>");
                while (o.hasMoreElements()) {
                    output.write(o.nextElement() + "<br>");
                }
                output.write("</td></tr>\n");
            }
            output.write("</table>\n");
        }
        output.write("<h2>Initialised Databases</h2>\n" + "<table border=1 cellspacing=0 cellpadding=1>" + "<tr><th>Database</th><th>PoemTransaction</th>" + "<th>Free</th><th>Blocked</th></tr>\n");
        Enumeration dbs = org.melati.LogicalDatabase.initialisedDatabases().elements();
        while (dbs.hasMoreElements()) {
            Database db = (Database) dbs.nextElement();
            for (int i = 0; i < db.transactionsMax(); i++) {
                boolean isFree = db.isFree(db.poemTransaction(i));
                Transaction blockedOn = db.poemTransaction(i).getBlockedOn();
                boolean blocked = false;
                if (blockedOn != null) blocked = true;
                output.write("<tr><td>" + db + "</td>\n" + "<td>" + db.poemTransaction(i) + "</td>\n" + "<td bgcolor=" + (isFree ? "green" : "red") + ">" + isFree + "</td>\n" + "<td bgcolor=" + (blocked ? "red" : "green") + ">" + (blocked ? blockedOn.toString() : "&nbsp;") + "</td>\n" + "</tr>\n");
            }
        }
        output.write("</table>\n" + "</body>\n" + "</html>\n");
    }
