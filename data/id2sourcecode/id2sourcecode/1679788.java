    public EList<DesiredResult> getChannelsEffortToward() {
        if (channelsEffortToward == null) {
            channelsEffortToward = new EObjectWithInverseResolvingEList.ManyInverse<DesiredResult>(DesiredResult.class, this, BmmPackage.COURSE_OF_ACTION__CHANNELS_EFFORT_TOWARD, BmmPackage.DESIRED_RESULT__SUPPORTED_BY);
        }
        return channelsEffortToward;
    }
