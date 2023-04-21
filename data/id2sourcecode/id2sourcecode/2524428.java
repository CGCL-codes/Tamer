    private void writeText(String pagerep, Reader r) throws Exception {
        PrintWriter w = WebLocal.getWebInteractionContext().getWriter();
        char[] chars = new char[1024];
        int numread = -1;
        while ((numread = r.read(chars)) != -1) {
            w.write(chars, 0, numread);
        }
        w.flush();
    }
