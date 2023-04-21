    private ArrayList getNewUsers() {
        ArrayList users = new ArrayList();
        BufferedReader input = null;
        try {
            URL url = new URL(WebServicesImpl.BASE_WEBSITE_URL + "/admin/regtask.php?pw=kujukudu&action=new");
            input = new BufferedReader(new InputStreamReader(url.openStream()));
            String rec = input.readLine();
            if (rec.indexOf("NONE") == -1) {
                while (rec != null) {
                    users.add(rec);
                    rec = input.readLine();
                }
            }
        } catch (MalformedURLException e) {
            LOG.severe(e.getMessage());
        } catch (IOException e) {
            LOG.severe(e.getMessage());
        } finally {
            try {
                input.close();
            } catch (Exception e) {
            }
        }
        return users;
    }
