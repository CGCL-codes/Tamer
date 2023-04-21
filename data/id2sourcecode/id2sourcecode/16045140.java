    public static String buildNavPosSelector(CmsObject cms, String filename, String attributes, CmsMessages messages) {
        CmsJspNavElement curNav = CmsJspNavBuilder.getNavigationForResource(cms, filename);
        filename = CmsResource.getParentFolder(filename);
        List navList = CmsJspNavBuilder.getNavigationForFolder(cms, filename);
        float maxValue = 0;
        float nextPos = 0;
        float firstValue = 1;
        if (navList.size() > 0) {
            try {
                CmsJspNavElement ne = (CmsJspNavElement) navList.get(0);
                maxValue = ne.getNavPosition();
            } catch (Exception e) {
                LOG.error(e.getLocalizedMessage());
            }
        }
        if (maxValue != 0) {
            firstValue = maxValue / 2;
        }
        List options = new ArrayList(navList.size() + 1);
        List values = new ArrayList(navList.size() + 1);
        options.add(messages.key(Messages.GUI_CHNAV_POS_FIRST_0));
        values.add(firstValue + "");
        for (int i = 0; i < navList.size(); i++) {
            CmsJspNavElement ne = (CmsJspNavElement) navList.get(i);
            String navText = ne.getNavText();
            float navPos = ne.getNavPosition();
            nextPos = navPos + 2;
            if ((i + 1) < navList.size()) {
                nextPos = ((CmsJspNavElement) navList.get(i + 1)).getNavPosition();
            }
            float newPos;
            if ((nextPos - navPos) > 1) {
                newPos = navPos + 1;
            } else {
                newPos = (navPos + nextPos) / 2;
            }
            if (navPos > maxValue) {
                maxValue = navPos;
            }
            if (curNav.getNavText().equals(navText) && (curNav.getNavPosition() == navPos)) {
                options.add(CmsEncoder.escapeHtml(messages.key(Messages.GUI_CHNAV_POS_CURRENT_1, new Object[] { ne.getFileName() })));
                values.add("-1");
            } else {
                options.add(CmsEncoder.escapeHtml(navText + " [" + ne.getFileName() + "]"));
                values.add(newPos + "");
            }
        }
        options.add(messages.key(Messages.GUI_CHNAV_POS_LAST_0));
        values.add((maxValue + 1) + "");
        options.add(messages.key(Messages.GUI_CHNAV_NO_CHANGE_0));
        if (curNav.getNavPosition() == Float.MAX_VALUE) {
            values.add((maxValue + 1) + "");
        } else {
            values.add("-1");
        }
        if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(attributes)) {
            attributes = " " + attributes;
        } else {
            attributes = "";
        }
        return CmsWorkplace.buildSelect("name=\"" + PARAM_NAVPOS + "\"" + attributes, options, values, values.size() - 1, true);
    }
