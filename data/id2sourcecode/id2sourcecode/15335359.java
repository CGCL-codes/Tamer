    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (log.isDebugEnabled()) {
            log.debug("query string = " + request.getQueryString());
        }
        String workflowId = request.getParameter(WORKFLOW_ID);
        try {
            ApplicationGlobalContext context = new ApplicationGlobalContext();
            FileInputStream fis = new FileInputStream(context.getCredentials().getHostcertsKeyFile());
            GlobusCredential globusCred = new GlobusCredential(fis);
            context.setGssCredential(new GlobusGSSCredentialImpl(globusCred, GSSCredential.INITIATE_AND_ACCEPT));
            XBayaWorkflowUtil xRegistryUtil = new XBayaWorkflowUtil();
            this.workflow = xRegistryUtil.getWorkflowFromRegistry(workflowId, context);
            BufferedImage buffer = workflow.getWorkflowImage();
            if (buffer == null) {
                InputStream defaultImage = getServletContext().getResourceAsStream(DEFAULT_IMAGE_URL);
                response.setContentType("image/jpeg");
                OutputStream out = response.getOutputStream();
                ImageIO.write(ImageIO.read(defaultImage), "jpeg", out);
                out.close();
                return;
            }
            response.setContentType("image/png");
            if (buffer == null) {
                InputStream defaultImage = getServletContext().getResourceAsStream(DEFAULT_IMAGE_URL);
                response.setContentType("image/jpeg");
                OutputStream out = response.getOutputStream();
                ImageIO.write(ImageIO.read(defaultImage), "jpeg", out);
                out.close();
            }
            OutputStream out = response.getOutputStream();
            ImageIO.write(buffer, "png", out);
            out.close();
        } catch (Exception e) {
            System.out.println("Default Image");
            InputStream defaultImage = getServletContext().getResourceAsStream(DEFAULT_IMAGE_URL);
            response.setContentType("image/jpeg");
            OutputStream out = response.getOutputStream();
            ImageIO.write(ImageIO.read(defaultImage), "jpeg", out);
            out.close();
            e.printStackTrace();
        }
    }
