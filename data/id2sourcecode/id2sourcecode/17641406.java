    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        ServletOutputStream out = response.getOutputStream();
        int msgNum = Integer.parseInt(request.getParameter("message"));
        int partNum = Integer.parseInt(request.getParameter("part"));
        MailUserBean mailuser = (MailUserBean) session.getAttribute("mailuser");
        if (mailuser.isLoggedIn()) {
            try {
                Message msg = mailuser.getFolder().getMessage(msgNum);
                Multipart multipart = (Multipart) msg.getContent();
                Part part = multipart.getBodyPart(partNum);
                String sct = part.getContentType();
                if (sct == null) {
                    out.println("invalid part");
                    return;
                }
                ContentType ct = new ContentType(sct);
                response.setContentType(ct.getBaseType());
                InputStream is = part.getInputStream();
                int i;
                while ((i = is.read()) != -1) out.write(i);
                out.flush();
                out.close();
            } catch (MessagingException ex) {
                throw new ServletException(ex.getMessage());
            }
        } else {
            getServletConfig().getServletContext().getRequestDispatcher("/index.html").forward(request, response);
        }
    }
