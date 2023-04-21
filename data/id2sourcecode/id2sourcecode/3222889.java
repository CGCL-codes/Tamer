    public void handleAddMessage(HttpServletRequest request, HttpServletResponse response, ArrayList chatMessages) throws NGFException {
        String roomId = request.getParameter("roomId");
        String memberId = request.getParameter("memberId");
        String message = request.getParameter("message");
        int messageId = chatMessages.size() + 1;
        System.out.println("Adding message no " + messageId);
        chatMessages.add(new ChatMessageVo(Integer.parseInt(roomId), Integer.parseInt(memberId), messageId, message));
        this.initAction(request, response);
        this.writeResponse("<message>Thread Created Successfully</message>");
    }
