    private void exportData(Data data, Viewer viewer) throws IOException {
        String path = Utilities.getApplicationDataPath();
        File currentDirectory = new File(path);
        JFileChooser fileChooser = new JFileChooser();
        ExportFileFilter ff = new ExportFileFilter(ExportFileFilter.MODE_AMATERAS_UML);
        fileChooser.addChoosableFileFilter(ff);
        fileChooser.setCurrentDirectory(currentDirectory);
        int value = fileChooser.showOpenDialog(null);
        if (value != JFileChooser.APPROVE_OPTION) {
            return;
        }
        String filename = fileChooser.getSelectedFile().getPath();
        if (!filename.endsWith(".sqd")) {
            filename += ".sqd";
        }
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
        SequenceModelBuilder builder = new SequenceModelBuilder();
        Session session = data.getSession();
        Where where = new Where();
        where.createBaseClause(data);
        String hsql = "from Trace trace " + where.toWhere() + " order by trace.id";
        Query query = session.createQuery(hsql);
        where.usePlaceholders(query);
        List<Trace> list = query.list();
        Iterator<Trace> traceIterator = list.iterator();
        Map<String, InstanceModel> classes = new HashMap<String, InstanceModel>();
        RGB whiteColour = new RGB(255, 255, 255);
        RGB currentColour = null;
        MessageModel msg;
        InstanceModel atStart = builder.createActor(" begin ");
        builder.init(atStart);
        LinkedList<String> stack = new LinkedList<String>();
        stack.add("");
        while (traceIterator.hasNext()) {
            Trace trace = traceIterator.next();
            String signature = trace.getSignature(data);
            String clazz = trace.getClassNameAmaterasUML(signature);
            String method = trace.getMethodName(signature);
            currentColour = getRGB(trace);
            InstanceModel instance = classes.get(clazz);
            if (instance == null) {
                instance = builder.createInstance(clazz);
                classes.put(clazz, instance);
            }
            if (trace.isReturn()) {
                builder.endMessage();
                stack.removeLast();
            } else {
                if (clazz.equals(stack.getLast())) {
                    msg = builder.createSelfCallMessage(method);
                } else {
                    msg = builder.createMessage(method, instance);
                }
                msg.setForegroundColor(currentColour);
                msg.getTarget().setForegroundColor(currentColour);
                if (trace.getColourMarker() == null || trace.getColourMarker().intValue() == 0) {
                    msg.getTarget().setBackgroundColor(whiteColour);
                } else {
                    msg.getTarget().setBackgroundColor(currentColour);
                }
                stack.add(clazz);
            }
        }
        session.clear();
        fileWriter.write(builder.toXML());
        fileWriter.close();
    }
