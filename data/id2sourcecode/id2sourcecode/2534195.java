    public static void testReportXlsTrasfertePersonale(int year, int month) {
        List<Calendar> l = buildCalendarStartEndMese(year, month);
        Calendar start = l.get(0);
        Calendar end = l.get(1);
        List<Personale> personas = getHelper().searchPersonale(null);
        for (Personale pers : personas) {
            CollaborationFilter taskFilter = new CollaborationFilter();
            taskFilter.setData_inizio(new Timestamp(start.getTimeInMillis()));
            taskFilter.setData_fine(new Timestamp(end.getTimeInMillis()));
            taskFilter.setTipo(Constant.TIPO_TASK_TRASFERTA);
            taskFilter.setA(pers);
            taskFilter.addOrder("id");
            List<Task> taskList = getHelper().searchTask(taskFilter);
            String reportTitle = "Trasferte di " + pers.getCognome() + " " + pers.getNome() + " " + titleFrmt.format(start.getTime());
            String reportName = reportTitle.replace(' ', '_') + ".xls";
            FileOutputStream f = null;
            if (taskList.size() > 0) {
                try {
                    String folderPersonale = pers.getId() + "_" + pers.getCognome().toLowerCase() + "_" + pers.getNome().toLowerCase();
                    String folderMese = titleFrmt.format(start.getTime()).replace(' ', '_');
                    String pathPersonale = FileSystem.getUnionPath(new String[] { reportPath, folderPersonale, folderMese });
                    if (!FileSystem.exist(pathPersonale)) FileSystem.mkDir(pathPersonale);
                    f = new FileOutputStream(FileSystem.getUnionPath(pathPersonale, reportName));
                    HSSFWorkbook wb = new HSSFWorkbook();
                    if (!pers.getRuoli().getNome_ruolo().equalsIgnoreCase(Constant.RUOLO_ADMIN)) {
                        ReportManager.getInstance().createFileXslTrasferta(taskList, pers, wb);
                    }
                    wb.write(f);
                    List<String> filesName = new ArrayList<String>();
                    filesName.add(FileSystem.getUnionPath(new String[] { folderPersonale, folderMese, reportName }));
                    for (Task task : taskList) {
                        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                        String title = format.format(task.getData_inizio());
                        Document doc = new Document(PageSize.A4);
                        String filename = "autorizzazione_missione_" + title + ".pdf";
                        try {
                            String pdfPath = FileSystem.getUnionPath(pathPersonale, filename);
                            PdfWriter.getInstance(doc, new FileOutputStream(pdfPath));
                            doc.open();
                            String pathLogo = FileSystem.getUnionPath(new String[] { reportPath, "images" });
                            if (!pers.getRuoli().getNome_ruolo().equalsIgnoreCase(Constant.RUOLO_ADMIN)) {
                                ReportManager.getInstance().createFilePdfTrasferta(task, pers, doc, pathLogo);
                            }
                            filesName.add(FileSystem.getUnionPath(new String[] { folderPersonale, folderMese, filename }));
                            doc.close();
                        } catch (Exception e) {
                            Configure.log(e);
                        }
                    }
                } catch (Exception e) {
                    Configure.log(e);
                } finally {
                    if (f != null) try {
                        f.close();
                    } catch (IOException e) {
                        Configure.log(e);
                    }
                }
            }
        }
    }
