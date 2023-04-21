    @Override
    protected void handlePost(PageContext pageContext, Template template, VelocityContext templateContext) throws ServletException, IOException {
        IDAOSession f;
        try {
            DAOFactory df = (DAOFactory) FactoryRegistrar.getFactory(DAOFactory.class);
            f = df.getInstance();
        } catch (FactoryException e) {
            throw new ServletException("dao init error", e);
        }
        Assignment assignment;
        InputStream is = null;
        OutputStream os = null;
        try {
            String assignmentString = pageContext.getParameter("assignment");
            if (assignmentString == null) {
                throw new DAOException("No assignment parameter given");
            }
            Long assignmentId = Long.valueOf(pageContext.getParameter("assignment"));
            f.beginTransaction();
            IStaffInterfaceQueriesDAO staffInterfaceQueriesDao = f.getStaffInterfaceQueriesDAOInstance();
            IAssignmentDAO assignmentDao = f.getAssignmentDAOInstance();
            assignment = assignmentDao.retrievePersistentEntity(assignmentId);
            if (!staffInterfaceQueriesDao.isStaffModuleAccessAllowed(pageContext.getSession().getPersonBinding().getId(), assignment.getModuleId())) {
                f.abortTransaction();
                throw new DAOException("permission denied");
            }
            IResourceDAO resourceDao = f.getResourceDAOInstance();
            Resource resource = resourceDao.retrievePersistentEntity(assignment.getResourceId());
            FileItemIterator fileIterator = pageContext.getUploadedFiles();
            FileItemStream fileItemStream = null;
            while (fileIterator.hasNext()) {
                FileItemStream nextFileItemStream = fileIterator.next();
                if (nextFileItemStream.getFieldName().equals("resource")) {
                    fileItemStream = nextFileItemStream;
                    break;
                }
            }
            if (fileItemStream == null) {
                throw new DAOException("Expected an uploaded file!");
            }
            if (!fileItemStream.getName().endsWith(".zip")) {
                throw new DAOException("Expected uploaded file to be a zip!");
            }
            resource.setFilename(fileItemStream.getName());
            resource.setMimeType("application/zip");
            is = fileItemStream.openStream();
            os = resourceDao.openOutputStream(assignment.getResourceId());
            byte buffer[] = new byte[1024];
            int nread = -1;
            long total = 0;
            while ((nread = is.read(buffer)) != -1) {
                total += nread;
                os.write(buffer, 0, nread);
            }
            if (total == 0) {
                throw new DAOException("Expected an uploaded file!");
            }
            is.close();
            os.flush();
            os.close();
            resourceDao.updatePersistentEntity(resource);
            templateContext.put("greet", pageContext.getSession().getPersonBinding().getChosenName());
            templateContext.put("success", true);
            templateContext.put("nextPage", pageContext.getPageUrl(StaffPageFactory.SITE_NAME, StaffPageFactory.ASSIGNMENTS_PAGE));
            templateContext.put("nextPageParamName", "assignment");
            templateContext.put("nextPageParamValue", assignmentId);
            f.endTransaction();
        } catch (Exception e) {
            f.abortTransaction();
            if (os != null) {
                os.flush();
                os.close();
            }
            if (is != null) {
                is.close();
            }
            throw new ServletException("upload error", e);
        }
        pageContext.renderTemplate(template, templateContext);
    }
