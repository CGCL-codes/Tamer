    private void getChannels(HttpServletResponse resp) throws IOException {
        lstChannels = AppMisc.getChannels();
        if (lstChannels.size() == 0) return;
        for (int index = 0; index < lstChannels.size(); index++) {
            resp.getWriter().print(lstChannels.get(index).getName() + "|");
            resp.getWriter().print(lstChannels.get(index).getRssLink() + "|");
            resp.getWriter().print(lstChannels.get(index).getRatingCount() + "|");
            resp.getWriter().print("~");
        }
        if (!AppMisc.cursorString.isEmpty()) {
            resp.getWriter().print(AppMisc.cursorString);
        }
    }
