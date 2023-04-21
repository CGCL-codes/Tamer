    public void _jspService(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
        PageContext pageContext = null;
        HttpSession session = null;
        ServletContext application = null;
        ServletConfig config = null;
        JspWriter out = null;
        Object page = this;
        JspWriter _jspx_out = null;
        PageContext _jspx_page_context = null;
        try {
            response.setContentType("text/html;charset=UTF-8");
            pageContext = _jspxFactory.getPageContext(this, request, response, null, true, 8192, true);
            _jspx_page_context = pageContext;
            application = pageContext.getServletContext();
            config = pageContext.getServletConfig();
            session = pageContext.getSession();
            out = pageContext.getOut();
            _jspx_out = out;
            _jspx_resourceInjector = (org.apache.jasper.runtime.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            com.sun.portal.portletcontainer.taglib.DefineObjectsTag _jspx_th_portlet_defineObjects_0 = (com.sun.portal.portletcontainer.taglib.DefineObjectsTag) _jspx_tagPool_portlet_defineObjects_nobody.get(com.sun.portal.portletcontainer.taglib.DefineObjectsTag.class);
            _jspx_th_portlet_defineObjects_0.setPageContext(_jspx_page_context);
            _jspx_th_portlet_defineObjects_0.setParent(null);
            int _jspx_eval_portlet_defineObjects_0 = _jspx_th_portlet_defineObjects_0.doStartTag();
            if (_jspx_th_portlet_defineObjects_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _jspx_tagPool_portlet_defineObjects_nobody.reuse(_jspx_th_portlet_defineObjects_0);
                return;
            }
            _jspx_tagPool_portlet_defineObjects_nobody.reuse(_jspx_th_portlet_defineObjects_0);
            javax.portlet.RenderRequest renderRequest = null;
            javax.portlet.RenderResponse renderResponse = null;
            javax.portlet.ActionRequest actionRequest = null;
            javax.portlet.ActionResponse actionResponse = null;
            javax.portlet.EventRequest eventRequest = null;
            javax.portlet.EventResponse eventResponse = null;
            javax.portlet.ResourceRequest resourceRequest = null;
            javax.portlet.ResourceResponse resourceResponse = null;
            javax.portlet.PortletConfig portletConfig = null;
            java.lang.String portletName = null;
            javax.portlet.PortletSession portletSession = null;
            java.util.Map<String, String[]> portletSessionScope = null;
            javax.portlet.PortletPreferences portletPreferences = null;
            java.util.Map<String, String[]> portletPreferencesValues = null;
            renderRequest = (javax.portlet.RenderRequest) _jspx_page_context.findAttribute("renderRequest");
            renderResponse = (javax.portlet.RenderResponse) _jspx_page_context.findAttribute("renderResponse");
            actionRequest = (javax.portlet.ActionRequest) _jspx_page_context.findAttribute("actionRequest");
            actionResponse = (javax.portlet.ActionResponse) _jspx_page_context.findAttribute("actionResponse");
            eventRequest = (javax.portlet.EventRequest) _jspx_page_context.findAttribute("eventRequest");
            eventResponse = (javax.portlet.EventResponse) _jspx_page_context.findAttribute("eventResponse");
            resourceRequest = (javax.portlet.ResourceRequest) _jspx_page_context.findAttribute("resourceRequest");
            resourceResponse = (javax.portlet.ResourceResponse) _jspx_page_context.findAttribute("resourceResponse");
            portletConfig = (javax.portlet.PortletConfig) _jspx_page_context.findAttribute("portletConfig");
            portletName = (java.lang.String) _jspx_page_context.findAttribute("portletName");
            portletSession = (javax.portlet.PortletSession) _jspx_page_context.findAttribute("portletSession");
            portletSessionScope = (java.util.Map<String, String[]>) _jspx_page_context.findAttribute("portletSessionScope");
            portletPreferences = (javax.portlet.PortletPreferences) _jspx_page_context.findAttribute("portletPreferences");
            portletPreferencesValues = (java.util.Map<String, String[]>) _jspx_page_context.findAttribute("portletPreferencesValues");
            out.write("\r\n");
            out.write("\r\n");
            ISwingServices swingServices = SwingServices.createDelegate(renderRequest, renderResponse.getNamespace(), "admin");
            if (swingServices.getJSessionId() != null) {
                out.write("\r\n");
                out.write("            <script type=\"text/javascript\">\r\n");
                out.write("            // apply after DOM loading is ready\r\n");
                out.write("           Ext.onReady(function() {\r\n");
                out.write("\r\n");
                out.write("                    // reference local blank image\r\n");
                out.write("                    Ext.BLANK_IMAGE_URL = '/swing-theme-bi/SwingResources/scripts/ext/resources/images/default/s.gif';\r\n");
                out.write("\r\n");
                out.write("                    var adminView = new Swing.windows.AdminWin('");
                out.print(swingServices.getSwingQueryPath());
                out.write("',\r\n");
                out.write("                        '");
                out.print(swingServices.getNamespace());
                out.write("',\r\n");
                out.write("                        'Swing Admin',\r\n");
                out.write("                        '");
                out.print(swingServices.getLanguage());
                out.write("');\r\n");
                out.write("                    Ext.QuickTips.init();\r\n");
                out.write("            });\r\n");
                out.write("            </script>\r\n");
                out.write("\r\n");
                out.write("            <!-- for generating EXTJS panels  -->\r\n");
                out.write("            <div id=\"SwingBI_DOMPanel\" ></div>\r\n");
            } else {
                out.println("Session expired or invalidated!");
            }
        } catch (Throwable t) {
            if (!(t instanceof SkipPageException)) {
                out = _jspx_out;
                if (out != null && out.getBufferSize() != 0) out.clearBuffer();
                if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
            }
        } finally {
            _jspxFactory.releasePageContext(_jspx_page_context);
        }
    }
