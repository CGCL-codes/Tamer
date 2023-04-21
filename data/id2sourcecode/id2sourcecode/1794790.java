    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            PrintWriter writer = response.getWriter();
            PortalRequest pr = PortalRequest.getCurrentRequest();
            String allReadyRun = Property.getPropertyValue(POST_INSTALL_PROPERTY);
            if (allReadyRun != null && allReadyRun.equalsIgnoreCase("True")) {
                writer.println("Post Install Has Already Been Run!!");
            } else {
                Query query = HibernateUtil.currentSession().createQuery("from com.c2b2.ipoint.model.PortletType pt where pt.Name = 'com.c2b2.ipoint.presentation.portlets.PortalFileRepository'");
                PortletType docRepoType = (PortletType) query.uniqueResult();
                Portlet portlet = Portlet.createPortlet("Portal Files", pr.getCurrentUser(), docRepoType);
                PortalFileRepository prenderer = (PortalFileRepository) pr.getRenderFactory().getPortletRenderer(portlet);
                prenderer.initialiseNew();
                Query pageQuery = HibernateUtil.currentSession().createQuery("from com.c2b2.ipoint.model.Page p where p.Name = 'Portal Files'");
                Page page = (Page) pageQuery.uniqueResult();
                Set<View> views = page.getViews();
                View view = views.iterator().next();
                view.addPortlet(2, portlet);
                Property.createProperty(POST_INSTALL_PROPERTY, "True");
                Property.createProperty("PortalDocumentRepository", Long.toString(prenderer.getRepository().getID()));
                writer.println("Post Install Run Successfully <a href='ipoint' title='Return to Home Page'>Return to the Home page </a>");
            }
        } catch (PersistentModelException e) {
            throw new ServletException(e);
        } catch (PresentationException e) {
            throw new ServletException(e);
        } catch (IOException ioe) {
            throw new ServletException(ioe);
        }
    }
