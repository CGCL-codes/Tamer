    private void doHead(se.snigel.net.servlet.ServletRequest request, se.snigel.net.servlet.ServletResponse response) throws IOException {
        response.getWriter().write("<html><head><title>Test servlet</title></head><body>");
        response.getWriter().write("client header:<br><pre>");
        response.getWriter().write(request.getThread().getClientHeader().toString());
        response.getWriter().write("</pre><br>");
    }
