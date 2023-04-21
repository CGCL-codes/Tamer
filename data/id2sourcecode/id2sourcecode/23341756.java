    public CrumbPanel(Workspace workspace, CrumbData crumbData, GuiController guiController) {
        this.workspace = workspace;
        this.crumbData = crumbData;
        this.guiController = guiController;
        if (crumbData.getName().equals("Side")) {
            this.isSideCrumb = true;
            this.isTopLinkable = false;
            this.isBottomLinkable = false;
            if (useCrumbImage == false) {
            } else {
                setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/purple.png")));
            }
        } else if (crumbData.getName().equals("End")) {
            this.isEndCrumb = true;
            this.isTopLinkable = false;
            if (useCrumbImage == false) {
            } else {
                setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/purple.png")));
            }
        } else if (crumbData.getName().equals("Father")) {
            isTopLinkable = false;
            isFatherCrumb = true;
            if (useCrumbImage == false) {
            } else {
                setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/button.png")));
            }
        } else if (crumbData.getName().equals("While") || crumbData.getName().equals("Loop") || crumbData.getName().equals("If") || crumbData.getName().equals("Else If") || crumbData.getName().equals("Move Servo")) {
            isParamLinkable = true;
        } else if (crumbData.getType() == CrumbData.CrumbType.Conditional || crumbData.getType() == CrumbData.CrumbType.Expression) {
            this.isTopLinkable = false;
            this.isBottomLinkable = false;
            this.isParamCrumb = true;
            this.isParamLinkable = true;
            this.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        }
        if (this.getCrumbData().getCategory().equalsIgnoreCase("Variables") || this.getCrumbData().getCategory().equalsIgnoreCase("Functions")) {
            Color varColor = new Color(172, 225, 238);
            this.setForeground(varColor);
        } else this.setForeground(Color.white);
        this.setOpaque(true);
        layoutParameters();
        updateHitBox();
        Color color1 = new Color(41, 74, 73);
        Color color2 = new Color(130, 191, 147);
        Color color4 = new Color(189, 110, 70);
        Color color5 = new Color(69, 59, 44);
        Color color6 = new Color(74, 37, 31);
        Color color8 = new Color(120, 114, 72);
        Color color9 = new Color(178, 148, 71);
        Color color10 = new Color(224, 156, 72);
        Color color11 = new Color(86, 22, 75);
        Color color12 = new Color(12, 54, 1);
        Color color13 = new Color(48, 90, 61);
        if (crumbData.getCategory().equalsIgnoreCase("led")) {
            this.setBackground(color2);
        } else if (crumbData.getCategory().equalsIgnoreCase("control")) {
            this.setBackground(color1);
        } else if (crumbData.getCategory().equalsIgnoreCase("dc motor")) {
            this.setBackground(Color.MAGENTA);
        } else if (crumbData.getCategory().equalsIgnoreCase("servo")) {
            this.setBackground(color6);
        } else if (crumbData.getCategory().equalsIgnoreCase("conditionals")) {
            this.setBackground(color4);
        } else if (crumbData.getCategory().equalsIgnoreCase("variables")) {
            this.setBackground(color8);
        } else if (crumbData.getCategory().equalsIgnoreCase("expressions")) {
            this.setBackground(color11);
        } else if (crumbData.getCategory().equalsIgnoreCase("functions")) {
            this.setBackground(color9);
        } else if (crumbData.getCategory().equalsIgnoreCase("arduino funcs")) {
            this.setBackground(color12);
        } else if (crumbData.getCategory().equalsIgnoreCase("read/write")) {
            this.setBackground(color13);
        } else {
            this.setBackground(color5);
        }
        if (this.getCrumbData().getType().equals(CrumbType.Statement)) {
        } else if (this.getCrumbData().getType().equals(CrumbType.Conditional)) {
        } else if (this.getCrumbData().getType().equals(CrumbType.Control)) {
            this.isBeginCrumb = true;
        } else if (this.getCrumbData().getType().equals(CrumbType.Expression)) {
        } else if (this.getCrumbData().getType().equals(CrumbType.Other)) {
        }
        addCrumbMouseListeners();
    }
