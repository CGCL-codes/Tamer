    private ChannelDto getChannelModel(ChannelDto parent) {
        ChannelDto dto = new ChannelDto();
        dto.setParentExt(parent);
        if (parent != null) dto.setParentId(parent.getId());
        return dto;
    }
