    public static FormElement getChannelAccess(final AccessChannelTreeWidget channelTree) {
        CaptionPanel wrapper = new CaptionPanel("栏目权限");
        wrapper.add(channelTree);
        FormElement el = new FormElement(wrapper, new IFormElementProvider() {

            public void setValue(Object object) {
                List access = ((SysRoleDto) object).getChannelAccesses();
                if (access == null) return;
                for (Iterator iv = access.iterator(); iv.hasNext(); ) {
                    AccessItemDto dto = (AccessItemDto) iv.next();
                    for (Iterator it = channelTree.getTree().iterator(); it.hasNext(); ) {
                        CheckBoxTreeItemUI item = (CheckBoxTreeItemUI) it.next();
                        if (item.getTreeItem().getUserObject() instanceof AccessItemDto) {
                            AccessItemDto model = (AccessItemDto) item.getTreeItem().getUserObject();
                            if (model.getCode().equals(dto.getCode())) {
                                item.getCheckBox().setValue(true);
                                CheckBoxTreeItemUI pa = (CheckBoxTreeItemUI) item.getTreeItem().getParentItem().getWidget();
                                pa.getCheckBox().setValue(true);
                                checkParentChannel(pa);
                            }
                        }
                    }
                }
            }

            public void getValue(Object object) {
                List values = channelTree.getTree().getCheckUserObject();
                List r = new ArrayList();
                for (Iterator it = values.iterator(); it.hasNext(); ) {
                    Object ob = it.next();
                    if (ob instanceof AccessItemDto) {
                        AccessItemDto aDto = (AccessItemDto) ob;
                        r.add(aDto);
                    }
                }
                ((SysRoleDto) object).setChannelAccessListAndCheck(r);
            }

            public void reset(IFormElement element) {
                channelTree.getTree().setCheckUserObject(null);
            }
        });
        return el;
    }
