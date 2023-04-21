    public void init() {
        super.init();
        try {
            URL url = new URL(this.getCodeBase() + "servlet/isJava?installed=true");
            HttpURLConnection huc = (java.net.HttpURLConnection) url.openConnection();
            huc.setRequestProperty("Cookie", "JSESSIONID=" + this.getParameter("jsessionid"));
            InputStream inS = huc.getInputStream();
            byte[] buffer = new byte[100];
            while (inS.read(buffer) >= 0) {
            }
            inS.close();
            JSObject jso = JSObject.getWindow(this);
            jso.eval("j2seExists('2');");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
