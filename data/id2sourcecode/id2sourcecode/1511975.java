    @TestTargetNew(level = TestLevel.COMPLETE, notes = "this function does not read parameter 'flag'.", method = "writeToParcel", args = { android.os.Parcel.class, int.class })
    public void testWriteToParcel() {
        Criteria criteria = new Criteria();
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setSpeedRequired(true);
        Parcel parcel = Parcel.obtain();
        criteria.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Criteria newCriteria = Criteria.CREATOR.createFromParcel(parcel);
        assertEquals(criteria.getAccuracy(), newCriteria.getAccuracy());
        assertEquals(criteria.getPowerRequirement(), newCriteria.getPowerRequirement());
        assertEquals(criteria.isAltitudeRequired(), newCriteria.isAltitudeRequired());
        assertEquals(criteria.isBearingRequired(), newCriteria.isBearingRequired());
        assertEquals(criteria.isSpeedRequired(), newCriteria.isSpeedRequired());
        assertEquals(criteria.isCostAllowed(), newCriteria.isCostAllowed());
    }
