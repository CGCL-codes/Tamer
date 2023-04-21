    public static int checkdutyUpdate(DraftInfo pDraftInfo) {
        mHttpClient = new DefaultHttpClient();
        mBuilder = new StringBuilder();
        mHttpPost = new HttpPost(new StringBuilder().append("http://").append(Enforcement.HOST).append("/ZJWHServiceTest/GIS_CheckDaily.asmx/PDARecieveCheckInfo").toString());
        try {
            String[] jcqks = pDraftInfo.getJcqk().split(";");
            StringBuilder lQtBuilder = new StringBuilder();
            StringBuilder lWgBuilder = new StringBuilder();
            for (String jcqk : jcqks) {
                if (jcqk.contains("#")) {
                    lWgBuilder.append("").append(jcqk).append("&");
                } else {
                    lQtBuilder.append("").append(jcqk).append(";");
                }
            }
            List<NameValuePair> lNameValuePairs = new ArrayList<NameValuePair>(2);
            StringBuffer lSb = new StringBuffer();
            lSb.append("id=").append(pDraftInfo.getId()).append("^");
            lSb.append("dutyno=").append(pDraftInfo.getDutyno()).append("^");
            lSb.append("whcsno=").append(pDraftInfo.getCsid()).append("^");
            lSb.append("reachtime=").append(pDraftInfo.getArrivaltime()).append("^");
            lSb.append("leavetime=").append(pDraftInfo.getLeavetime()).append("^");
            lSb.append("dailycheckno=").append(pDraftInfo.getWhcsdutyid()).append("^");
            lSb.append("jcqk=").append(lQtBuilder.toString()).append("^");
            lSb.append("tjGuid=").append(pDraftInfo.getTjguid()).append("^");
            lSb.append("area=").append(pDraftInfo.getArea()).append("^");
            lSb.append("cdrc=").append(pDraftInfo.getCdrc()).append("^");
            lSb.append("xs=").append(pDraftInfo.getCheckxs()).append("^");
            lSb.append("account=").append(LoginActivity.g_username).append("^");
            lSb.append("together=").append(pDraftInfo.getTogether()).append("^");
            lSb.append("whcsname=").append(pDraftInfo.getContitle()).append("^");
            lSb.append("areatype=").append(pDraftInfo.getAreatype()).append("^");
            lSb.append("address=").append(pDraftInfo.getAddress()).append("^");
            lSb.append("jcjg=").append(pDraftInfo.getJcjg()).append("^");
            lSb.append("jcsj=").append(pDraftInfo.getChecktime()).append("^");
            lSb.append("permitcode=").append(pDraftInfo.getPermitcode()).append("^");
            lSb.append("permitword=").append(pDraftInfo.getPermitword()).append("^");
            lSb.append("manager=").append(pDraftInfo.getManager()).append("^");
            lSb.append("cslb=").append(pDraftInfo.getCslb()).append("^");
            lSb.append("jcjgDetial=").append(lWgBuilder.toString()).append("~");
            lNameValuePairs.add(new BasicNameValuePair("checkinfo", lSb.toString()));
            mHttpPost.setEntity(new UrlEncodedFormEntity(lNameValuePairs));
            HttpResponse lHttpReponse = mHttpClient.execute(mHttpPost);
            BufferedReader lReader = new BufferedReader(new InputStreamReader(lHttpReponse.getEntity().getContent()));
            for (String s = lReader.readLine(); null != s; s = lReader.readLine()) {
                mBuilder.append(s);
            }
            String lResponse = DataParseUtil.handleResponse(mBuilder.toString());
            if (!lResponse.equals("1")) {
                return 0;
            }
        } catch (Exception e) {
            return -1;
        }
        return 1;
    }
