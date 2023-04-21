    protected void doBody(CalDAVTransaction transaction, HttpServletResponse resp, String path) {
        try {
            StoredObject so = this._store.getStoredObject(transaction, path);
            if (so.isNullResource()) {
                String methodsAllowed = CalDAVMethods.determineMethodsAllowed(so);
                resp.addHeader("Allow", methodsAllowed);
                resp.sendError(CalDAVResponse.SC_METHOD_NOT_ALLOWED);
                return;
            }
            OutputStream out = resp.getOutputStream();
            InputStream in = this._store.getResourceContent(transaction, path);
            try {
                int read = -1;
                byte[] copyBuffer = new byte[BUF_SIZE];
                while ((read = in.read(copyBuffer, 0, copyBuffer.length)) != -1) {
                    out.write(copyBuffer, 0, read);
                }
            } finally {
                try {
                    in.close();
                } catch (Exception _ex) {
                }
                try {
                    out.flush();
                    out.close();
                } catch (Exception _ex) {
                }
            }
        } catch (AccessDeniedException _ex) {
            try {
                resp.sendError(CalDAVResponse.SC_FORBIDDEN);
            } catch (Exception _ex2) {
            }
        } catch (Exception _ex) {
        }
    }
