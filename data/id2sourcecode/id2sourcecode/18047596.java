    private static List<PortletDefinition> processPortletFile(File portletFile) throws PortletContainerException {
        System.out.println("Start process portlet file: " + portletFile.getName());
        List<PortletDefinition> portletList = new ArrayList<PortletDefinition>();
        if (portletFile.exists()) {
            try {
                PortletApplication portletApp = portletDefinitionProcessor.digest(portletFile);
                for (int i = 0; i < portletApp.getPortletCount(); i++) {
                    PortletDefinition portletDefinition = portletApp.getPortlet(i);
                    portletDefinition.setApplicationName(PortletService.isEmpty(portletApp.getId()) ? "" : portletApp.getId());
                    if (portletDefinition.getPortletPreferences() == null) {
                        portletDefinition.setPortletPreferences(new PortletPreferencesImpl());
                    }
                    System.out.println("Add new portlet: " + portletDefinition.getFullPortletName());
                    portletList.add(portletDefinition);
                }
            } catch (Exception e) {
                String errorString = "Error processing portlet file " + portletFile.getName();
                throw new PortletContainerException(errorString, e);
            }
        }
        return portletList;
    }
