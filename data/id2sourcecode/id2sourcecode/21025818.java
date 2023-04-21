        @Override
        public void actionPerformed(ActionEvent e) {
            InputLine descriptor = new DialogDescriptor.InputLine("Template Name:", "Save to Template");
            descriptor.setOptions(new Object[] { DialogDescriptor.OK_OPTION, DialogDescriptor.CANCEL_OPTION });
            Object ret = DialogDisplayer.getDefault().notify(descriptor);
            if (ret.equals(DialogDescriptor.OK_OPTION)) {
                String name = descriptor.getInputText();
                if (!TemplateManager.getDefault().templateExists(name)) {
                    TemplateManager.getDefault().saveToTemplate(name, chartFrame);
                } else {
                    Confirmation confirmation = new DialogDescriptor.Confirmation("<html>This template already exists!<br>Do you want to overwrite this template?</html>", "Overwrite");
                    Object obj = DialogDisplayer.getDefault().notify(confirmation);
                    if (obj.equals(DialogDescriptor.OK_OPTION)) {
                        TemplateManager.getDefault().removeTemplate(name);
                        TemplateManager.getDefault().saveToTemplate(name, chartFrame);
                    }
                }
            }
        }
