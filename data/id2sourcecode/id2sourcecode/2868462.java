    public void handleMyPost(HttpServletRequest request, HttpServletResponse response) throws NGFException {
        HomeBL homeBL = new HomeBL();
        try {
            logger.info("start handleMyPosts");
            String memberId = request.getParameter("memberId");
            System.out.println("handleMyPost memberId= " + memberId);
            this.initAction(request, response);
            this.writeResponse(homeBL.generateMemberThreadData(memberId));
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
