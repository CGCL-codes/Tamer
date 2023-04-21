    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding(Config.getMng().getEncoding());
        response.setCharacterEncoding(Config.getMng().getEncoding());
        String repository = request.getServletPath();
        if (repository.startsWith("/")) {
            repository = repository.substring(1);
        }
        Repository rep = repos.get(repository);
        String resource = request.getPathInfo();
        request.setAttribute("resource-request", resource);
        if (resource == null) {
            response.sendRedirect(repository + "/");
            return;
        }
        resource = URLDecoder.decode(resource, Config.getMng().getEncoding());
        if (resource == null || resource.equals("/") || resource.equals(".") || resource.length() == 0) {
            resource = "index.html";
        }
        resource = resource.replaceAll("-", " ");
        if (rep.template != null && (resource.endsWith(".html") || resource.endsWith(".htm"))) {
            String title = resource;
            if (title.startsWith("/")) {
                title = title.substring(1);
            }
            if (title.length() > 1) {
                title = title.substring(0, 1).toUpperCase() + title.substring(1);
            }
            if (title.endsWith(".html")) {
                title = title.substring(0, title.length() - 5);
            } else if (title.endsWith(".htm")) {
                title = title.substring(0, title.length() - 4);
            }
            request.setAttribute("repositorio", rep);
            request.setAttribute("resource", resource);
            request.setAttribute("title", title);
            request.getRequestDispatcher(rep.template).forward(request, response);
        } else {
            URL url = rep.getResource(request, resource);
            if (url != null) {
                IOTools.copy(url.openStream(), response.getOutputStream());
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, resource);
            }
        }
    }
