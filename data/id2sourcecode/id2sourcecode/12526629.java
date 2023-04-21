    public static void doHeader(PrintWriter writer, HttpServletRequest request, HttpServletResponse response, String[] scripts) throws ServletException, IOException {
        writer.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        writer.println("<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en' lang='en'>");
        writer.println("<head>");
        writer.println("    <title>i-jetty Console</title>");
        writer.println("    <link rel='stylesheet' type='text/css' media='screen' href='/console/console.css' />");
        writer.println("    <meta name='viewport' content='width=device-width,minimum-scale=1.0,maximum-scale=1.0'/>");
        writer.println("    <META http-equiv='Pragma' content='no-cache'/>");
        writer.println("    <META http-equiv='Cache-Control' content='no-cache,no-store'/>");
        if (scripts != null) {
            for (String script : scripts) {
                writer.println("    <script src=\"" + script + "\"></script>");
            }
            writer.println("    <script>$(document).ready(function() { $('table').tablesorter(); });</script>");
        }
        writer.println("</head>");
        writer.println("<body>");
    }
