    public void handleForumThread(HttpServletRequest request, HttpServletResponse response) throws NGFException {
        String threadid = request.getParameter("threadid");
        ThreadPostBL postBL = null;
        try {
            logger.info("start handleForumThread ");
            postBL = new ThreadPostBL();
            this.initAction(request, response);
            this.writeResponse(postBL.getChilds(threadid));
        } catch (NGFException e) {
            this.initAction(request, response);
            this.writeResponse("<error>" + e.getErrorMessage() + "</error>");
            System.out.print("\n\n\n\nThread Excepton = " + e.getErrorMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error("Exception=" + e.toString());
            this.initAction(request, response);
            this.writeResponse("<error>" + e.getMessage() + "</error>");
        }
    }
