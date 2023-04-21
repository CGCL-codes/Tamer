    public void addFigurePanel(FigurePanel xp2) {
        if (xpList.size() >= 1) {
            xpList.add(xp2);
            xp2.setGraphicalViewer(this);
            xp2.addMouseMotionListener(this);
            content.removeAll();
            holder = new JPanel();
            int gcols = 0;
            int grows = 0;
            int xpnum = xpList.size();
            if (xpnum == 2) {
                gcols = 2;
                grows = 1;
            } else if (xpnum == 3 || xpnum == 4) {
                gcols = 2;
                grows = 2;
            } else if (xpnum == 5 || xpnum == 6) {
                gcols = 3;
                grows = 2;
            } else {
                int quadnum = 0;
                quadnum = (int) Math.sqrt(xpnum);
                gcols = quadnum;
                grows = quadnum;
            }
            int newid = idSet.getNextID();
            xp2.setID(newid);
            log.debug("add figurepanel ");
            log.debug("giv em id :" + newid);
            log.debug("figure to str: " + xp2);
            log.debug("---");
            holder.setLayout(new GridLayout(grows, gcols));
            holder.setBackground(Color.white);
            holder.addMouseMotionListener(this);
            for (FigurePanel p : xpList) {
                holder.add(p);
            }
            content.add(holder, BorderLayout.CENTER);
            ActivationPanel slit = new ActivationPanel(this);
            content.add(slit, BorderLayout.PAGE_END);
            toolboxPanel = new ToolboxPanel(this);
            pack();
            xpList.get(0).setPreferredSize(new Dimension(450, 500));
            xpList.get(0).setSize(new Dimension(450, 500));
            xpList.get(1).setPreferredSize(new Dimension(450, 500));
            xpList.get(1).setSize(new Dimension(450, 500));
            setSize(700, 500);
            repaint();
        } else {
            initFigurePanel(xp2);
        }
    }
