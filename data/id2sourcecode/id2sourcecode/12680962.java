    public void doHelp() {
        if (helpURLString != null) {
            displayURL(helpURLString);
        } else {
            try {
                InputStream in = getClass().getResourceAsStream("/help/application.help");
                if (in == null) return;
                Reader reader = new InputStreamReader(in);
                StringWriter writer = new StringWriter();
                int c;
                while ((c = reader.read()) != -1) writer.write(c);
                reader.close();
                writer.close();
                JFrame frame = new HTMLViewer(getNameString() + " Help", writer.toString());
                frame.setVisible(true);
            } catch (IOException ignore) {
            }
        }
    }
