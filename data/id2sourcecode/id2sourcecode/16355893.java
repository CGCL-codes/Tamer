    @Override
    public void init() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Throwable e) {
        }
        Container c = getContentPane();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        String[] labels = getAppletInfo().split("\n");
        for (int i = 0; i < labels.length; i++) {
            c.add(new JLabel((labels[i].length() == 0) ? " " : labels[i]));
        }
        new Worker() {

            public Object construct() {
                Object result;
                try {
                    if (getParameter("data") != null) {
                        NanoXMLDOMInput domi = new NanoXMLDOMInput(new UMLFigureFactory(), new StringReader(getParameter("data")));
                        result = domi.readObject(0);
                    } else if (getParameter("datafile") != null) {
                        InputStream in = null;
                        try {
                            URL url = new URL(getDocumentBase(), getParameter("datafile"));
                            in = url.openConnection().getInputStream();
                            NanoXMLDOMInput domi = new NanoXMLDOMInput(new UMLFigureFactory(), in);
                            result = domi.readObject(0);
                        } finally {
                            if (in != null) in.close();
                        }
                    } else {
                        result = null;
                    }
                } catch (Throwable t) {
                    result = t;
                }
                return result;
            }

            public void finished(Object value) {
                if (value instanceof Throwable) {
                    ((Throwable) value).printStackTrace();
                }
                Container c = getContentPane();
                c.setLayout(new BorderLayout());
                c.removeAll();
                c.add(uMLPanel = new UMLPanel());
                Object result = getValue();
                initComponents();
                if (result != null) {
                    if (result instanceof Drawing) {
                        Drawing drawing = (Drawing) result;
                        setDrawing(drawing);
                    } else if (result instanceof Throwable) {
                        getDrawing().add(new TextFigure(result.toString()));
                        ((Throwable) result).printStackTrace();
                    }
                }
                initDrawing(getDrawing());
                c.validate();
            }
        }.start();
    }
