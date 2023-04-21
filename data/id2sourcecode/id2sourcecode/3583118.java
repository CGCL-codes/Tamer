            public void onClick(ClickEvent clickEvent) {
                final NewsDto dto = (NewsDto) mm.getModel();
                if (dto.getChannelId() == null) {
                    Message.info("请选择栏目！");
                    return;
                }
                if (!baseForm.isValid()) return;
                getBodyModel(dto);
                baseForm.getModelManger().getModel();
                ServiceFactory.invoke(NewsManager.class.getName(), "saveAndUpdate", new Object[] { dto }, new LoadingAsyncCallback() {

                    public void success(Object result) {
                        InfoPanel.show("保存成功！");
                        NewsDto ins = (NewsDto) getNewContent();
                        ins.setChannelId(dto.getChannelId());
                        mm.renderModel(ins);
                        contentTab.setSelectItem(baseInfoItem);
                    }
                });
            }
