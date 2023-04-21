    protected void handlePost(PageContext pageContext, Template template, VelocityContext templateContext) throws ServletException, IOException {
        IDAOSession f;
        IMailSender mailSender;
        String submissionHash;
        try {
            DAOFactory df = (DAOFactory) FactoryRegistrar.getFactory(DAOFactory.class);
            MailFactory mf = (MailFactory) FactoryRegistrar.getFactory(MailFactory.class);
            f = df.getInstance();
            mailSender = mf.getInstance();
            submissionHash = df.getSubmissionHashSalt();
        } catch (FactoryException e) {
            throw new ServletException("dao init error", e);
        }
        Assignment assignment = null;
        String assignmentString = pageContext.getParameter("assignment");
        if (assignmentString == null) {
            throw new ServletException("No assignment parameter given");
        }
        Long assignmentId = Long.valueOf(pageContext.getParameter("assignment"));
        Collection<String> fileNames = null;
        try {
            f.beginTransaction();
            IAssignmentDAO assignmentDao = f.getAssignmentDAOInstance();
            IStudentInterfaceQueriesDAO studentInterfaceQueriesDAO = f.getStudentInterfaceQueriesDAOInstance();
            assignment = assignmentDao.retrievePersistentEntity(assignmentId);
            if (!studentInterfaceQueriesDAO.isStudentModuleAccessAllowed(pageContext.getSession().getPersonBinding().getId(), assignment.getModuleId())) {
                f.abortTransaction();
                throw new DAOException("permission denied (not on module)");
            }
            if (!studentInterfaceQueriesDAO.isStudentAllowedToSubmit(pageContext.getSession().getPersonBinding().getId(), assignmentId)) {
                f.abortTransaction();
                throw new DAOException("permission denied (after deadline)");
            }
            templateContext.put("assignment", assignment);
            fileNames = assignmentDao.fetchRequiredFilenames(assignmentId);
            f.endTransaction();
        } catch (DAOException e) {
            f.abortTransaction();
            throw new ServletException("dao exception", e);
        }
        Long resourceId = null;
        Resource resource = new Resource();
        resource.setTimestamp(new Date());
        resource.setFilename(pageContext.getSession().getPersonBinding().getUniqueIdentifier() + "-" + assignmentId + "-" + resource.getTimestamp().getTime() + ".zip");
        resource.setMimeType("application/zip");
        try {
            f.beginTransaction();
            IResourceDAO resourceDao = f.getResourceDAOInstance();
            resourceId = resourceDao.createPersistentCopy(resource);
            f.endTransaction();
        } catch (DAOException e) {
            f.abortTransaction();
            throw new ServletException("dao exception", e);
        }
        String securityCode = null;
        OutputStream resourceStream = null;
        ZipOutputStream resourceZipStream = null;
        MessageDigest digest = null;
        HashSet<String> remainingFiles = new HashSet<String>(fileNames);
        HashSet<String> omittedFiles = new HashSet<String>();
        try {
            f.beginTransaction();
            IResourceDAO resourceDao = f.getResourceDAOInstance();
            digest = MessageDigest.getInstance("MD5");
            resourceStream = resourceDao.openOutputStream(resourceId);
            resourceZipStream = new ZipOutputStream(resourceStream);
            FileItemIterator fileIterator = pageContext.getUploadedFiles();
            while (fileIterator.hasNext()) {
                FileItemStream currentUpload = fileIterator.next();
                if (fileNames.contains(currentUpload.getFieldName())) {
                    String filename = pageContext.getSession().getPersonBinding().getUniqueIdentifier() + "/" + currentUpload.getFieldName();
                    InputStream is = currentUpload.openStream();
                    try {
                        byte buffer[] = new byte[1024];
                        int nread = -1;
                        long total = 0;
                        nread = is.read(buffer, 0, 1);
                        if (nread == 1) {
                            resourceZipStream.putNextEntry(new ZipEntry(filename));
                            resourceZipStream.write(buffer, 0, nread);
                            digest.update(buffer, 0, nread);
                            while ((nread = is.read(buffer)) != -1) {
                                total += nread;
                                resourceZipStream.write(buffer, 0, nread);
                                digest.update(buffer, 0, nread);
                            }
                            resourceZipStream.closeEntry();
                            remainingFiles.remove(currentUpload.getFieldName());
                            pageContext.log(Level.INFO, "Student uploaded: " + currentUpload.getFieldName());
                        }
                        is.close();
                    } catch (IOException e) {
                        throw new DAOException("IO error returning file stream", e);
                    }
                } else if (currentUpload.getFieldName().equals("omit")) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(currentUpload.openStream()));
                    String fileName = reader.readLine();
                    omittedFiles.add(fileName);
                    reader.close();
                    pageContext.log(Level.INFO, "Student omitted: " + fileName);
                }
            }
            if (omittedFiles.equals(fileNames)) {
                pageContext.log(Level.ERROR, "Student tried to upload nothing!");
                resourceStream.close();
                pageContext.performRedirect(pageContext.getPageUrl("student", "submit") + "?assignment=" + assignmentId + "&missing=true");
                resourceDao.deletePersistentEntity(resourceId);
                f.endTransaction();
                return;
            }
            if (!remainingFiles.isEmpty()) {
                for (String fileName : remainingFiles) {
                    if (!omittedFiles.contains(fileName)) {
                        pageContext.log(Level.ERROR, "Student didn't upload " + fileName + " but didn't omit it!");
                        resourceStream.close();
                        pageContext.performRedirect(pageContext.getPageUrl("student", "submit") + "?assignment=" + assignmentId + "&missing=true");
                        resourceDao.deletePersistentEntity(resourceId);
                        f.endTransaction();
                        return;
                    }
                }
            }
            resourceZipStream.flush();
            resourceStream.flush();
            resourceZipStream.close();
            resourceStream.close();
            securityCode = byteArrayToHexString(digest.digest());
            f.endTransaction();
        } catch (Exception e) {
            resourceStream.close();
            f.abortTransaction();
            try {
                f.beginTransaction();
                IResourceDAO resourceDao = f.getResourceDAOInstance();
                resourceDao.deletePersistentEntity(resourceId);
                f.endTransaction();
            } catch (DAOException e2) {
                throw new ServletException("error storing upload - additional error cleaning stale resource " + resourceId, e);
            }
            throw new ServletException("error storing upload", e);
        }
        securityCode = securityCode + submissionHash;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            securityCode = byteArrayToHexString(digest.digest(securityCode.getBytes("UTF-8")));
        } catch (Exception e) {
            f.abortTransaction();
            try {
                f.beginTransaction();
                IResourceDAO resourceDao = f.getResourceDAOInstance();
                resourceDao.deletePersistentEntity(resourceId);
                f.endTransaction();
            } catch (DAOException e2) {
                throw new ServletException("error hashing - additional error cleaning stale resource " + resourceId, e);
            }
            throw new ServletException("error hashing", e);
        }
        Submission submission = new Submission();
        submission.setPersonId(pageContext.getSession().getPersonBinding().getId());
        submission.setAssignmentId(assignmentId);
        submission.setSubmissionTime(new Date());
        submission.setSecurityCode(securityCode);
        submission.setResourceId(resourceId);
        submission.setResourceSubdirectory(pageContext.getSession().getPersonBinding().getUniqueIdentifier());
        submission.setActive(false);
        try {
            f.beginTransaction();
            ISubmissionDAO submissionDao = f.getSubmissionDAOInstance();
            IStudentInterfaceQueriesDAO studentInterfaceQueriesDAo = f.getStudentInterfaceQueriesDAOInstance();
            submission.setId(submissionDao.createPersistentCopy(submission));
            studentInterfaceQueriesDAo.makeSubmissionActive(submission.getPersonId(), submission.getAssignmentId(), submission.getId());
            f.endTransaction();
        } catch (DAOException e) {
            f.abortTransaction();
            try {
                f.beginTransaction();
                IResourceDAO resourceDao = f.getResourceDAOInstance();
                resourceDao.deletePersistentEntity(resourceId);
                f.endTransaction();
            } catch (DAOException e2) {
                throw new ServletException("dao error occured - additional error cleaning stale resource " + resourceId, e);
            }
            throw new ServletException("dao error occured", e);
        }
        templateContext.put("person", pageContext.getSession().getPersonBinding());
        templateContext.put("submission", submission);
        templateContext.put("now", new Date());
        StringWriter pw = new StringWriter();
        emailTemplate.merge(templateContext, pw);
        try {
            mailSender.sendMail(pageContext.getSession().getPersonBinding().getEmailAddress(), "Submission (" + assignment.getName() + ")", pw.toString());
            templateContext.put("mailSent", true);
            pageContext.log(Level.INFO, "student submission mail sent (email: " + pageContext.getSession().getPersonBinding().getEmailAddress() + ") (code: " + securityCode + ")");
        } catch (MailException e) {
            templateContext.put("mailSent", false);
            pageContext.log(Level.ERROR, "student submission mail NOT sent (email: " + pageContext.getSession().getPersonBinding().getEmailAddress() + ") (code: " + securityCode + ")");
            pageContext.log(Level.ERROR, e);
        }
        pageContext.log(Level.INFO, "student submission successful (student: " + pageContext.getSession().getPersonBinding().getUniqueIdentifier() + ") (code: " + securityCode + ") (submission: " + submission.getId() + ")");
        templateContext.put("greet", pageContext.getSession().getPersonBinding().getChosenName());
        pageContext.renderTemplate(template, templateContext);
    }
