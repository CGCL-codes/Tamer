    public void handleCreateThread(HttpServletRequest request, HttpServletResponse response, String path) throws NGFException {
        logger.info("handleCreateThread");
        ThreadBL threadBL = new ThreadBL();
        try {
            Posts threadPostVo = new Posts();
            String title = request.getParameter("title");
            logger.debug("title = " + title);
            String message = request.getParameter("message");
            String parentId = request.getParameter("postId");
            String memberId = request.getParameter("memberId");
            String fileName = request.getParameter("fileName");
            logger.debug("title = " + title);
            logger.debug("message = " + message);
            logger.debug("parent = " + parentId);
            threadPostVo.setNoOfView(0);
            threadPostVo.setTitle(title);
            threadPostVo.setMessage(message);
            Date currentDate = Utility.convertStringToDate(Utility.getSQLTimeDate(), FormatConstants.MMDDYYhhmmssa_SEPARATOR_SLASH);
            System.out.println("time= " + currentDate);
            threadPostVo.setPostDate(currentDate);
            threadPostVo.setIsLeaf("true");
            threadPostVo.setPostType('T');
            if (!(fileName.equalsIgnoreCase("") || fileName == null)) {
                threadPostVo.setFilePath(path + fileName);
            }
            Posts post = new Posts();
            post.setPostId(Integer.parseInt(parentId));
            threadPostVo.setPosts(post);
            Member member = new Member();
            member.setId(Integer.parseInt(memberId));
            threadPostVo.setMember(member);
            threadBL.saveThread(threadPostVo);
            this.initAction(request, response);
            this.writeResponse("<message>Thread Created Successfully</message>");
        } catch (NGFException e) {
            this.initAction(request, response);
            this.writeResponse("<error>" + e.getErrorMessage() + "</error>");
            System.out.print("Search Forum Excepton = " + e.getMessage());
        } catch (Exception e) {
            this.initAction(request, response);
            this.writeResponse("<error>" + e.getMessage() + "</error>");
            System.out.print("Create Thread Excepton = " + e.getMessage());
        }
    }
