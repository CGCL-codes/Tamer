    protected void saveAllAnswersForDate(HttpSession sess, HttpServletRequest request, HttpServletResponse response, String role) throws ServletException, IOException {
        String date;
        date = request.getParameter("answerDate");
        if (date != null) {
            try {
                long l = new SimpleDateFormat("dd/MM/yyy").parse(date).getTime();
                java.sql.Date adate = new java.sql.Date(l);
                MenteeAnswerActions maa = new MenteeAnswerActions();
                List<QueryMenteeAnswerByDate> qma = maa.getDetailedInfoForDate(adate);
                sess.setAttribute("MenteeAnswers", qma);
                sess.setAttribute("AnswerDate", new SimpleDateFormat("dd/MM/yyyy").format(adate));
                if (role.equalsIgnoreCase("Researcher") || role.equalsIgnoreCase("Superuser")) {
                    File f = File.createTempFile("feedback_date-" + new SimpleDateFormat("dd-MM-yyyy").format(adate) + "_", ".xls");
                    FileOutputStream out = new FileOutputStream(f);
                    Workbook wb = new HSSFWorkbook();
                    CreationHelper createHelper = wb.getCreationHelper();
                    Sheet s = wb.createSheet();
                    Row r = null;
                    Cell c = null;
                    wb.setSheetName(0, "Mentee Feedback");
                    r = s.createRow(0);
                    CellStyle dateCellStyle = wb.createCellStyle();
                    CellStyle headerCellStyle = wb.createCellStyle();
                    Font font = wb.createFont();
                    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                    headerCellStyle.setFont(font);
                    dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
                    for (int i = 0; i < QueryMenteeAnswerByDate.getLabels().length; i++) {
                        c = r.createCell(i);
                        c.setCellValue(QueryMenteeAnswerByDate.getLabels()[i]);
                        c.setCellType(Cell.CELL_TYPE_STRING);
                        c.setCellStyle(headerCellStyle);
                    }
                    int rownum;
                    for (rownum = (short) 1; rownum <= qma.size(); rownum++) {
                        r = s.createRow(rownum);
                        Object[] obj = qma.get(rownum - 1).getObjectData();
                        for (short cellnum = (short) 0; cellnum < obj.length; cellnum++) {
                            c = r.createCell(cellnum);
                            Object sel = obj[cellnum];
                            if (sel instanceof Integer) {
                                c.setCellValue((Integer) sel);
                            } else if (sel instanceof String) {
                                c.setCellValue((String) sel);
                            } else if (sel instanceof Date) {
                                c.setCellValue((Date) sel);
                                c.setCellStyle(dateCellStyle);
                            } else {
                                c.setCellValue(sel.toString());
                            }
                        }
                    }
                    for (int i = 0; i < QueryMenteeAnswerByDate.getLabels().length; i++) {
                        s.autoSizeColumn(i);
                    }
                    wb.write(out);
                    out.flush();
                    out.close();
                    String contentType = getServletContext().getMimeType(f.getName());
                    if (contentType == null) {
                        contentType = "application/octet-stream";
                    }
                    response.reset();
                    response.setBufferSize(DEFAULT_BUFFER_SIZE);
                    response.setContentType(contentType);
                    response.setHeader("Content-Length", String.valueOf(f.length()));
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + f.getName() + "\"");
                    BufferedInputStream input = null;
                    BufferedOutputStream output = null;
                    try {
                        input = new BufferedInputStream(new FileInputStream(f), DEFAULT_BUFFER_SIZE);
                        output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);
                        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                        int length;
                        while ((length = input.read(buffer)) > 0) {
                            output.write(buffer, 0, length);
                        }
                    } finally {
                        close(output);
                        close(input);
                    }
                    if (!f.delete()) {
                        System.err.println("Oh no!");
                    }
                    return;
                }
            } catch (ParseException pe) {
            }
        }
        response.sendRedirect(response.encodeRedirectURL("main.jsp"));
        return;
    }
