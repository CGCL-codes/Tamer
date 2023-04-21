    public ServletOutputStream getOutputStream() {
        if (writer != null) throw new IllegalStateException("getWriter already called");
        writeHeaders();
        return out;
    }
