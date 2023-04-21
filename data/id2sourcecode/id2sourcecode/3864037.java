    private void processSingleFile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (ServletFileUpload.isMultipartContent(request)) {
            UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userInfo");
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            try {
                List items = upload.parseRequest(request);
                String fileName = ((FileItem) (items.get(0))).getFieldName();
                String sessionId = request.getSession().getId();
                File dir = new File("tempFiles\\" + sessionId);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File("tempFiles\\" + sessionId + "\\" + fileName);
                if (file.exists()) {
                    file.delete();
                }
                try {
                    ((FileItem) (items.get(0))).write(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String classFileName = getClassFileName("tempFiles\\" + sessionId + "\\" + fileName);
                String tempFilePath = "tempFiles\\" + sessionId + "\\" + classFileName + ".java";
                File file2 = new File(tempFilePath);
                FileUtils.copyFile(file, file2);
                JavaUtil.getInstance().compile(tempFilePath);
                System.out.println(JavaUtil.getInstance().getResult());
                DetectEngine engine = new DetectEngine();
                engine.reportFromFile("tempFiles\\" + sessionId + "\\" + classFileName + ".java", userInfo);
                HttpSession session = request.getSession();
                session.setAttribute("reports", engine.getReports());
                RequestDispatcher dispatcher = request.getRequestDispatcher("/showReports.jsp");
                dispatcher.forward(request, response);
            } catch (FileUploadException e) {
                e.printStackTrace();
            }
        }
    }
