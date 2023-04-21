    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        response.setContentType("text/xml");
        try {
            PrintWriter pw = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            if (request.getMethod() != "POST") {
                writeErrorResponse(pw, "Request Method must be POST");
                return;
            }
            String blogEntryID = request.getParameter("BlogEntryID");
            if (blogEntryID != null) {
                try {
                    BlogEntry be = BlogEntry.findEntry(blogEntryID);
                    if (be.hasTrackBack(request.getRemoteHost())) {
                        writeErrorResponse(pw, "You have already posted a trackback on this entry from IPAddress " + request.getRemoteHost());
                        return;
                    }
                    String url = request.getParameter("url");
                    if (url == null) {
                        writeErrorResponse(pw, "Your post should include a url");
                    } else {
                        String title = request.getParameter("title");
                        String excerpt = request.getParameter("excerpt");
                        String blogName = request.getParameter("blog_name");
                        be.createTrackBack(url, title != null ? title : "No Title", excerpt != null ? excerpt : "", blogName != null ? blogName : "", request.getRemoteHost());
                        writeSuccess(pw);
                    }
                } catch (PersistentModelException e) {
                    writeErrorResponse(pw, "Unable to find the Blog Entry with ID " + blogEntryID);
                }
            } else {
                writeErrorResponse(pw, "The track back URL should include the parameter BlogEntryID");
            }
        } catch (IOException e) {
            throw new ServletException("Unable to get the Writer from the Response");
        }
    }
