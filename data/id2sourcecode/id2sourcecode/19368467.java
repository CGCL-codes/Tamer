    private void initReaderAndWriter() throws XMPPException {
        try {
            if (!usingCompression) {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            } else {
                try {
                    Class<?> zoClass = Class.forName("com.jcraft.jzlib.ZOutputStream");
                    Constructor<?> constructor = zoClass.getConstructor(OutputStream.class, Integer.TYPE);
                    Object out = constructor.newInstance(socket.getOutputStream(), 9);
                    Method method = zoClass.getMethod("setFlushMode", Integer.TYPE);
                    method.invoke(out, 2);
                    writer = new BufferedWriter(new OutputStreamWriter((OutputStream) out, "UTF-8"));
                    Class<?> ziClass = Class.forName("com.jcraft.jzlib.ZInputStream");
                    constructor = ziClass.getConstructor(InputStream.class);
                    Object in = constructor.newInstance(socket.getInputStream());
                    method = ziClass.getMethod("setFlushMode", Integer.TYPE);
                    method.invoke(in, 2);
                    reader = new BufferedReader(new InputStreamReader((InputStream) in, "UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
                }
            }
        } catch (IOException ioe) {
            throw new XMPPException("XMPPError establishing connection with server.", new XMPPError(XMPPError.Condition.remote_server_error, "XMPPError establishing connection with server."), ioe);
        }
        if (configuration.isDebuggerEnabled()) {
            if (debugger == null) {
                String className = null;
                try {
                    className = System.getProperty("smack.debuggerClass");
                } catch (Throwable t) {
                }
                Class<?> debuggerClass = null;
                if (className != null) {
                    try {
                        debuggerClass = Class.forName(className);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (debuggerClass == null) {
                    try {
                        debuggerClass = Class.forName("org.jivesoftware.smackx.debugger.EnhancedDebugger");
                    } catch (Exception ex) {
                        try {
                            debuggerClass = Class.forName("org.jivesoftware.smack.debugger.LiteDebugger");
                        } catch (Exception ex2) {
                            ex2.printStackTrace();
                        }
                    }
                }
                try {
                    Constructor<?> constructor = debuggerClass.getConstructor(XMPPConnection.class, Writer.class, Reader.class);
                    debugger = (SmackDebugger) constructor.newInstance(this, writer, reader);
                    reader = debugger.getReader();
                    writer = debugger.getWriter();
                } catch (Exception e) {
                    e.printStackTrace();
                    DEBUG_ENABLED = false;
                }
            } else {
                reader = debugger.newConnectionReader(reader);
                writer = debugger.newConnectionWriter(writer);
            }
        }
    }
