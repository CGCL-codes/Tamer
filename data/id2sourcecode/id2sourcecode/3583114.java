    private void initBar() {
        bar = new ToolBar();
        bar.setWidth("100%");
        ToolItem save = new ToolItem("确认保存");
        ToolItem pagination = new ToolItem("添加分页");
        ToolItem cancel = new ToolItem("取消");
        bar.addToolItem(save);
        bar.addToolItem(pagination);
        bar.addToolItem(cancel);
        cancel.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                final NewsDto dto = (NewsDto) mm.getModel();
                if (dto.getId() == null) {
                    NewsDto ins = (NewsDto) getNewContent();
                    ins.setChannelId(dto.getChannelId());
                    mm.renderModel(ins);
                    contentTab.setSelectItem(baseInfoItem);
                } else {
                    CMSDashboard.dispatchPage(ContentListPage.class.getName(), new PageClient() {

                        public void success(AbstractPage page) {
                        }

                        public void failure() {
                        }
                    });
                }
            }
        });
        save.addClickHandler(new ClickHandler() {

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
        });
        pagination.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                addItem(null);
            }
        });
    }
