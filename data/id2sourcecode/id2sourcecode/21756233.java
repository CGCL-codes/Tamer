    private boolean isReqDup0(Execution exec, AuWriter out, String sid) throws IOException {
        final Object[] resInfo = (Object[]) ((DesktopCtrl) exec.getDesktop()).getLastResponse(out.getChannel(), sid);
        if (resInfo != null) {
            if (log.debugable()) {
                final Object req = exec.getNativeRequest();
                log.debug("Repeat request\n" + (req instanceof ServletRequest ? Servlets.getDetail((ServletRequest) req) : "sid: " + sid));
            }
            out.writeResponseId(((Integer) resInfo[0]).intValue());
            out.write((Collection) resInfo[1]);
            return true;
        }
        return false;
    }
