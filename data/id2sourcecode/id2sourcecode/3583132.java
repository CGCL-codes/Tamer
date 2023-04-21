    public void ddOutModel(Object[] parameters, Object model) {
        if (model == null) {
            model = getNewContent();
            mm.cacheModel(model);
        }
        baseForm.getModelManger().renderModel(model);
        if (((NewsDto) model).getChannelNameExt() != null) main.setTitle(((NewsDto) model).getChannelNameExt() + "[内容详情]");
        renderBodyModel((NewsDto) model);
    }
