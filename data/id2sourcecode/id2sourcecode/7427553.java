    public void setDisplayOptions() {
        URL url;
        HttpURLConnection conn;
        String cookie;
        try {
            url = new URL("http://cri-srv-ade.insa-toulouse.fr:8080/ade/custom/modules/plannings/plannings.jsp?isClickable=true&showTabStage=true&showTabRooms=true&showTabDuration=true&displayConfId=1&showTabHour=true&showTabInstructors=true&showTabActivity=true&showPianoDays=true&y=&showTreeCategory8=true&x=&showTab=true&displayType=0&showTreeCategory7=true&showTreeCategory6=true&showTreeCategory5=true&showTreeCategory4=true&showTreeCategory3=true&showTreeCategory2=true&showTabTrainees=true&showTreeCategory1=true&showTabCategory8=true&showTabCategory7=true&showTabCategory6=true&showTabCategory5=true&showPianoWeeks=true&display=true&showTabDate=true&showLoad=false&showTabResources=true&changeOptions=true");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Cookie", sessionId);
            cookie = conn.getHeaderField("Set-Cookie");
            displayCookie = cookie.split(";")[0];
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
