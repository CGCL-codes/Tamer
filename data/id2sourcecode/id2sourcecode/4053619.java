            public void renderColumn(CellItem cell) {
                NewsDto dto = (NewsDto) cell.getUserObject();
                dto.setChannelNameExt(getChannelName(dto.getChannelId()));
                cell.setText(dto.getChannelNameExt());
            }
