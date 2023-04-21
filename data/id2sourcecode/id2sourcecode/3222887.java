    public void handleRemoveUser(HttpServletRequest request, HttpServletResponse response, ArrayList roomUsers) throws NGFException {
        int roomId = Integer.parseInt(request.getParameter("roomId"));
        int memberId = Integer.parseInt(request.getParameter("memberId"));
        StringTokenizer stk = null;
        for (int i = 0; i < roomUsers.size(); i++) {
            stk = new StringTokenizer((String) roomUsers.get(i), ",");
            if (roomId == Integer.parseInt(stk.nextToken()) && memberId == Integer.parseInt(stk.nextToken())) {
                roomUsers.remove(i);
                break;
            }
        }
        this.initAction(request, response);
        this.writeResponse("<message>Thread Created Successfully</message>");
    }
