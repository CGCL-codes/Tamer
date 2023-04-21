    protected String doIt() throws Exception {
        int AD_Registration_ID = getRecord_ID();
        log.info("doIt - AD_Registration_ID=" + AD_Registration_ID);
        MSystem sys = MSystem.get(getCtx());
        if (sys.getName().equals("?") || sys.getName().length() < 2) throw new AdempiereUserError("Set System Name in System Record");
        if (sys.getUserName().equals("?") || sys.getUserName().length() < 2) throw new AdempiereUserError("Set User Name (as in Web Store) in System Record");
        if (sys.getPassword().equals("?") || sys.getPassword().length() < 2) throw new AdempiereUserError("Set Password (as in Web Store) in System Record");
        M_Registration reg = new M_Registration(getCtx(), AD_Registration_ID, get_TrxName());
        MLocation loc = null;
        if (reg.getC_Location_ID() > 0) {
            loc = new MLocation(getCtx(), reg.getC_Location_ID(), get_TrxName());
            if (loc.getCity() == null || loc.getCity().length() < 2) throw new AdempiereUserError("No City in Address");
        }
        if (loc == null) throw new AdempiereUserError("Please enter Address with City");
        String enc = WebEnv.ENCODING;
        StringBuffer urlString = new StringBuffer("http://www.adempiere.com").append("/wstore/registrationServlet?");
        urlString.append("Name=").append(URLEncoder.encode(sys.getName(), enc)).append("&UserName=").append(URLEncoder.encode(sys.getUserName(), enc)).append("&Password=").append(URLEncoder.encode(sys.getPassword(), enc));
        if (reg.getDescription() != null && reg.getDescription().length() > 0) urlString.append("&Description=").append(URLEncoder.encode(reg.getDescription(), enc));
        urlString.append("&IsInProduction=").append(reg.isInProduction() ? "Y" : "N");
        if (reg.getStartProductionDate() != null) urlString.append("&StartProductionDate=").append(URLEncoder.encode(String.valueOf(reg.getStartProductionDate()), enc));
        urlString.append("&IsAllowPublish=").append(reg.isAllowPublish() ? "Y" : "N").append("&NumberEmployees=").append(URLEncoder.encode(String.valueOf(reg.getNumberEmployees()), enc)).append("&C_Currency_ID=").append(URLEncoder.encode(String.valueOf(reg.getC_Currency_ID()), enc)).append("&SalesVolume=").append(URLEncoder.encode(String.valueOf(reg.getSalesVolume()), enc));
        if (reg.getIndustryInfo() != null && reg.getIndustryInfo().length() > 0) urlString.append("&IndustryInfo=").append(URLEncoder.encode(reg.getIndustryInfo(), enc));
        if (reg.getPlatformInfo() != null && reg.getPlatformInfo().length() > 0) urlString.append("&PlatformInfo=").append(URLEncoder.encode(reg.getPlatformInfo(), enc));
        urlString.append("&IsRegistered=").append(reg.isRegistered() ? "Y" : "N").append("&Record_ID=").append(URLEncoder.encode(String.valueOf(reg.getRecord_ID()), enc));
        urlString.append("&City=").append(URLEncoder.encode(loc.getCity(), enc)).append("&C_Country_ID=").append(URLEncoder.encode(String.valueOf(loc.getC_Country_ID()), enc));
        if (reg.isAllowStatistics()) {
            urlString.append("&NumClient=").append(URLEncoder.encode(String.valueOf(DB.getSQLValue(null, "SELECT Count(*) FROM AD_Client")), enc)).append("&NumOrg=").append(URLEncoder.encode(String.valueOf(DB.getSQLValue(null, "SELECT Count(*) FROM AD_Org")), enc)).append("&NumBPartner=").append(URLEncoder.encode(String.valueOf(DB.getSQLValue(null, "SELECT Count(*) FROM C_BPartner")), enc)).append("&NumUser=").append(URLEncoder.encode(String.valueOf(DB.getSQLValue(null, "SELECT Count(*) FROM AD_User")), enc)).append("&NumProduct=").append(URLEncoder.encode(String.valueOf(DB.getSQLValue(null, "SELECT Count(*) FROM M_Product")), enc)).append("&NumInvoice=").append(URLEncoder.encode(String.valueOf(DB.getSQLValue(null, "SELECT Count(*) FROM C_Invoice")), enc));
        }
        log.fine(urlString.toString());
        URL url = new URL(urlString.toString());
        StringBuffer sb = new StringBuffer();
        try {
            URLConnection uc = url.openConnection();
            InputStreamReader in = new InputStreamReader(uc.getInputStream());
            int c;
            while ((c = in.read()) != -1) sb.append((char) c);
            in.close();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Connect - " + e.toString());
            throw new IllegalStateException("Cannot connect to Server - Please try later");
        }
        String info = sb.toString();
        log.info("Response=" + info);
        int index = sb.indexOf("Record_ID=");
        if (index != -1) {
            try {
                int Record_ID = Integer.parseInt(sb.substring(index + 10));
                reg.setRecord_ID(Record_ID);
                reg.setIsRegistered(true);
                reg.save();
                info = info.substring(0, index);
            } catch (Exception e) {
                log.log(Level.SEVERE, "Record - ", e);
            }
        }
        return info;
    }
