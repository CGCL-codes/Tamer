    public void initFigurePanel(FigurePanel p) {
        int newid = idSet.getNextID();
        p.setID(newid);
        log.debug("add figurepanel ");
        log.debug("giv em id :" + newid);
        log.debug("figure to str: " + p);
        log.debug("---");
        xpList.add(p);
        this.xp = p;
        xp.setGraphicalViewer(this);
        xp.addMouseMotionListener(this);
        content.removeAll();
        content.add(xp, BorderLayout.CENTER);
        slit = new ActivationPanel(this);
        content.add(slit, BorderLayout.PAGE_END);
        pack();
        p.setPreferredSize(new Dimension(500, 500));
        p.setSize(new Dimension(500, 500));
        setSize(700, 500);
        initMenu();
        repaint();
    }
