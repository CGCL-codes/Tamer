    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        try {
            HttpSession session = req.getSession();
            session.removeAttribute("msg");
            String author = (String) session.getAttribute("logon.isDone");
            String nextpage = req.getParameter("nextpage");
            if (author == null) {
                throw new RedirectException("Sorry. Please login again.", "Failure.jsp?page=writeError");
            }
            res.setContentType("text/html");
            Iteration iter = ms.findIteration(Long.parseLong(req.getParameter("iter")));
            WikiPage page = ms.findWikiPage(req.getParameter("wikipage"));
            List<WikiPage> notPos = null;
            String iterationName = "";
            if (req.getParameter("addDate") != null) {
                iterationName = req.getParameter("name") + " " + req.getParameter("hiddenDate");
            } else {
                iterationName = req.getParameter("name");
            }
            if (iterationName.trim().equalsIgnoreCase("")) {
                throw new RedirectException("You have to define a name for the story card", req.getParameter("page"));
            }
            String error = "";
            if (!(error = WikipageService.checkInput(iterationName)).equalsIgnoreCase("ok")) {
                throw new RedirectException(error + " in your iteration name", req.getParameter("page"));
            }
            if (!iter.getName().equalsIgnoreCase(iterationName)) {
                nextpage = "Wiki.jsp?page=" + iter.getNamespace() + "." + iterationName + "&type=iteration&id=" + req.getParameter("iter") + "&projid=" + req.getParameter("projid");
            }
            iter.setName(iterationName.trim());
            GregorianCalendar startTime = new GregorianCalendar(Integer.parseInt(req.getParameter("startYear")), Integer.parseInt(req.getParameter("startMonth")) - 1, Integer.parseInt(req.getParameter("startDayOfMonth")));
            GregorianCalendar endTime = new GregorianCalendar(Integer.parseInt(req.getParameter("endYear")), Integer.parseInt(req.getParameter("endMonth")) - 1, Integer.parseInt(req.getParameter("endDayOfMonth")));
            if (startTime.after(endTime)) {
                throw new RedirectException("Sorry! Your start date is after your end date Please choose page type and access rights new!", req.getParameter("page"));
            }
            iter.setStartDate(new Timestamp(startTime.getTimeInMillis()));
            iter.setEndDate(new Timestamp(endTime.getTimeInMillis()));
            if (!(req.getParameter("availableEffort").equalsIgnoreCase(""))) {
                if (!(req.getParameter("availableEffort").matches("(([0-9]*\\.[0-9]*)|([0-9]*))"))) {
                    throw new RedirectException("Please use only correct numbers for available effort!! Please choose page type and access rights new!", req.getParameter("page"));
                }
                iter.setAvailableEffort(Float.parseFloat(req.getParameter("availableEffort")));
            } else {
                throw new RedirectException("Please define an available effort Please choose page type and access rights new!", req.getParameter("page"));
            }
            iter.setReadPermission(Integer.parseInt(req.getParameter("readPermission")));
            iter.setWritePermission(Integer.parseInt(req.getParameter("writePermission")));
            iter.setDescription(req.getParameter("description"));
            if (req.getParameter("fitpage") != null) {
                iter.setFitTest(true);
            } else {
                iter.setFitTest(false);
            }
            if (!ms.updateIteration(iter)) {
                throw new RedirectException("Database Error! Please choose page type and access rights new!", req.getParameter("page"));
            }
            page.setId(0);
            page.setMainContent(req.getParameter("wikitext"));
            page.setRightContent(req.getParameter("textRight"));
            page.setPageLayout(Integer.parseInt(req.getParameter("layout")));
            page.setReadPermission(Integer.parseInt(req.getParameter("readPermission")));
            page.setWritePermission(Integer.parseInt(req.getParameter("writePermission")));
            page.setModified(new Timestamp(System.currentTimeMillis()));
            page.setLastAccessed(new Timestamp(System.currentTimeMillis()));
            page.setAuthor(ms.findTeamMemberByEmail((String) session.getAttribute("eMail")));
            page.setName(iterationName.trim());
            if (req.getParameter("fitpage") != null) {
                page.setFitTest(true);
            } else {
                page.setFitTest(false);
            }
            if ((page = ms.addWikiPage(page)) == null) {
                throw new RedirectException("Database Error! Please choose page type and access rights new!", req.getParameter("page"));
            } else {
                if (req.getParameter("subscribed") != null) {
                    ms.subscribeToPage(page.getFullQualifiedName(), author, true);
                }
            }
            if (req.getParameter("allwikis") != null) {
                notPos = ms.changeWikiPagePermission(Integer.parseInt(req.getParameter("readPermission")), Integer.parseInt(req.getParameter("writePermission")), iter.getFullQualifiedName(), (String) req.getSession().getAttribute("logon.isDone"), true);
            } else {
                notPos = ms.changeWikiPagePermission(Integer.parseInt(req.getParameter("readPermission")), Integer.parseInt(req.getParameter("writePermission")), iter.getFullQualifiedName(), (String) req.getSession().getAttribute("logon.isDone"), false);
            }
            if (notPos == null) {
                throw new RedirectException("Database error.", req.getParameter("page"));
            } else if (!notPos.isEmpty()) {
                String outputPage = "";
                String resPerson = "";
                WikiPage wiki = null;
                StringTokenizer st = null;
                List<Project> prjs = null;
                for (int i = 0; i < notPos.size(); i++) {
                    wiki = notPos.get(i);
                    outputPage += "|<a href=\"Wiki.jsp?page=" + wiki.getFullQualifiedName() + "\">" + wiki.getFullQualifiedName() + "</a>";
                    if (wiki.getWritePermission() == WikipageService.PERM_OWNER) {
                        resPerson += "|<a href=\"EditTeamMember.jsp?page=viewUser&id=" + wiki.getAuthor().getId() + "\">" + wiki.getAuthor().getFirstName() + " " + wiki.getAuthor().getLastName() + "</a>";
                    } else if (wiki.getWritePermission() == WikipageService.PERM_MANAGER || wiki.getWritePermission() == WikipageService.PERM_PROJECT) {
                        st = new StringTokenizer(wiki.getFullQualifiedName(), ".");
                        if (st.hasMoreTokens()) {
                            String prjName = st.nextToken();
                            prjs = ms.findAllVersionsOfProjectByName(prjName);
                        }
                        for (int j = 0; j < prjs.size(); j++) {
                            if (prjs.get(j).getVersion() == 1) {
                                if (wiki.getWritePermission() == WikipageService.PERM_MANAGER) {
                                    resPerson += "|<a href=\"EditTeamMember.jsp?page=viewUser&id=" + prjs.get(j).getManager().getId() + "\">" + prjs.get(j).getManager().getFirstName() + " " + prjs.get(j).getManager().getLastName() + "</a>";
                                } else {
                                    resPerson += "|<a href=\"Project.jsp?page=viewTeamMember&lastpage=allProjectsTable&projid=" + prjs.get(j).getId() + "\">TeamMember</a>";
                                }
                            }
                        }
                    } else if (wiki.getWritePermission() == WikipageService.PERM_LOGGEDIN) {
                        resPerson += "|You have to log in.";
                    } else {
                        resPerson += "|Error";
                    }
                }
                req.getSession().setAttribute("outputPage", outputPage);
                req.getSession().setAttribute("resPerson", resPerson);
                res.sendRedirect("Failure.jsp?page=accessList");
            } else {
                res.sendRedirect(nextpage);
            }
        } catch (RedirectException e) {
            req.getSession().setAttribute("msg", e.getMessage());
            req.getSession().setAttribute("startDayOfMonth", req.getParameter("startDayOfMonth"));
            req.getSession().setAttribute("startMonth", req.getParameter("startMonth"));
            req.getSession().setAttribute("startYear", req.getParameter("startYear"));
            req.getSession().setAttribute("endDayOfMonth", req.getParameter("endDayOfMonth"));
            req.getSession().setAttribute("endMonth", req.getParameter("endMonth"));
            req.getSession().setAttribute("endYear", req.getParameter("endYear"));
            req.getSession().setAttribute("availableEffort", req.getParameter("availableEffort"));
            req.getSession().setAttribute("description", req.getParameter("description"));
            req.getSession().setAttribute("wikitext", req.getParameter("wikitext"));
            req.getSession().setAttribute("textRight", req.getParameter("textRight"));
            req.getSession().setAttribute("name", req.getParameter("name"));
            res.sendRedirect(e.getRedirect());
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
    }
