    protected boolean checkpoint() {
        boolean result;
        int http_rc;
        int i;
        String checkpoint_name;
        String checkpoint_url_str;
        MiG.oneclick.File file;
        URL url;
        HttpsURLConnection httpsUrlConn;
        ObjectOutputStream oos;
        OutputStreamWriter osw;
        this.checkpoint_id++;
        try {
            this.cleanup_filelist();
            for (i = 0; i < this.files.size(); i++) {
                file = (File) this.files.elementAt(i);
                if (file.getFlushCount() > 0) {
                    checkpoint_name = this.jobid + "." + file.getFilename() + ".checkpoint." + this.checkpoint_id;
                    checkpoint_url_str = this.server + "/cgi-sid/cp.py?iosessionid=" + this.iosessionid + "&src=" + file.getFilename() + "&dst=" + checkpoint_name;
                    System.out.println("checkpoint_url_str: " + checkpoint_url_str);
                    url = new URL(checkpoint_url_str);
                    httpsUrlConn = (HttpsURLConnection) url.openConnection();
                    httpsUrlConn.setRequestMethod("GET");
                    httpsUrlConn.connect();
                    http_rc = httpsUrlConn.getResponseCode();
                    System.out.println("New File checkpoint: '" + file.getFilename() + "', rc: " + http_rc);
                    httpsUrlConn.disconnect();
                }
            }
            checkpoint_url_str = this.server + "/sid_redirect/" + this.iosessionid + "/";
            checkpoint_name = this.jobid + "." + this.getClass().getName() + ".checkpoint";
            url = new URL(checkpoint_url_str + checkpoint_name + "." + this.checkpoint_id);
            httpsUrlConn = (HttpsURLConnection) url.openConnection();
            httpsUrlConn.setRequestMethod("PUT");
            httpsUrlConn.setDoOutput(true);
            httpsUrlConn.connect();
            oos = new ObjectOutputStream(httpsUrlConn.getOutputStream());
            oos.writeObject(this);
            oos.flush();
            http_rc = httpsUrlConn.getResponseCode();
            httpsUrlConn.disconnect();
            url = new URL(checkpoint_url_str + checkpoint_name + ".latest");
            httpsUrlConn = (HttpsURLConnection) url.openConnection();
            httpsUrlConn.setRequestMethod("PUT");
            httpsUrlConn.setDoOutput(true);
            httpsUrlConn.connect();
            osw = new OutputStreamWriter(httpsUrlConn.getOutputStream());
            osw.write(String.valueOf(this.checkpoint_id));
            osw.flush();
            httpsUrlConn.disconnect();
            http_rc = httpsUrlConn.getResponseCode();
            System.out.println("New object checkpoint, rc: " + http_rc);
            result = true;
        } catch (java.lang.Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }
