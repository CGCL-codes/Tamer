    private List getTagInfoList(CmsTagCategory entity) {
        if (null != entity) {
            List list = new ArrayList();
            String tagSort = entity.getTagSort();
            String categoryId = entity.getCategoryId();
            List id_list = new ArrayList();
            try {
                id_list = StringTool.stringToArray(categoryId);
                if (null != id_list && "channels".equals(tagSort)) {
                    for (int i = 0; i < id_list.size(); i++) {
                        String channelsId = (String) id_list.get(i);
                        channelsId = channelsId.replaceAll(" ", "");
                        if (StringTool.isNull(channelsId)) {
                            SiteChannels data = (SiteChannels) siteChannelsManager.getById(Integer.parseInt(channelsId));
                            data.setName(data.getChannelsName());
                            list.add(data);
                        }
                    }
                }
                if (null != id_list && "category".equals(tagSort)) {
                    for (int i = 0; i < id_list.size(); i++) {
                        String channelsId = (String) id_list.get(i);
                        channelsId = channelsId.replaceAll(" ", "");
                        if (StringTool.isNull(channelsId)) {
                            SitePart data = (SitePart) sitePartManager.getById(Integer.parseInt(channelsId));
                            data.setName(data.getPartName());
                            list.add(data);
                        }
                    }
                }
                if (null != id_list && "sort".equals(tagSort)) {
                    for (int i = 0; i < id_list.size(); i++) {
                        String channelsId = (String) id_list.get(i);
                        channelsId = channelsId.replaceAll(" ", "");
                        if (StringTool.isNull(channelsId)) {
                            SiteContentSort data = (SiteContentSort) siteContentSortManager.getById(Integer.parseInt(channelsId));
                            data.setName(data.getClassifyName());
                            list.add(data);
                        }
                    }
                }
            } catch (Exception exe) {
            }
            return list;
        } else {
            return null;
        }
    }
