    public String chooseTpl() {
        String s = getTplContent();
        if (StringUtils.isBlank(s)) {
            return getChannel().chooseTplContent();
        } else {
            return getWebsite().getTplRoot().append(getTplContent()).toString();
        }
    }
