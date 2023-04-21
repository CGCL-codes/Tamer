    public void service(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        PrintWriter out;
        try {
            out = response.getWriter();
            Listener dummy = new Listener(request.getServerPort(), request.getRequestURI());
            ServletContainer container = ServletContainer.getServletContainer();
            Hashtable registeredListeners = container.getRegisteredListeners();
            Listener listener = (Listener) registeredListeners.get(dummy);
            if (listener != null) {
                Queue requestsQueue = container.getRequestsQueue();
                registeredListeners.remove(dummy);
                HttpMessage message = new HttpMessage(request);
                if (listener.getDebug() > 0) listener.println("Received message:\n" + message);
                listener.setHttpMessageRequest(message);
                listener.setHttpServletResponse(response);
                PipedWriter writer = new PipedWriter();
                PipedReader reader = new PipedReader(writer);
                listener.setResponseWriter(writer);
                requestsQueue.put(dummy);
                char[] buffer = new char[1024];
                int len;
                while ((len = reader.read(buffer, 0, 1024)) != -1) {
                    out.write(buffer, 0, len);
                }
                reader.close();
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not found " + registeredListeners + ", dummy " + dummy);
            }
        } catch (Exception ex) {
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("text/plain");
                ex.printStackTrace();
            } catch (Exception e) {
            }
        }
    }
