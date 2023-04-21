    public void export(Data data, Viewer viewer) {
        StringBuffer result = new StringBuffer();
        String path = Utilities.getApplicationDataPath();
        File currentDirectory = new File(path);
        JFileChooser fileChooser = new JFileChooser();
        ExportFileFilter ff = new ExportFileFilter(ExportFileFilter.MODE_HTML);
        fileChooser.addChoosableFileFilter(ff);
        fileChooser.setCurrentDirectory(currentDirectory);
        int value = fileChooser.showOpenDialog(null);
        try {
            if (value == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().getPath();
                System.out.println("Selected file: " + filename);
                BufferedWriter fileWriter;
                File file = new File(filename);
                if (file.exists()) {
                    int answer = JOptionPane.showConfirmDialog(viewer, "File already exists. Rewrite ?\n" + filename, "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (answer != JOptionPane.OK_OPTION) {
                        return;
                    }
                }
                file.delete();
                file.createNewFile();
                fileWriter = new BufferedWriter(new FileWriter(file, true));
                Session session = data.getSession();
                Where where = new Where();
                where.createBaseClause(data);
                String hsql = "from Trace trace " + where.toWhere() + " order by trace.id";
                Query query = session.createQuery(hsql);
                where.usePlaceholders(query);
                List<Trace> list = query.list();
                Iterator<Trace> traceIterator = list.iterator();
                result.append("<html>");
                result.append("<head></head>");
                result.append("<body style=\"font-family:monospace;font-size:14px;background:#ffffff;\">");
                result.append("<pre>");
                while (traceIterator.hasNext()) {
                    Trace trace = traceIterator.next();
                    result.append(trace.getHTML(data)).append("\n");
                }
                result.append("</pre></body></html>");
                session.clear();
                fileWriter.write(result.toString());
                fileWriter.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
