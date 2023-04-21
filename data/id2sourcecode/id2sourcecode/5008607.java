    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        Class[] params = method.getParameterTypes();
        if (methodName.equals("equals") && params.length == 1 && params[0].equals(Object.class)) {
            Object value = args[0];
            if (value == null || !Proxy.isProxyClass(value.getClass())) return new Boolean(false);
            BurlapProxy handler = (BurlapProxy) Proxy.getInvocationHandler(value);
            return new Boolean(_url.equals(handler.getURL()));
        } else if (methodName.equals("hashCode") && params.length == 0) return new Integer(_url.hashCode()); else if (methodName.equals("getBurlapType")) return proxy.getClass().getInterfaces()[0].getName(); else if (methodName.equals("getBurlapURL")) return _url.toString(); else if (methodName.equals("toString") && params.length == 0) return getClass().getSimpleName() + "[" + _url + "]";
        InputStream is = null;
        URLConnection conn = null;
        HttpURLConnection httpConn = null;
        try {
            conn = _factory.openConnection(_url);
            httpConn = (HttpURLConnection) conn;
            httpConn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/xml");
            OutputStream os;
            try {
                os = conn.getOutputStream();
            } catch (Exception e) {
                throw new BurlapRuntimeException(e);
            }
            BurlapOutput out = _factory.getBurlapOutput(os);
            if (!_factory.isOverloadEnabled()) {
            } else if (args != null) methodName = methodName + "__" + args.length; else methodName = methodName + "__0";
            if (log.isLoggable(Level.FINE)) log.fine(this + " calling " + methodName + " (" + method + ")");
            out.call(methodName, args);
            try {
                os.flush();
            } catch (Exception e) {
                throw new BurlapRuntimeException(e);
            }
            if (conn instanceof HttpURLConnection) {
                httpConn = (HttpURLConnection) conn;
                int code = 500;
                try {
                    code = httpConn.getResponseCode();
                } catch (Exception e) {
                }
                if (code != 200) {
                    StringBuffer sb = new StringBuffer();
                    int ch;
                    try {
                        is = httpConn.getInputStream();
                        if (is != null) {
                            while ((ch = is.read()) >= 0) sb.append((char) ch);
                            is.close();
                        }
                        is = httpConn.getErrorStream();
                        if (is != null) {
                            while ((ch = is.read()) >= 0) sb.append((char) ch);
                        }
                    } catch (FileNotFoundException e) {
                        throw new BurlapRuntimeException(code + ": " + String.valueOf(e));
                    } catch (IOException e) {
                    }
                    if (is != null) is.close();
                    throw new BurlapProtocolException(code + ": " + sb.toString());
                }
            }
            is = conn.getInputStream();
            AbstractBurlapInput in = _factory.getBurlapInput(is);
            return in.readReply(method.getReturnType());
        } catch (BurlapProtocolException e) {
            throw new BurlapRuntimeException(e);
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
            }
            if (httpConn != null) httpConn.disconnect();
        }
    }
