    public String getTplContentOrDef() {
        String tpl = getTplContent();
        if (!StringUtils.isBlank(tpl)) {
            return tpl;
        } else {
            return getChannel().getTplContentOrDef();
        }
    }
