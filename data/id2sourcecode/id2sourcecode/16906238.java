    private Widget getForm() {
        WidgetContainer wrapper = new WidgetContainer();
        wrapper.setSize("100%", "100%");
        form = new FormContainer(2);
        final FishLabelTextBox name = new FishLabelTextBox("角色名称:");
        final FishLabelTextAreaBox description = new FishLabelTextAreaBox("角色描述:");
        form.addElement(new FormElement(name, new FormElementProviderAdpter() {

            public void setValue(Object object) {
                name.setValue(((SysRoleDto) object).getName());
            }

            public void getValue(Object object) {
                ((SysRoleDto) object).setName((String) name.getValue());
            }
        }, new IValidator[] { ValidatorCreator.require() }));
        form.addElement(new FormElement(description, new FormElementProviderAdpter() {

            public void setValue(Object object) {
                description.setValue(((SysRoleDto) object).getDescription());
            }

            public void getValue(Object object) {
                ((SysRoleDto) object).setDescription((String) description.getValue());
            }
        }));
        form.addElement(getFunctionAccess(functionTree));
        form.addElement(getChannelAccess(channelTree));
        form.getContainer().getCellFormatter().setVerticalAlignment(1, 0, VerticalPanel.ALIGN_TOP);
        form.getContainer().getCellFormatter().setVerticalAlignment(1, 1, VerticalPanel.ALIGN_TOP);
        wrapper.add(form);
        return wrapper;
    }
