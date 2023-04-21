    private ColumnWaiter getColsWaiter() {
        ColumnExt co1 = new ColumnExt("内容名称", 200, new IColumnRender() {

            public void renderColumn(CellItem cell) {
                NewsDto dto = (NewsDto) cell.getUserObject();
                cell.setText(dto.getName());
            }
        });
        ColumnExt co8 = new ColumnExt("所属栏目", 200, new IColumnRender() {

            public void renderColumn(CellItem cell) {
                NewsDto dto = (NewsDto) cell.getUserObject();
                dto.setChannelNameExt(getChannelName(dto.getChannelId()));
                cell.setText(dto.getChannelNameExt());
            }
        });
        ColumnExt co3 = new ColumnExt("审核状态", 100, new IColumnRender() {

            public void renderColumn(CellItem cell) {
                NewsDto dto = (NewsDto) cell.getUserObject();
                cell.setText(CodesHelper.getNameByCode(CMSConstants._CODE_CONTENT_WORKFLOW_STATUS_ID, dto.getWorkflowStatus()));
            }
        });
        ColumnExt co4 = new ColumnExt("创建日期", 150, new IColumnRender() {

            public void renderColumn(CellItem cell) {
                NewsDto dto = (NewsDto) cell.getUserObject();
                cell.setText(DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(dto.getCreateTime()));
            }
        });
        ColumnExt co2 = new ColumnExt("审核日期", 150, new IColumnRender() {

            public void renderColumn(CellItem cell) {
                NewsDto dto = (NewsDto) cell.getUserObject();
                if (dto.getAuditTime() != null) cell.setText(DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(dto.getAuditTime()));
            }
        });
        ColumnExt co6 = new ColumnExt("发布日期", 150, new IColumnRender() {

            public void renderColumn(CellItem cell) {
                NewsDto dto = (NewsDto) cell.getUserObject();
                if (dto.getAuditTime() != null) cell.setText(DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(dto.getPublishTime()));
            }
        });
        ColumnExt co5 = new ColumnExt("操作", 100, new IColumnRender() {

            public void renderColumn(final CellItem cell) {
                HorizontalPanel bar = new HorizontalPanel();
                MyButton t1 = new MyButton("预览");
                MyButton t2 = new MyButton("编辑");
                bar.add(t1);
                bar.add(new HTML("&nbsp;"));
                bar.add(t2);
                cell.setWidget(bar);
                t1.addClickHandler(new ClickHandler() {

                    public void onClick(ClickEvent clickEvent) {
                    }
                });
                t2.addClickHandler(new ClickHandler() {

                    public void onClick(ClickEvent clickEvent) {
                        CMSDashboard.dispatchPage(EditContentPage.class.getName(), new PageClient() {

                            public void success(AbstractPage page) {
                                page.getModelManger().renderModel(ModelReflection.clone(cell.getUserObject()));
                            }

                            public void failure() {
                            }
                        });
                    }
                });
            }
        });
        ColumnWaiter cw = new ColumnWaiter(new ColumnExt[] { co1, co8, co3, co4, co2, co6, co5 });
        return cw;
    }
