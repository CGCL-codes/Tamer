    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != data && null != data.getExtras()) {
            Bundle lBundle = data.getExtras();
            String lDatetime = lBundle.getString("DATATIME");
            SimpleDateFormat lSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            switch(resultCode) {
                case 1:
                    try {
                        if (lSdf.parse(lDatetime).compareTo(lSdf.parse(lSdf.format(new Date()))) > 0) {
                            Toast.makeText(Enforcement.this, "开始时间比系统时间晚,请重新选择!", Toast.LENGTH_SHORT).show();
                            isStarted = false;
                            return;
                        }
                        break;
                    } catch (Exception e) {
                        return;
                    }
                case 2:
                    try {
                        if (lSdf.parse(lDatetime).compareTo(mStartdutytime) < 0) {
                            Toast.makeText(Enforcement.this, "结束时间比开始时间早,请重新选择!", Toast.LENGTH_SHORT).show();
                            isStarted = true;
                            return;
                        }
                        break;
                    } catch (Exception e) {
                        return;
                    }
            }
            if (image[3] == R.drawable.homenext1) {
                image[3] = R.drawable.homeback1;
                list.remove(3);
                list.add(3, new GridInfo(R.string.homeback));
            } else {
                image[3] = R.drawable.homenext1;
                list.remove(3);
                list.add(3, new GridInfo(R.string.homenext));
            }
            this.adapter.setImage(image);
            this.adapter.setList(list);
            this.gridview.invalidateViews();
            int lYear = lBundle.getInt("YEAR");
            int lMonth = lBundle.getInt("MONTH");
            int lDay = lBundle.getInt("DAY");
            int lHour = lBundle.getInt("HOUR");
            int lMinute = lBundle.getInt("MINUTE");
            DutyInfo lDutyInfo = new DutyInfo();
            ContentValues lValues = null;
            Intent lIntent;
            switch(resultCode) {
                case 1:
                    mMcTJGuid = CommonUtil.randomNumber(19);
                    mAfTJGuid = CommonUtil.randomNumber(19);
                    mDutyno = CommonUtil.randomNumber(19);
                    Calendar lCal = Calendar.getInstance();
                    lCal.set(lYear, lMonth, lDay, lHour, lMinute);
                    mStartdutytime = lCal.getTime();
                    lDutyInfo.setDusername(LoginActivity.g_username);
                    lDutyInfo.setDutyno(mDutyno);
                    lDutyInfo.setCdrc((SxryMActivity.SXRP_SELECT_MEMBER.split(",").length + SxryActivity.QTRY_EDIT_MEMBER_COUNT) + "");
                    lDutyInfo.setTogetheruser(SxryMActivity.SXRP_SELECT_MEMBER.replaceAll(",", "|"));
                    lDutyInfo.setOtheruser(SxryActivity.QTRY_EDIT_MEMBER);
                    lDutyInfo.setOthercount(SxryActivity.QTRY_EDIT_MEMBER_COUNT + "");
                    lDutyInfo.setDutystatus("1");
                    lDutyInfo.setMctjuuid(mMcTJGuid);
                    lDutyInfo.setAftjuuid(mAfTJGuid);
                    lDutyInfo.setStartdutytime(lYear + "-" + lMonth + "-" + lDay + " " + lHour + ":" + lMinute + ":00");
                    lDutyInfo.setEnddutytime("");
                    lIntent = new Intent(Enforcement.this, UploadDutyInfoService.class);
                    lIntent.putExtra("dutyinfo", lDutyInfo);
                    startService(lIntent);
                    lValues = ContentValuesUtil.convertDutyInfo(lDutyInfo);
                    mDBHelper = new DutyInfoDBHelper(Enforcement.this);
                    mDBHelper.insertDutyInfo(lValues);
                    mDBHelper.closeDB();
                    break;
                case 2:
                    lDutyInfo.setDutyno(mDutyno);
                    lDutyInfo.setDusername(LoginActivity.g_username);
                    lDutyInfo.setCdrc((SxryMActivity.SXRP_SELECT_MEMBER.split(",").length + SxryActivity.QTRY_EDIT_MEMBER_COUNT) + "");
                    lDutyInfo.setTogetheruser(SxryMActivity.SXRP_SELECT_MEMBER);
                    lDutyInfo.setOtheruser(SxryActivity.QTRY_EDIT_MEMBER);
                    lDutyInfo.setOthercount(SxryActivity.QTRY_EDIT_MEMBER_COUNT + "");
                    lDutyInfo.setDutystatus("2");
                    lDutyInfo.setEnddutytime(lYear + "-" + lMonth + "-" + lDay + " " + lHour + ":" + lMinute + ":00");
                    lDutyInfo.setStartdutytime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(mStartdutytime));
                    CheckDraftDBHelper lDBHelper2 = new CheckDraftDBHelper(Enforcement.this);
                    mDraftList = lDBHelper2.findDraftByType();
                    lDBHelper2.closeDB();
                    lDutyInfo.setAfareacode(getAreacode("暗访"));
                    lDutyInfo.setMcareacode(getAreacode("明察"));
                    lIntent = new Intent(Enforcement.this, UploadDutyInfoService.class);
                    lIntent.putExtra("dutyinfo", lDutyInfo);
                    startService(lIntent);
                    lValues = ContentValuesUtil.convertDutyInfo(lDutyInfo);
                    mDBHelper = new DutyInfoDBHelper(Enforcement.this);
                    mDBHelper.updateDutyInfo(lValues);
                    mDBHelper.closeDB();
                    break;
            }
        } else {
            isStarted = isStarted == true ? false : true;
        }
    }
