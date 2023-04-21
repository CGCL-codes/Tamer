    public boolean getData() {
        try {
            HttpClient client = null;
            client = this.LoginToElearning(0, client);
            if (client == null) {
                Log.i(LOG_CONNECTION, "Problem logging into eLearning");
                return false;
            }
            HttpGet request5 = new HttpGet("https://elearning.utdallas.edu/webct/urw/lc5122011.tp0/viewCalendar.dowebct");
            Log.i(LOG_CONNECTION, "-- about to make call to open the calendar --");
            HttpResponse response5 = client.execute(request5);
            HttpEntity entity5 = response5.getEntity();
            if (entity5 == null) {
                Log.i(LOG_CONNECTION, "error on fifth page response");
                return false;
            }
            String sHTML1 = "";
            sHTML1 = this.parseHtmlFromEntity(entity5);
            int nIndex = sHTML1.indexOf("Key:");
            if (nIndex < 0 && sHTML1.indexOf("<title>Calendar</title>") > 0) {
                Log.i(LOG_CONNECTION, "error on fifth page response - 2");
                return false;
            }
            Log.i(LOG_CONNECTION, "-- successfully got to the calendar --");
            ArrayList<UniversityClass> listClasses = new ArrayList<UniversityClass>();
            listClasses = this.parseUnivClassesFromText(nIndex, sHTML1);
            String sTextOut = "";
            UniversityDataStorage uds = UniversityDataStorage.getSingleton();
            uds.onCreate(uds.getWritableDatabase());
            for (UniversityClass c : listClasses) {
                sTextOut += String.format("prefix: %s num: %s sec: %s sem: %s title: %s\n", c.Prefix, c.ClassNumber, c.ClassSection, c.Semester, c.Title);
                c.UniversityClassId = uds.SaveUniversityClass(c);
            }
            ArrayList<UnivClassEvent> listEvents = new ArrayList<UnivClassEvent>();
            listEvents = parseEventsFromCalendar(listClasses, sHTML1);
            for (UnivClassEvent uce : listEvents) {
                uce.UnivClassEventId = uds.SaveUnivClassEvent(uce);
            }
            ArrayList<BasicNameValuePair> listParams = new ArrayList<BasicNameValuePair>();
            listParams = this.parseNameValueFromText(0, "startMonth", ">", "selected=\"true\" value=\"", "\"", sHTML1, 1);
            int nMonth = Integer.parseInt(listParams.get(0).getValue());
            nMonth = (++nMonth == 13) ? 1 : nMonth;
            Log.d("CALENDAR", "next Month:  calendar:" + listParams.get(0).getValue() + "  nextValue: " + nMonth);
            listParams = this.parseNameValueFromText(0, "startYear", ">", "selected=\"true\" value=\"", "\"", sHTML1, 1);
            int nYear = Integer.parseInt(listParams.get(0).getValue());
            nYear = (nMonth == 1) ? nYear + 1 : nYear;
            Log.d("CALENDAR", "next Year: calendar:" + listParams.get(0).getValue() + "   nextValue:" + nYear);
            listParams = new ArrayList<BasicNameValuePair>();
            listParams.add(new BasicNameValuePair("startHideDay", "1"));
            listParams.add(new BasicNameValuePair("startAllowBlankSelection", "false"));
            listParams.add(new BasicNameValuePair("startAllowBlankTimeSelection", "false"));
            listParams.add(new BasicNameValuePair("startMonth", String.valueOf(nMonth)));
            listParams.add(new BasicNameValuePair("startYear", String.valueOf(nYear)));
            listParams.add(new BasicNameValuePair("start", nYear + "/" + nMonth + "/1 8:30"));
            for (int i = 0; i < listParams.size(); i++) Log.d("CALENDAR", "name: " + listParams.get(i).getName() + "  value: " + listParams.get(i).getValue());
            HttpPost request6 = new HttpPost("https://elearning.utdallas.edu/webct/urw/lc5122011.tp0/viewMonth.dowebct");
            request6.setEntity(new UrlEncodedFormEntity(listParams, HTTP.UTF_8));
            Log.i(LOG_CONNECTION, "--- about to make call to open a different month on the calendar ---");
            HttpResponse response6 = client.execute(request6);
            HttpEntity entity6 = response6.getEntity();
            if (entity6 == null) {
                Log.i(LOG_CONNECTION, "error on opening different month on calendar");
                return false;
            }
            sHTML1 = this.parseHtmlFromEntity(entity6);
            listEvents = new ArrayList<UnivClassEvent>();
            listEvents = parseEventsFromCalendar(listClasses, sHTML1);
            for (UnivClassEvent uce : listEvents) {
                uce.UnivClassEventId = uds.SaveUnivClassEvent(uce);
            }
            uds.updateLastSync();
            this.LogoutElearning(client);
            return true;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.e("GETDATA", "getData() - client protocol exception: " + e.getMessage());
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("GETDATA", "getData() - IO exception: " + e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("GETDATA", "getData() - general exception: " + e.getMessage());
            return false;
        }
    }
