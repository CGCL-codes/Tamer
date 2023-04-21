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
