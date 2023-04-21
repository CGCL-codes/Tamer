    public void handleThreadPosts(HttpServletRequest request, HttpServletResponse response) throws NGFException {
        logger.info("handleThreadPosts");
        String threadid = request.getParameter("threadid");
        ThreadPostBL postBL = null;
        try {
            logger.debug("start try handleThreadPosts = " + threadid);
            postBL = new ThreadPostBL();
            this.initAction(request, response);
            this.writeResponse(postBL.getPost(threadid));
        } catch (NGFException e) {
            this.initAction(request, response);
            this.writeResponse("<error>" + e.getErrorMessage() + "</error>");
            System.out.print("Search Forum Excepton = " + e.getMessage());
        } catch (Exception e) {
            logger.error("Exception=" + e.toString());
            this.initAction(request, response);
            this.writeResponse("<error>" + e.getMessage() + "</error>");
        }
    }
