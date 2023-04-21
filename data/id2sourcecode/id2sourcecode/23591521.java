    @Override
    protected void handlePost(PageContext pageContext, Template template, VelocityContext templateContext) throws ServletException, IOException {
        IDAOSession f;
        try {
            DAOFactory df = (DAOFactory) FactoryRegistrar.getFactory(DAOFactory.class);
            f = df.getInstance();
        } catch (FactoryException e) {
            throw new ServletException("dao init error", e);
        }
        Test test;
        InputStream is = null;
        OutputStream os = null;
        try {
            String testString = pageContext.getParameter("test");
            if (testString == null) {
                throw new DAOException("No test parameter given");
            }
            Long testId = Long.valueOf(pageContext.getParameter("test"));
            f.beginTransaction();
            IStaffInterfaceQueriesDAO staffInterfaceQueriesDao = f.getStaffInterfaceQueriesDAOInstance();
            IAssignmentDAO assignmentDao = f.getAssignmentDAOInstance();
            ITestDAO testDao = f.getTestDAOInstance();
            test = testDao.retrievePersistentEntity(testId);
            Assignment assignment = assignmentDao.retrievePersistentEntity(test.getAssignmentId());
            if (!staffInterfaceQueriesDao.isStaffModuleAccessAllowed(pageContext.getSession().getPersonBinding().getId(), assignment.getModuleId())) {
                f.abortTransaction();
                throw new DAOException("permission denied");
            }
            IResourceDAO resourceDao = f.getResourceDAOInstance();
            Resource resource = resourceDao.retrievePersistentEntity(test.getLibraryResourceId());
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
            if (!fileItemStream.getName().toLowerCase().endsWith(".zip") && !fileItemStream.getName().toLowerCase().endsWith(".jar")) {
                throw new IOException("Upload is not a .zip or .jar");
            }
            resource.setFilename(fileItemStream.getName());
            resource.setMimeType("application/zip");
            is = fileItemStream.openStream();
            os = resourceDao.openOutputStream(test.getLibraryResourceId());
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
            templateContext.put("nextPage", pageContext.getPageUrl(StaffPageFactory.SITE_NAME, StaffPageFactory.TESTS_PAGE));
            templateContext.put("nextPageParamName", "assignment");
            templateContext.put("nextPageParamValue", test.getAssignmentId());
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
