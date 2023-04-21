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
