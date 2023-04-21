    public void run() {
        try {
            view.execute(new Command() {

                public void run() throws Exception {
                    viewBuilder.buildView(viewContext);
                }
            });
        } catch (Throwable e) {
            LOG.error("error loading file", e);
            final JEditorPane editorPane = new JEditorPane();
            final JScrollPane scrollPane = new JScrollPane(editorPane);
            scrollPane.setViewportBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            editorPane.setEditorKit(new HTMLEditorKit());
            StringWriter writer = new StringWriter();
            writer.write("<html><h2>Error reading file!</h2><h3>" + e.getMessage() + "</h3><pre>");
            e.printStackTrace(new PrintWriter(writer));
            writer.write("</pre></html>");
            editorPane.setText(writer.toString());
            scrollPane.setBackground(Color.WHITE);
        } finally {
        }
    }
