    protected void saveTemplate(String templateName, TemplateType type, ReportObject templateObject) {
        File baseTemplatePath = TemplateUtilities.getUserTemplateDir();
        File templatePath = new File(baseTemplatePath, type.getTemplatePath());
        File template = new File(templatePath, templateName + TEMPLATE_EXTENSION);
        if (!template.exists()) {
            TemplateUtilities.writeTemplate(template, templateObject);
            JOptionPane.showMessageDialog(NavigationPanel.getInstance(), "Template saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int value = JOptionPane.showConfirmDialog(NavigationPanel.getInstance(), "Template already exists. Click on OK to save and overwrite or CANCEL to abort", "Warning", JOptionPane.WARNING_MESSAGE);
            if (value == JOptionPane.OK_OPTION) {
                TemplateUtilities.writeTemplate(template, templateObject);
                JOptionPane.showMessageDialog(NavigationPanel.getInstance(), "Template saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
