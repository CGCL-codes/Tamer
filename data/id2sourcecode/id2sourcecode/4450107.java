    public boolean isHasUpdateRight(CmsUser user) {
        AfterCheckEnum after = getChannel().getAfterCheckEnum();
        if (AfterCheckEnum.CANNOT_UPDATE == after) {
            CmsSite site = getSite();
            Byte userStep = user.getCheckStep(site.getId());
            Byte channelStep = getChannel().getFinalStepExtends();
            boolean checked = getStatus() == ContentCheck.CHECKED;
            if (getCheckStep() > userStep || (checked && userStep < channelStep)) {
                return false;
            } else {
                return true;
            }
        } else if (AfterCheckEnum.BACK_UPDATE == after || AfterCheckEnum.KEEP_UPDATE == after) {
            return true;
        } else {
            throw new RuntimeException("AfterCheckEnum '" + after + "' did not handled");
        }
    }
