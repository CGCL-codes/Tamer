    public void handleDeleteThread(HttpServletRequest request, HttpServletResponse response) throws NGFException {
        logger.info("handleDeleteThread");
        String threadid = request.getParameter("threadid");
        System.out.println("handleDeleteThread  thread id = " + threadid);
        Posts posts = new Posts();
        ThreadPostBL threadPostBL = new ThreadPostBL();
        try {
            logger.debug("start try handleDeleteThread = " + threadid);
            posts.setPostId(Integer.parseInt(threadid));
            threadPostBL.deleteThread(posts);
            this.initAction(request, response);
            this.writeResponse("<message>Thread Deletes </message>");
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
