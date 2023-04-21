                    public void success(Object result) {
                        InfoPanel.show("保存成功！");
                        NewsDto ins = (NewsDto) getNewContent();
                        ins.setChannelId(dto.getChannelId());
                        mm.renderModel(ins);
                        contentTab.setSelectItem(baseInfoItem);
                    }
