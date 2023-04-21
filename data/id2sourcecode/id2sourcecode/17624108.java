    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) throw new IllegalStateException("Unauthorized user request!");
        log("SERVICE >> Session id: " + session.getId() + " Creation time: " + session.getCreationTime());
        ObjectInputStream in = new ObjectInputStream(request.getInputStream());
        String method = in.readUTF();
        Serializable[] arguments;
        try {
            arguments = (Serializable[]) in.readUnshared();
        } catch (ClassNotFoundException ex) {
            throw Exceptions.getNested(ServletException.class, ex);
        }
        log("SERVICE >> Method: " + method + " Arguments" + Arrays.deepToString(arguments));
        Object result;
        try {
            if (method.equals("initSocket")) result = RemoteSockets.init(session, (Class<?>[]) arguments[0], (Object[]) arguments[1]); else if (method.equals("initServerSocket")) result = RemoteServerSockets.init(session, (Class<?>[]) arguments[0], (Object[]) arguments[1]); else if (method.equals("accept") || method.equals("readFromInputStream") || method.equals("writeToOutputStream")) throw new IllegalArgumentException("Illegal invocation method: " + method); else {
                result = invoke(session, (Long) arguments[0], method, (Class<?>[]) arguments[1], (Object[]) arguments[2]);
            }
        } catch (Exception ex) {
            result = Exceptions.getNested(IOException.class, ex);
        }
        ByteArrayOutputStream content = new ByteArrayOutputStream();
        try {
            ObjectOutputStream out = new ObjectOutputStream(content);
            out.writeUnshared(result);
            out.flush();
        } catch (Exception ex) {
            content.reset();
            log("Serializaion error: " + ex.getMessage());
        }
        response.setContentType("application/octet-stream");
        response.setContentLength(content.size());
        content.writeTo(response.getOutputStream());
        response.getOutputStream().flush();
    }
