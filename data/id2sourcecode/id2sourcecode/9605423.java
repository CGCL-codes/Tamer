    private void download(HttpServletRequest req, HttpServletResponse resp, QueryDB query) throws ServletException, IOException {
        FaqUser fuser = null;
        String logon = req.getRemoteUser();
        if (null != logon && logon.length() > 0) {
            try {
                fuser = query.getFaqUser(logon, false);
            } catch (UserNotFoundException unfe) {
                throw (ServletException) new ServletException("FaqUser with logon \"" + req.getRemoteUser() + "\" not found").initCause(unfe);
            }
        } else {
            throw new ServletException("Remote user not authenticated");
        }
        Project project = null;
        String parameter = req.getParameter(PROJECT);
        if (null != parameter && parameter.length() > 0) {
            int projId = -1;
            try {
                projId = Integer.parseInt(parameter);
            } catch (NumberFormatException nfe) {
                throw (ServletException) new ServletException("\"" + PROJECT + "\" parameter must be a valid integer").initCause(nfe);
            }
            try {
                project = fuser.getProject(projId);
            } catch (ProjectNotFoundException pnfe) {
                throw (ServletException) new ServletException("Project with id \"" + projId + "\" not found").initCause(pnfe);
            }
        } else {
            throw new ServletException("\"" + PROJECT + "\" parameter must be specified");
        }
        Faq faq = null;
        parameter = req.getParameter(FAQ);
        if (null != parameter && parameter.length() > 0) {
            int faqId = -1;
            try {
                faqId = Integer.parseInt(parameter);
            } catch (NumberFormatException nfe) {
                throw (ServletException) new ServletException("\"" + FAQ + "\" parameter must be a valid integer").initCause(nfe);
            }
            try {
                faq = project.getFaq(faqId);
            } catch (FaqNotFoundException pnfe) {
                throw (ServletException) new ServletException("Faq with id \"" + faqId + "\" not found").initCause(pnfe);
            }
        } else {
            throw new ServletException("\"" + FAQ + "\" parameter must be specified");
        }
        Question question = null;
        parameter = req.getParameter(QUESTION);
        if (null != parameter && parameter.length() > 0) {
            int questionId = -1;
            try {
                questionId = Integer.parseInt(parameter);
            } catch (NumberFormatException nfe) {
                throw (ServletException) new ServletException("\"" + QUESTION + "\" parameter must be a valid integer").initCause(nfe);
            }
            try {
                question = faq.getQuestion(questionId);
            } catch (QuestionNotFoundException pnfe) {
                throw (ServletException) new ServletException("Question with id \"" + questionId + "\" not found").initCause(pnfe);
            }
        } else {
            throw new ServletException("\"" + QUESTION + "\" parameter must be specified");
        }
        Answer answer = null;
        parameter = req.getParameter(ANSWER);
        if (null != parameter && parameter.length() > 0) {
            int answerId = -1;
            try {
                answerId = Integer.parseInt(parameter);
            } catch (NumberFormatException nfe) {
                throw (ServletException) new ServletException("\"" + ANSWER + "\" parameter must be a valid integer").initCause(nfe);
            }
            try {
                answer = question.getAnswer(answerId);
            } catch (AnswerNotFoundException pnfe) {
                throw (ServletException) new ServletException("Answer with id \"" + answerId + "\" not found").initCause(pnfe);
            }
        } else {
            throw new ServletException("\"" + ANSWER + "\" parameter must be specified");
        }
        Attachment attachment = null;
        parameter = req.getParameter(ATTACHMENT);
        if (null != parameter && parameter.length() > 0) {
            int attachmentId = -1;
            try {
                attachmentId = Integer.parseInt(parameter);
            } catch (NumberFormatException nfe) {
                throw (ServletException) new ServletException("\"" + ATTACHMENT + "\" parameter must be a " + "valid integer").initCause(nfe);
            }
            try {
                attachment = answer.getAttachment(attachmentId);
                logger.info("attachment.id: " + attachment.getId());
            } catch (AttachmentNotFoundException pnfe) {
                throw (ServletException) new ServletException("Attachment with id \"" + attachmentId + "\" not found").initCause(pnfe);
            }
        } else {
            throw new ServletException("\"" + ATTACHMENT + "\" parameter must be specified");
        }
        String ct = attachment.getFileType();
        resp.setContentType(ct);
        logger.info("Content-Type: " + ct);
        StringBuffer disp = new StringBuffer();
        disp.append("attachment; filename=\"");
        disp.append(attachment.getFileName());
        disp.append("\"");
        resp.setHeader("Content-Disposition", disp.toString());
        logger.info("Content-Disposition: " + disp.toString());
        resp.setHeader("Cache-Control", "private");
        OutputStream ostream = resp.getOutputStream();
        BufferedInputStream istream = new BufferedInputStream(attachment.getAttachment());
        byte[] bytes = new byte[BLOCK];
        do {
            int nread = istream.read(bytes, 0, BLOCK);
            logger.info("number bytes read: " + nread);
            if (nread < 0) {
                break;
            }
            ostream.write(bytes, 0, nread);
        } while (true);
        ostream.flush();
        ostream.close();
    }
