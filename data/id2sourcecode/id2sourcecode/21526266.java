    public boolean restoreCheckpointedFiles() {
        boolean result;
        int http_rc;
        int i;
        String checkpoint_name;
        String checkpoint_url_str;
        MiG.oneclick.File file;
        URL url;
        HttpsURLConnection httpsUrlConn;
        result = false;
        try {
            for (i = 0; i < this.files.size(); i++) {
                file = (File) files.elementAt(i);
                if (file.getMode() != File.CLOSED) file.setIOsessionid(this.iosessionid);
                if (file.getFlushCount() > 0) {
                    checkpoint_name = this.jobid + "." + file.getFilename() + ".checkpoint." + this.checkpoint_id;
                    checkpoint_url_str = this.server + "/cgi-sid/cp.py?iosessionid=" + this.iosessionid + "&src=" + checkpoint_name + "&dst=" + file.getFilename();
                    System.out.println("restore checkpoint_url_str: " + checkpoint_url_str);
                    url = new URL(checkpoint_url_str);
                    httpsUrlConn = (HttpsURLConnection) url.openConnection();
                    httpsUrlConn.setRequestMethod("GET");
                    httpsUrlConn.connect();
                    http_rc = httpsUrlConn.getResponseCode();
                    System.out.println("Restore File checkpoint copy: '" + file.getFilename() + "', rc: " + http_rc);
                    httpsUrlConn.disconnect();
                }
            }
            result = true;
        } catch (java.lang.Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }
