    @Override
    public void init() {
        if (getParameter("language") != null) {
            try {
                Locale.setDefault(new Locale(getParameter("language"), ""));
            } catch (final RuntimeException e) {
            }
        }
        Global.initBundle("rene/zirkel/docs/ZirkelProperties");
        Count.resetAll();
        CC = C = Global.Background;
        initLightColors();
        initObjectKeys();
        final Dimension dscreen = getToolkit().getScreenSize();
        F = new Frame();
        F.setSize(dscreen);
        if (getParameter("oldicons") == null) {
            Global.setParameter("iconpath", "/eric/icons/palette/");
            Global.setParameter("iconsize", getParameter("smallicons") == null ? 32 : 24);
            Global.setParameter("icontype", "png");
        } else {
            Global.setParameter("iconpath", "//eric/icons/palette/");
            Global.setParameter("iconsize", 20);
        }
        String color = getParameter("color");
        if (color != null) {
            final StringParser p = new StringParser(getParameter("color"));
            p.replace(',', ' ');
            int red, green, blue;
            red = p.parseint();
            green = p.parseint();
            blue = p.parseint();
            C = new Color(red, green, blue);
            Global.Background = C;
            Global.ControlBackground = C;
            CC = C;
        }
        color = getParameter("colorbackground");
        if (color != null) {
            Global.setParameter("colorbackground", color);
        } else {
            Global.removeParameter("colorbackground");
        }
        color = getParameter("colorselect");
        if (color != null) {
            Global.setParameter("colorselect", color);
        } else {
            Global.removeParameter("colorselect");
        }
        ZirkelFrame.SelectColor = Global.getParameter("colorselect", Color.red);
        color = getParameter("colortarget");
        if (color != null) {
            Global.setParameter("colortarget", color);
        } else {
            Global.removeParameter("colortarget");
        }
        ZirkelFrame.TargetColor = Global.getParameter("colorselect", Color.pink);
        ZirkelFrame.initLightColors(Color.white);
        final String font = getParameter("font");
        if (font != null) {
            if (font.indexOf("bold") >= 0) {
                Global.setParameter("font.bold", true);
            }
            if (font.indexOf("large") >= 0) {
                Global.setParameter("font.large", true);
            }
        }
        if (getParameter("demo") != null) {
            initDemo();
            return;
        }
        String style = getParameter("style");
        if (style == null) {
            if ((getParameter("tools") != null) || (getParameter("options") != null)) {
                style = "icons";
            } else {
                style = "plain";
            }
        }
        final boolean simple = (style.equals("plain") || style.equals("3D") || style.equals("breaks"));
        edit = !simple;
        final boolean status = (style.equals("full") || style.equals("status"));
        final boolean icons = (style.equals("full") || style.equals("nonvisual") || style.equals("icons"));
        final boolean breaks = (style.equals("breaks"));
        if (getParameter("edit") != null) {
            edit = true;
        }
        for (int i = 0; i < ZirkelFrame.Colors.length; i++) {
            color = getParameter("color" + i);
            if (color != null) {
                Global.setParameter("color" + i, color);
            } else {
                Global.removeParameter("color" + i);
            }
        }
        ZirkelFrame.initLightColors(Color.white);
        getContentPane().setLayout(new BorderLayout());
        Tools = getParameter("tools");
        if (Tools == null || Tools.equals("defaults")) {
            Tools = DefaultIcons;
        }
        Options = getParameter("options");
        if (Options == null || Options.equals("defaults")) {
            Options = DefaultOptions;
        }
        Global.setParameter("macrobar", Options.indexOf("macrobar") >= 0);
        if (icons) {
            IB = new IconBar(F);
            IB.setBackground(CC);
            IB.setIconBarListener(this);
            StringTokenizer t = new StringTokenizer(Tools);
            int count = 0;
            while (t.hasMoreTokens()) {
                t.nextToken();
                count++;
            }
            final String a[] = new String[count];
            t = new StringTokenizer(Tools);
            count = 0;
            while (t.hasMoreTokens()) {
                a[count++] = t.nextToken();
            }
            if (count > 0) {
                FirstConstructor = a[0];
            }
            IB.addToggleGroupLeft(a);
            if (Options.indexOf("twolines") >= 0 || Options.indexOf("defaults") >= 0) {
                IA = new IconBar(F);
                IA.setBackground(CC);
                IA.setIconBarListener(this);
                setIA(IA, Options);
                final JPanel north = new JPanel();
                north.setBackground(CC);
                north.setLayout(new GridLayout(0, 1));
                north.add(IA);
                north.add(IB);
                if (IM != null) {
                    north.add(IM);
                }
                getContentPane().add("North", new Panel3D(north, CC));
            } else {
                IA = IB;
                setIA(IB, Options);
                if (IM != null) {
                    final JPanel north = new JPanel();
                    north.setBackground(CC);
                    north.setLayout(new GridLayout(0, 1));
                    north.add(IA);
                    if (IM != null) {
                        north.add(IM);
                    }
                    getContentPane().add("North", new Panel3D(north, CC));
                } else {
                    getContentPane().add("North", new Panel3D(IB, CC));
                }
            }
        } else {
            IA = IB = null;
        }
        rene.zirkel.Zirkel.IsApplet = true;
        Global.setParameter("options.choice", true);
        Global.setParameter("options.indicate", true);
        Global.setParameter("options.indicate.simple", false);
        Global.setParameter("options.intersection", false);
        Global.setParameter("options.pointon", false);
        eric.JMacrosTools.initObjectsProperties();
        if (getParameter("selectionsize") != null) {
            try {
                final double x = new Double(getParameter("selectionsize")).doubleValue();
                Global.setParameter("selectionsize", x);
            } catch (final Exception e) {
            }
        }
        ZC = new ZirkelCanvas(!edit, !breaks, !breaks);
        ZC.addMouseListener(ZC);
        ZC.addMouseMotionListener(ZC);
        ZC.setBackground(Global.getParameter("colorbackground", C));
        ZC.setFrame(F);
        ZC.setZirkelCanvasListener(this);
        if (getParameter("showhidden") != null) {
            ZC.setShowHidden(true);
        }
        if (style.equals("plain")) {
            getContentPane().add("Center", ZC);
        } else {
            getContentPane().add("Center", new Panel3D(ZC, ZC.getBackground()));
        }
        ZC.addStatusListener(this);
        ZC.addKeyListener(this);
        setShowNames(false);
        if (status) {
            Status = new Label("");
            Status.setBackground(CC);
            getContentPane().add("South", new Panel3D(Status, Status.getBackground()));
        } else if (style.equals("nonvisual")) {
            Input = new HistoryTextField(this, "Input");
            ZC.setTextField(Input);
            ZC.Visual = false;
            setShowNames(true);
            getContentPane().add("South", new Panel3D(Input));
        }
        try {
            Global.setParameter("digits.edit", Integer.parseInt(getParameter("editdigits")));
        } catch (final Exception e) {
        }
        try {
            Global.setParameter("digits.lengths", Integer.parseInt(getParameter("displaydigits")));
        } catch (final Exception e) {
        }
        try {
            Global.setParameter("digits.angles", Integer.parseInt(getParameter("angledigits")));
        } catch (final Exception e) {
        }
        setOption("movename");
        setOption("movefixname");
        ZC.updateDigits();
        setOption("nopopupmenu");
        setOption("nomousezoom");
        try {
            Global.setParameter("minpointsize", new Double(getParameter("minpointsize")).doubleValue());
        } catch (final Exception e) {
        }
        try {
            Global.setParameter("minlinesize", new Double(getParameter("minlinesize")).doubleValue());
        } catch (final Exception e) {
        }
        try {
            Global.setParameter("minfontsize", new Double(getParameter("minfontsize")).doubleValue());
        } catch (final Exception e) {
        }
        try {
            Global.setParameter("arrowsize", new Double(getParameter("arrowsize")).doubleValue());
        } catch (final Exception e) {
        }
        try {
            final String grid = getParameter("grid");
            ZC.ShowGrid = !grid.equals("none");
            Global.setParameter("grid.fine", grid.equals("coordinates"));
            if (getParameter("snap").equals("left")) {
                Global.setParameter("grid.leftsnap", true);
            }
        } catch (final Exception e) {
        }
        if (getParameter("interactive") != null && getParameter("interactive").equals("false")) {
            ZC.setInteractive(false);
        }
        boolean job = false;
        ZC.IW = getSize().width;
        ZC.IH = getSize().height;
        if (getParameter("germanpoints") != null && getParameter("germanpoints").equals("true")) {
            Global.setParameter("options.germanpoints", true);
        }
        try {
            final InputStream o = getClass().getResourceAsStream("/builtin.mcr");
            ZC.ProtectMacros = true;
            ZC.load(o, false, true);
            ZC.ProtectMacros = false;
            o.close();
            ZC.PM.removeAll();
        } catch (final Exception e) {
        }
        filename = getParameter("file");
        if (filename == null) {
            filename = getParameter("job");
            job = true;
        }
        if (filename != null) {
            boolean firsttry = true;
            while (true) {
                try {
                    URL url;
                    if (filename.toUpperCase().startsWith("HTTP")) {
                        url = new URL(firsttry ? FileName.toURL(filename) : filename);
                    } else {
                        url = new URL(getCodeBase(), firsttry ? FileName.toURL(filename) : filename);
                    }
                    ZC.clear();
                    InputStream in = url.openStream();
                    if (ZirkelFrame.isCompressed(filename)) {
                        in = new GZIPInputStream(in);
                    }
                    showStatus(Global.name("loading"));
                    ZC.load(in);
                    toggleGrid(ZC.ShowGrid);
                    if (job) {
                        ZC.displayJob(true);
                        ZC.setDoneListener(this);
                    }
                    if (icons) {
                        iconPressed(FirstConstructor);
                    }
                    in.close();
                    if (getParameter("background") != null) {
                        final Image i = getToolkit().getImage(new URL(getCodeBase(), getParameter("background")));
                        final MediaTracker mt = new MediaTracker(this);
                        mt.addImage(i, 0);
                        mt.waitForID(0);
                        if (mt.checkID(0) && !mt.isErrorAny()) {
                            ZC.setBackground(i);
                        }
                    }
                    ZC.repaint();
                } catch (final Exception e) {
                    if (firsttry) {
                        firsttry = false;
                        continue;
                    }
                    final Warning w = new Warning(F, FileName.chop(32, "" + e, 64), Zirkel.name("message"), true);
                    w.center(F);
                    w.setVisible(true);
                    showStatus("" + e);
                    e.printStackTrace();
                    System.out.println(e);
                }
                break;
            }
        }
        if (breaks) {
            IC = new IconBar(F);
            IC.setBackground(CC);
            IC.setIconBarListener(this);
            IC.addLeft("allback");
            if (haveBreaks()) {
                IC.addLeft("nextbreak");
            } else {
                IC.addLeft("oneforward");
            }
            IC.addLeft("allforward");
            final javax.swing.JPanel pic = new Panel3D(IC);
            getContentPane().add("South", pic);
            IC.setEnabled("nextbreak", false);
            IC.setEnabled("oneforward", false);
            IC.setEnabled("allforward", false);
            ZC.getConstruction().setOriginalOrder(true);
            jumptostart = (getParameter("jumptostart") != null);
        }
        if (getParameter("restrictedmove") != null) {
            Global.setParameter("restrictedmove", true);
        }
        if (getParameter("noconfirmation") != null) {
            Global.setParameter("confirmation", false);
        }
        if (IA != null) {
            settype(2);
        }
        ZC.setMacroBar(IM);
        ZC.updateMacroBar();
        ZC.recompute();
        ZC.setSize(ZC.getSize().width + 1, ZC.getSize().height + 1);
        ZC.setSize(ZC.getSize().width - 1, ZC.getSize().height - 1);
        ZC.JCM.hideHandles(null);
    }
