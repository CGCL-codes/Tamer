    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        try {
            HttpSession session = req.getSession();
            session.removeAttribute("msg");
            String author = (String) session.getAttribute("logon.isDone");
            if (author == null) {
                throw new RedirectException("Sorry. Please login again.", "Failure.jsp?page=writeError");
            }
            List<WikiPage> notPos = null;
            res.setContentType("text/html");
            StoryCard card = ms.findStoryCard(Long.parseLong(req.getParameter("card")));
            WikiPage page = ms.findWikiPage(req.getParameter("wikipage"));
            card.setStoryCardType(Integer.parseInt(req.getParameter("activityType")));
            card.setReadPermission(Integer.parseInt(req.getParameter("readPermission")));
            card.setWritePermission(Integer.parseInt(req.getParameter("writePermission")));
            card.setDescription(req.getParameter("description"));
            if (req.getParameter("fitpage") != null) {
                card.setFitTest(true);
            } else {
                card.setFitTest(false);
            }
            if (!ms.updateStoryCard(card)) {
                throw new RedirectException("Database Error!", req.getParameter("page"));
            }
            page.setId(0);
            page.setMainContent(req.getParameter("wikitext"));
            page.setModified(new Timestamp(System.currentTimeMillis()));
            page.setLastAccessed(new Timestamp(System.currentTimeMillis()));
            page.setRightContent(req.getParameter("textRight"));
            page.setPageLayout(Integer.parseInt(req.getParameter("layout")));
            page.setReadPermission(Integer.parseInt(req.getParameter("readPermission")));
            page.setWritePermission(Integer.parseInt(req.getParameter("writePermission")));
            page.setAuthor(ms.findTeamMemberByEmail((String) session.getAttribute("eMail")));
            if (req.getParameter("fitpage") != null) {
                page.setFitTest(true);
            } else {
                page.setFitTest(false);
            }
            if ((page = ms.addWikiPage(page)) == null) {
                throw new RedirectException("Database Error!", req.getParameter("page"));
            } else {
                if (req.getParameter("subscribed") != null) {
                    ms.subscribeToPage(page.getFullQualifiedName(), author, true);
                }
            }
            if (req.getParameter("allwikis") != null) {
                notPos = ms.changeWikiPagePermission(Integer.parseInt(req.getParameter("readPermission")), Integer.parseInt(req.getParameter("writePermission")), card.getFullQualifiedName(), (String) req.getSession().getAttribute("logon.isDone"), true);
            } else {
                notPos = ms.changeWikiPagePermission(Integer.parseInt(req.getParameter("readPermission")), Integer.parseInt(req.getParameter("writePermission")), card.getFullQualifiedName(), (String) req.getSession().getAttribute("logon.isDone"), false);
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
                res.sendRedirect(req.getParameter("nextpage"));
            }
        } catch (RedirectException e) {
            req.getSession().setAttribute("msg", e.getMessage());
            req.getSession().setAttribute("description", req.getParameter("description"));
            req.getSession().setAttribute("description", req.getParameter("wikitext"));
            req.getSession().setAttribute("textRight", req.getParameter("textRight"));
            res.sendRedirect(e.getRedirect());
        }
    }
