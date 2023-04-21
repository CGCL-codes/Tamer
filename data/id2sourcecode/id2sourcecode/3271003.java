    protected void showAllAnswersForDate(HttpSession sess, HttpServletRequest request, HttpServletResponse response, String role) throws ServletException, IOException {
        RequestDispatcher rd;
        String date;
        date = request.getParameter("answerDate");
        if (date != null) {
            try {
                long l = new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime();
                java.sql.Date adate = new java.sql.Date(l);
                MenteeAnswerActions maa = new MenteeAnswerActions();
                List<QueryMenteeAnswerByDate> qma = maa.getDetailedInfoForDate(adate);
                sess.setAttribute("MenteeAnswers", qma);
                sess.setAttribute("AnswerDate", new SimpleDateFormat("dd/MM/yyyy").format(adate));
                if (role.equalsIgnoreCase("Researcher") || role.equalsIgnoreCase("Superuser")) {
                    rd = request.getRequestDispatcher("/WEB-INF/Researcher/MenteeAnswerDate.jsp");
                    rd.forward(request, response);
                    return;
                }
            } catch (ParseException pe) {
            }
        }
        response.sendRedirect(response.encodeRedirectURL("main.jsp"));
        return;
    }
