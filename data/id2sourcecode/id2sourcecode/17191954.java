    private void loadAutomations(Document doc) throws InstantiationException, IllegalAccessException {
        NodeList automationsList = doc.getElementsByTagName("automation");
        for (int i = 0; i < automationsList.getLength(); i++) {
            Node automationNode = automationsList.item(i);
            NamedNodeMap attributes = automationNode.getAttributes();
            String name = attributes.getNamedItem("name").getNodeValue();
            Automation automation = getAutomation(name);
            if (automation != null) {
                ErrorMessage dialog = new ErrorMessage(2);
                dialog.append("An automation called \"" + name + "\" already exists. Do you want to overwrite the existing automation or to keep it ?");
                dialog.setButton(0, "Overwrite");
                dialog.setButton(1, "Keep existing");
                dialog.setTitle("Importing automation \"" + name + "\"");
                dialog.setVisible(true);
                if (dialog.getButton() != 0) {
                    continue;
                }
                automation.loadAttributes(attributes);
                automation.removeAllGenerators();
            } else {
                automation = new Automation(attributes);
            }
            automation.loadGenerators(automationNode.getChildNodes(), generatorFactory);
            addAutomation(automation);
        }
    }
