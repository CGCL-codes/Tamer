    public void _jspService(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
        JspFactory _jspxFactory = null;
        PageContext pageContext = null;
        HttpSession session = null;
        ServletContext application = null;
        ServletConfig config = null;
        JspWriter out = null;
        Object page = this;
        JspWriter _jspx_out = null;
        PageContext _jspx_page_context = null;
        try {
            _jspxFactory = JspFactory.getDefaultFactory();
            response.setContentType("text/html");
            pageContext = _jspxFactory.getPageContext(this, request, response, null, true, 8192, true);
            _jspx_page_context = pageContext;
            application = pageContext.getServletContext();
            config = pageContext.getServletConfig();
            session = pageContext.getSession();
            out = pageContext.getOut();
            _jspx_out = out;
            out.write("\n");
            out.write("\n");
            out.write("\n");
            out.write("\n");
            out.write("\n");
            out.write("\n");
            out.write("\n");
            out.write("\n");
            out.write("\n");
            out.write("\n");
            org.openxava.util.Messages errors = null;
            synchronized (request) {
                errors = (org.openxava.util.Messages) _jspx_page_context.getAttribute("errors", PageContext.REQUEST_SCOPE);
                if (errors == null) {
                    errors = new org.openxava.util.Messages();
                    _jspx_page_context.setAttribute("errors", errors, PageContext.REQUEST_SCOPE);
                }
            }
            out.write('\n');
            org.openxava.util.Messages messages = null;
            synchronized (request) {
                messages = (org.openxava.util.Messages) _jspx_page_context.getAttribute("messages", PageContext.REQUEST_SCOPE);
                if (messages == null) {
                    messages = new org.openxava.util.Messages();
                    _jspx_page_context.setAttribute("messages", messages, PageContext.REQUEST_SCOPE);
                }
            }
            out.write('\n');
            org.openxava.controller.ModuleContext context = null;
            synchronized (session) {
                context = (org.openxava.controller.ModuleContext) _jspx_page_context.getAttribute("context", PageContext.SESSION_SCOPE);
                if (context == null) {
                    context = new org.openxava.controller.ModuleContext();
                    _jspx_page_context.setAttribute("context", context, PageContext.SESSION_SCOPE);
                }
            }
            out.write('\n');
            org.openxava.web.style.Style style = null;
            synchronized (request) {
                style = (org.openxava.web.style.Style) _jspx_page_context.getAttribute("style", PageContext.REQUEST_SCOPE);
                if (style == null) {
                    style = new org.openxava.web.style.Style();
                    _jspx_page_context.setAttribute("style", style, PageContext.REQUEST_SCOPE);
                }
            }
            out.write('\n');
            out.write('\n');
            if (request.getParameter("parent") == null) {
                out.write('\n');
                out.write("<!-- JavaScript for calendar in date editors -->\n");
                out.write("\n");
                out.write("<!-- Loading Theme file(s) -->\n");
                out.write("<link rel=\"stylesheet\" type=\"text/css\" media=\"all\" href=\"");
                out.print(request.getContextPath());
                out.write("/xava/editors/calendar/skins/aqua/theme.css\" title=\"Aqua\" />\n");
                out.write("\n");
                if (!(style instanceof org.openxava.web.style.LiferayStyle)) {
                    out.write("\n");
                    out.write("<!-- import the calendar script -->\n");
                    out.write("<script type=\"text/javascript\" src=\"");
                    out.print(request.getContextPath());
                    out.write("/xava/editors/calendar/calendar.js\"></script>\n");
                    out.write("\n");
                    out.write("<!-- import the language module -->\n");
                    out.write("<script type=\"text/javascript\" src=\"");
                    out.print(request.getContextPath());
                    out.write("/xava/editors/calendar/lang/calendar-");
                    out.print(request.getLocale().getLanguage());
                    out.write(".js\"></script>\n");
                    out.write("\n");
                }
                out.write("\n");
                out.write("\n");
                out.write("<!-- other languages might be available in the lang directory; please check\n");
                out.write("your distribution archive. -->\n");
                out.write("\n");
                out.write("<!-- helper script that uses the calendar -->\n");
                out.write("<script type=\"text/javascript\">\n");
                out.write("\n");
                out.write("var oldLink = null;\n");
                out.write("// code to change the active stylesheet\n");
                out.write("function setActiveStyleSheet(link, title) {\n");
                out.write("  var i, a, main;\n");
                out.write("  for(i=0; (a = document.getElementsByTagName(\"link\")[i]); i++) {\n");
                out.write("    if(a.getAttribute(\"rel\").indexOf(\"style\") != -1 && a.getAttribute(\"title\")) {\n");
                out.write("      a.disabled = true;\n");
                out.write("      if(a.getAttribute(\"title\") == title) a.disabled = false;\n");
                out.write("    }\n");
                out.write("  }\n");
                out.write("  if (oldLink) oldLink.style.fontWeight = 'normal';\n");
                out.write("  oldLink = link;\n");
                out.write("  link.style.fontWeight = 'bold';\n");
                out.write("  return false;\n");
                out.write("}\n");
                out.write("\n");
                out.write("// This function gets called when the end-user clicks on some date.\n");
                out.write("function selected(cal, date) {\n");
                out.write("  cal.sel.value = date; // just update the date in the input field.\n");
                out.write("  if (cal.dateClicked && (cal.sel.id == \"sel1\" || cal.sel.id == \"sel3\"))\n");
                out.write("    // if we add this call we close the calendar on single-click.\n");
                out.write("    // just to exemplify both cases, we are using this only for the 1st\n");
                out.write("    // and the 3rd field, while 2nd and 4th will still require double-click.\n");
                out.write("    cal.callCloseHandler();\n");
                out.write("}\n");
                out.write("\n");
                out.write("// And this gets called when the end-user clicks on the _selected_ date,\n");
                out.write("// or clicks on the \"Close\" button.  It just hides the calendar without\n");
                out.write("// destroying it.\n");
                out.write("function closeHandler(cal) {\n");
                out.write("  cal.hide();                        // hide the calendar\n");
                out.write("  if (_dynarch_popupCalendar.dateClicked) {\n");
                out.write("    if ( _dynarch_popupCalendar.sel.fireEvent ) { // IE\n");
                out.write("  \t  _dynarch_popupCalendar.sel.fireEvent(\"onChange\");\n");
                out.write("    }\n");
                out.write("    else { // Others: Firefox, Safari\n");
                out.write("      var ev = document.createEvent(\"HTMLEvents\");\n");
                out.write("      ev.initEvent(\"change\", true, true);\n");
                out.write("      _dynarch_popupCalendar.sel.dispatchEvent(ev);\n");
                out.write("    }\n");
                out.write("  }\n");
                out.write("//  cal.destroy();\n");
                out.write("  _dynarch_popupCalendar = null;\n");
                out.write("}\n");
                out.write("\n");
                out.write("// This function shows the calendar under the element having the given id.\n");
                out.write("// It takes care of catching \"mousedown\" signals on document and hiding the\n");
                out.write("// calendar if the click was outside.\n");
                out.write("function showCalendar(id, format, showsTime, showsOtherMonths) {\n");
                out.write("  var el = document.getElementById(id);\n");
                out.write("  if (_dynarch_popupCalendar != null) {\n");
                out.write("    // we already have some calendar created\n");
                out.write("    _dynarch_popupCalendar.hide();                 // so we hide it first.\n");
                out.write("  } else {\n");
                out.write("    // first-time call, create the calendar.\n");
                out.write("    var cal = new Calendar(1, null, selected, closeHandler);\n");
                out.write("    // uncomment the following line to hide the week numbers\n");
                out.write("    // cal.weekNumbers = false;\n");
                out.write("    if (typeof showsTime == \"string\") {\n");
                out.write("      cal.showsTime = true;\n");
                out.write("      cal.time24 = (showsTime == \"24\");\n");
                out.write("    }\n");
                out.write("    if (showsOtherMonths) {\n");
                out.write("      cal.showsOtherMonths = true;\n");
                out.write("    }\n");
                out.write("    _dynarch_popupCalendar = cal;                  // remember it in the global var\n");
                out.write("    cal.setRange(1900, 2070);        // min/max year allowed.\n");
                out.write("    cal.create();\n");
                out.write("  }\n");
                out.write("  _dynarch_popupCalendar.setDateFormat(format);    // set the specified date format\n");
                out.write("  _dynarch_popupCalendar.parseDate(el.value);      // try to parse the text in field\n");
                out.write("  _dynarch_popupCalendar.sel = el;                 // inform it what input field we use\n");
                out.write("\n");
                out.write("  // the reference element that we pass to showAtElement is the button that\n");
                out.write("  // triggers the calendar.  In this example we align the calendar bottom-right\n");
                out.write("  // to the button.\n");
                out.write("  _dynarch_popupCalendar.showAtElement(el.nextSibling, \"Br\");        // show the calendar\n");
                out.write("\n");
                out.write("  return false;\n");
                out.write("}\n");
                out.write("\n");
                out.write("var MINUTE = 60 * 1000;\n");
                out.write("var HOUR = 60 * MINUTE;\n");
                out.write("var DAY = 24 * HOUR;\n");
                out.write("var WEEK = 7 * DAY;\n");
                out.write("\n");
                out.write("// If this handler returns true then the \"date\" given as\n");
                out.write("// parameter will be disabled.  In this example we enable\n");
                out.write("// only days within a range of 10 days from the current\n");
                out.write("// date.\n");
                out.write("// You can use the functions date.getFullYear() -- returns the year\n");
                out.write("// as 4 digit number, date.getMonth() -- returns the month as 0..11,\n");
                out.write("// and date.getDate() -- returns the date of the month as 1..31, to\n");
                out.write("// make heavy calculations here.  However, beware that this function\n");
                out.write("// should be very fast, as it is called for each day in a month when\n");
                out.write("// the calendar is (re)constructed.\n");
                out.write("function isDisabled(date) {\n");
                out.write("  var today = new Date();\n");
                out.write("  return (Math.abs(date.getTime() - today.getTime()) / DAY) > 10;\n");
                out.write("}\n");
                out.write("\n");
                out.write("function flatSelected(cal, date) {\n");
                out.write("  var el = document.getElementById(\"preview\");\n");
                out.write("  el.innerHTML = date;\n");
                out.write("}\n");
                out.write("\n");
                out.write("function showFlatCalendar() {\n");
                out.write("  var parent = document.getElementById(\"display\");\n");
                out.write("\n");
                out.write("  // construct a calendar giving only the \"selected\" handler.\n");
                out.write("  var cal = new Calendar(0, null, flatSelected);\n");
                out.write("\n");
                out.write("  // hide week numbers\n");
                out.write("  cal.weekNumbers = false;\n");
                out.write("\n");
                out.write("  // We want some dates to be disabled; see function isDisabled above\n");
                out.write("  cal.setDisabledHandler(isDisabled);\n");
                out.write("  cal.setDateFormat(\"%A, %B %e\");\n");
                out.write("\n");
                out.write("  // this call must be the last as it might use data initialized above; if\n");
                out.write("  // we specify a parent, as opposite to the \"showCalendar\" function above,\n");
                out.write("  // then we create a flat calendar -- not popup.  Hidden, though, but...\n");
                out.write("  cal.create(parent);\n");
                out.write("\n");
                out.write("  // ... we can show it here.\n");
                out.write("  cal.show();\n");
                out.write("}\n");
                out.write("</script>\n");
                out.write('\n');
            }
            out.write('\n');
            out.write('\n');
            Users.setCurrent(request);
            Locales.setCurrent(request);
            boolean isPortlet = (request.getAttribute("xava.portlet.renderURL") != null);
            boolean messagesOnTop = !"false".equalsIgnoreCase(request.getParameter("messagesOnTop"));
            org.openxava.controller.ModuleManager manager = (org.openxava.controller.ModuleManager) context.get(request, "manager", "org.openxava.controller.ModuleManager");
            manager.setSession(session);
            manager.resetPersistence();
            org.openxava.tab.Tab t = (org.openxava.tab.Tab) context.get(request, "xava_tab");
            request.setAttribute("tab", t);
            out.write('\n');
            org.openxava.tab.Tab tab = null;
            synchronized (request) {
                tab = (org.openxava.tab.Tab) _jspx_page_context.getAttribute("tab", PageContext.REQUEST_SCOPE);
                if (tab == null) {
                    tab = new org.openxava.tab.Tab();
                    _jspx_page_context.setAttribute("tab", tab, PageContext.REQUEST_SCOPE);
                }
            }
            out.write('\n');
            if (manager.isListMode()) {
                tab.deselectVisualizedRows();
            }
            out.write('\n');
            out.write('\n');
            if (!"false".equals(request.getAttribute("xava.sendParametersToTab"))) {
                out.write('\n');
                org.apache.jasper.runtime.JspRuntimeLibrary.introspecthelper(_jspx_page_context.findAttribute("tab"), "selected", request.getParameter("selected"), request, "selected", false);
                out.write('\n');
                org.apache.jasper.runtime.JspRuntimeLibrary.introspecthelper(_jspx_page_context.findAttribute("tab"), "conditionComparators", request.getParameter("conditionComparators"), request, "conditionComparators", false);
                out.write('\n');
                org.apache.jasper.runtime.JspRuntimeLibrary.introspecthelper(_jspx_page_context.findAttribute("tab"), "conditionValues", request.getParameter("conditionValues"), request, "conditionValues", false);
                out.write('\n');
            }
            out.write('\n');
            out.write('\n');
            out.write('\n');
            manager.setApplicationName(request.getParameter("application"));
            boolean isNew = manager.setModuleName(request.getParameter("module"));
            org.openxava.view.View view = (org.openxava.view.View) context.get(request, "xava_view");
            if (isNew) {
                view.setModelName(manager.getModelName());
                view.setViewName(manager.getXavaViewName());
            }
            view.setRequest(request);
            view.setErrors(errors);
            view.setMessages(messages);
            tab.setRequest(request);
            if (manager.isListMode()) {
                tab.setModelName(manager.getModelName());
                if (tab.getTabName() == null) {
                    tab.setTabName(manager.getTabName());
                }
            }
            boolean hasProcessRequest = manager.hasProcessRequest(request);
            if (manager.isXavaView()) {
                if (hasProcessRequest) {
                    view.assignValuesToWebView();
                }
            }
            manager.initModule(request, errors, messages);
            if (hasProcessRequest) {
                manager.execute(request, errors, messages);
                if (manager.isListMode()) {
                    tab.setModelName(manager.getModelName());
                    if (tab.getTabName() == null) {
                        tab.setTabName(manager.getTabName());
                    }
                }
            }
            String forwardURI = (String) session.getAttribute("xava_forward");
            String forwardInNewWindow = (String) session.getAttribute("xava_forward_inNewWindow");
            if (!Is.emptyString(forwardURI)) {
                session.removeAttribute("xava_forward");
                session.removeAttribute("xava_forward_inNewWindow");
                if ("true".equals(forwardInNewWindow)) {
                    out.write("\n");
                    out.write("<script>\n");
                    out.write("window.open(\"");
                    out.print(request.getScheme());
                    out.write(':');
                    out.write('/');
                    out.write('/');
                    out.print(request.getServerName());
                    out.write(':');
                    out.print(request.getServerPort());
                    out.print(request.getContextPath());
                    out.print(forwardURI);
                    out.write("\");\n");
                    out.write("</script>\n");
                } else {
                    out.write("\n");
                    out.write("<script>\n");
                    out.write("location.href=\"");
                    out.print(request.getScheme());
                    out.write(':');
                    out.write('/');
                    out.write('/');
                    out.print(request.getServerName());
                    out.write(':');
                    out.print(request.getServerPort());
                    out.print(request.getContextPath());
                    out.print(forwardURI);
                    out.write("\";\n");
                    out.write("</script>\n");
                }
            }
            boolean returnToPreviousModule = org.openxava.actions.IChangeModuleAction.PREVIOUS_MODULE.equals(manager.getNextModule());
            if (returnToPreviousModule) {
                org.openxava.controller.ModuleManager parentManager = (org.openxava.controller.ModuleManager) context.get(request.getParameter("application"), request.getParameter("parent"), "manager", "org.openxava.controller.ModuleManager");
                parentManager.setNextModule(null);
                manager.setNextModule(null);
                out.write('\n');
                org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "module.jsp" + (("module.jsp").indexOf('?') > 0 ? '&' : '?') + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("application", request.getCharacterEncoding()) + "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode(String.valueOf(request.getParameter("application")), request.getCharacterEncoding()) + "&" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("module", request.getCharacterEncoding()) + "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode(String.valueOf(request.getParameter("parent")), request.getCharacterEncoding()), out, false);
                out.write('\n');
            } else if (!org.openxava.util.Is.emptyString(manager.getNextModule())) {
                out.write('\n');
                org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "module.jsp" + (("module.jsp").indexOf('?') > 0 ? '&' : '?') + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("application", request.getCharacterEncoding()) + "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode(String.valueOf(request.getParameter("application")), request.getCharacterEncoding()) + "&" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("module", request.getCharacterEncoding()) + "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode(String.valueOf(manager.getNextModule()), request.getCharacterEncoding()) + "&" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("parent", request.getCharacterEncoding()) + "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode(String.valueOf(request.getParameter("module")), request.getCharacterEncoding()), out, false);
                out.write('\n');
            } else {
                out.write("\n");
                out.write("\n");
                out.write("\n");
                out.write("<script>\n");
                out.write("function executeXavaAction(confirmMessage, takesLong, formu, action) {\n");
                out.write("\texecuteXavaAction(confirmMessage, takesLong, formu, action, null);\n");
                out.write("}\n");
                out.write("function executeXavaAction(confirmMessage, takesLong, formu, action, argv) {\t\n");
                out.write("\tif (confirmMessage != \"\" && !confirm(confirmMessage)) return;\n");
                out.write("\tif (takesLong) {\n");
                out.write("\t\tdocument.getElementById('processingLayer').style.display='block';\n");
                out.write("\t\tsetTimeout('document.images[\"processingImage\"].src = \"");
                out.print(request.getContextPath());
                out.write("/xava/images/processing.gif\"', 1);\n");
                out.write("\t}\n");
                out.write("\tformu.focus_forward.value = \"false\";\n");
                out.write("\tformu.xava_action.value=action;\t\n");
                out.write("\tformu.xava_action_argv.value=argv;\t\n");
                out.write("\tformu.submit();\n");
                out.write("}\n");
                out.write("function throwPropertyChanged(formu, property) {\t\n");
                out.write("\tformu.focus_forward.value = \"true\";\n");
                out.write("\tformu.focus_property.value=property;\t\n");
                out.write("\tformu.changed_property.value=property;\t\n");
                out.write("\tformu.submit();\t\n");
                out.write("}\n");
                String focusPropertyId = view.getFocusPropertyId();
                out.write("\n");
                out.write("function setFocus() {\n");
                out.write("\telement = document.");
                out.print(manager.getForm());
                out.write(".elements['");
                out.print(focusPropertyId);
                out.write("'];\n");
                out.write("\tif (element != null && typeof element.disabled != \"undefined\" && !element.disabled) {\n");
                out.write("\t\telement.focus();\n");
                out.write("\t\telement.select();\t\t\n");
                out.write("\t}\n");
                out.write("}\n");
                out.write("\n");
                out.write("function processKey(event) {\n");
                out.write("\tif (!event) event = window.event;\n");
                java.util.Iterator it = manager.getAllMetaActionsIterator();
                while (it.hasNext()) {
                    MetaAction action = (MetaAction) it.next();
                    if (!action.hasKeystroke()) continue;
                    KeyStroke key = KeyStroke.getKeyStroke(action.getKeystroke());
                    if (key == null) {
                        continue;
                    }
                    int keyCode = key.getKeyCode();
                    String ctrl = (key.getModifiers() & InputEvent.CTRL_DOWN_MASK) > 0 ? " && event.ctrlKey" : "";
                    String alt = (key.getModifiers() & InputEvent.ALT_DOWN_MASK) > 0 ? " && event.altKey" : "";
                    String shift = (key.getModifiers() & InputEvent.SHIFT_DOWN_MASK) > 0 ? " && event.shiftKey" : "";
                    out.write("\n");
                    out.write("\tif (event.keyCode == ");
                    out.print(keyCode);
                    out.write(' ');
                    out.print(ctrl);
                    out.write(' ');
                    out.print(alt);
                    out.write(' ');
                    out.print(shift);
                    out.write(") {\n");
                    out.write("\t\texecuteXavaAction('");
                    out.print(action.getConfirmMessage(request));
                    out.write('\'');
                    out.write(',');
                    out.write(' ');
                    out.print(action.isTakesLong());
                    out.write(", document.");
                    out.print(manager.getForm());
                    out.write(',');
                    out.write(' ');
                    out.write('\'');
                    out.print(action.getQualifiedName());
                    out.write("');\t\t\n");
                    out.write("\t\tevent.returnValue = false;\n");
                    out.write("\t\tevent.preventDefault();\n");
                    out.write("\t\treturn;\n");
                    out.write("\t}\n");
                }
                out.write("\n");
                out.write("\tif (event.keyCode >= 49 && event.keyCode <= 57 && event.ctrlKey && !event.altKey) {\n");
                out.write("\t\texecuteXavaAction(\"\", false, document.");
                out.print(manager.getForm());
                out.write(", \"Sections.change\", \"activeSection=\" + (event.keyCode - 49));\t\t\n");
                out.write("\t\tevent.returnValue = false;\n");
                out.write("\t\tevent.preventDefault();\n");
                out.write("\t\treturn;\n");
                out.write("\t}\t\n");
                out.write("}\n");
                out.write("\n");
                out.write("document.onkeydown = processKey;\n");
                out.write("</script>\n");
                out.write("\n");
                out.write("\n");
                out.write("\n");
                if (!isPortlet) {
                    out.write("\n");
                    out.write("<!DOCTYPE HTML PUBLIC \"-//w3c//dtd html 4.0 transitional//en\">\n");
                    out.write("<html>\n");
                    out.write("<head>\n");
                    out.write("<title>OpenXava - ");
                    out.print(manager.getModuleDescription());
                    out.write("</title>\n");
                    out.write("<link href=\"");
                    out.print(request.getContextPath());
                    out.write("/xava/style/default.css\" rel=\"stylesheet\" type=\"text/css\">\n");
                    out.write("</head>\n");
                    out.write("\n");
                    out.write("<body bgcolor=\"#ffffff\">\n");
                }
                out.write("\n");
                out.write("\n");
                out.write("<link href=\"");
                out.print(request.getContextPath());
                out.write("/xava/style/openxava.css\" rel=\"stylesheet\" type=\"text/css\">\n");
                out.write("\n");
                out.write("\n");
                out.write("<div id='processingLayer' style='position:absolute;top:100px;left:150px;display:none'>\n");
                out.write("<table cellspacing='0'>\n");
                out.write("   <tr class='");
                out.print(style.getProcessing());
                out.write("'>\n");
                out.write("       <td align='center' valign='middle' style='line-height:1.4;padding:25px 80px;border:2px solid #000'>\n");
                out.write("           ");
                out.print(XavaResources.getString(request, "processing"));
                out.write("<br/>\n");
                out.write("           <img src='");
                out.print(request.getContextPath());
                out.write("/xava/images/processing.gif' name='processingImage'/>\n");
                out.write("       </td>\n");
                out.write("   </tr>\n");
                out.write("</table>\n");
                out.write("</div>\n");
                out.write("\n");
                out.write("<div class=\"");
                out.print(style.getModule());
                out.write("\">\n");
                out.write("<form name='");
                out.print(manager.getForm());
                out.write("' \n");
                out.write("\tmethod='POST' ");
                out.print(manager.getEnctype());
                out.write(' ');
                out.write('\n');
                out.write('	');
                out.print(manager.getFormAction(request));
                out.write(">\n");
                out.write("\n");
                out.write("<INPUT type=\"hidden\" name=\"xava_page_id\" value=\"");
                out.print(manager.getPageId());
                out.write("\"/>\n");
                out.write("<INPUT type=\"hidden\" name=\"xava_action\" value=\"\"/>\n");
                out.write("<INPUT type=\"hidden\" name=\"xava_action_argv\" value=\"\"/>\n");
                out.write("<INPUT type=\"hidden\" name=\"xava_action_application\" value=\"");
                out.print(request.getParameter("application"));
                out.write("\"/>\n");
                out.write("<INPUT type=\"hidden\" name=\"xava_action_module\" value=\"");
                out.print(request.getParameter("module"));
                out.write("\"/>\n");
                out.write("<INPUT type=\"hidden\" name=\"changed_property\"/>\n");
                out.write("<INPUT type=\"hidden\" name=\"focus_property\"/>\n");
                out.write("<INPUT type=\"hidden\" name=\"focus_forward\"/>\n");
                out.write("<INPUT type=\"hidden\" name=\"focus_property_id\" value=\"");
                out.print(focusPropertyId);
                out.write("\"/>\n");
                out.write("\n");
                org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "languages.jsp", out, false);
                out.write("\n");
                out.write("\n");
                out.write("<table ");
                out.print(style.getModuleSpacing());
                out.write(">\n");
                out.write("  <tbody>\n");
                out.write("    <tr>\n");
                out.write("      <td class='");
                out.print(style.getButtonBar());
                out.write("'>\n");
                out.write("      \t");
                org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "buttonBar.jsp", out, false);
                out.write("\n");
                out.write("      </td>\n");
                out.write("    </tr>\n");
                out.write("    ");
                if (messagesOnTop && (errors.contains() || messages.contains())) {
                    out.write("\n");
                    out.write("    <tr>\n");
                    out.write("      <td class=");
                    out.print(style.getMessagesWrapper());
                    out.write(">\n");
                    out.write("\t\t");
                    org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "errors.jsp", out, false);
                    out.write("\n");
                    out.write("      </td>\n");
                    out.write("    </tr>    \n");
                    out.write("    <tr>\n");
                    out.write("      <td class=");
                    out.print(style.getMessagesWrapper());
                    out.write(">\n");
                    out.write("\t\t");
                    org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "messages.jsp", out, false);
                    out.write("\n");
                    out.write("      </td>\n");
                    out.write("    </tr>            \n");
                    out.write("    ");
                }
                out.write("\n");
                out.write("    <tr>      \t\t\n");
                out.write("\t\t<td ");
                out.print(manager.isListMode() ? "" : ("class='" + style.getDetail() + "'"));
                out.write(">\n");
                out.write("\t\t");
                org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, manager.getViewURL(), out, false);
                out.write("\t\t\n");
                out.write("\t\t</td>\n");
                out.write("    </tr>\n");
                out.write("    <tr>\n");
                out.write("      <td ");
                out.print(style.getBottomButtonsStyle());
                out.write(">\t\n");
                out.write("\t\t");
                org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "bottomButtons.jsp", out, false);
                out.write("\n");
                out.write("      </td>\n");
                out.write("    </tr>\n");
                out.write("    ");
                if (!messagesOnTop) {
                    out.write("\n");
                    out.write("    <tr>\n");
                    out.write("      <td class=");
                    out.print(style.getMessagesWrapper());
                    out.write(">\n");
                    out.write("\t\t");
                    org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "errors.jsp", out, false);
                    out.write("\n");
                    out.write("      </td>\n");
                    out.write("    </tr>    \n");
                    out.write("    <tr>\n");
                    out.write("      <td class=");
                    out.print(style.getMessagesWrapper());
                    out.write(">\n");
                    out.write("\t\t");
                    org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "messages.jsp", out, false);
                    out.write("\n");
                    out.write("      </td>\n");
                    out.write("    </tr>            \n");
                    out.write("    ");
                }
                out.write("\n");
                out.write("  </tbody>\n");
                out.write("</table>\n");
                out.write("</form>\n");
                out.write("</div>\n");
                out.write("\n");
                if (!isPortlet) {
                    out.write("\n");
                    out.write("</body></html>\n");
                }
                out.write('\n');
                out.write('\n');
            }
            out.write('\n');
            out.write('\n');
            manager.commit();
            out.write("\n");
            out.write("\n");
            out.write("<script>setTimeout ('setFocus()', 10);</script>\n");
        } catch (Throwable t) {
            if (!(t instanceof SkipPageException)) {
                out = _jspx_out;
                if (out != null && out.getBufferSize() != 0) out.clearBuffer();
                if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
            }
        } finally {
            if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
        }
    }
