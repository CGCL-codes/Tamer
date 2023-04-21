    private Object getCheckpointObject() {
        int checkpoint_nr;
        int mods;
        Boolean filerestore_result;
        String checkpoint_url_str;
        String checkpoint_request_url_str;
        URL checkpoint_obj_url;
        HttpsConnection checkpoint_request_conn;
        HttpsURLConnection httpsUrlConn;
        Class job_class;
        Object job_object;
        Object result;
        ObjectInputStream ois;
        Method restoreCheckpointedFiles_method;
        result = null;
        try {
            checkpoint_url_str = this.server + "/sid_redirect/" + this.iosessionid + "/" + this.jobid + "." + this.execute_cmd + ".checkpoint";
            checkpoint_request_url_str = checkpoint_url_str + ".latest";
            System.out.println("checkpoint_request_url_str: " + checkpoint_request_url_str);
            checkpoint_request_conn = new HttpsConnection(checkpoint_request_url_str);
            checkpoint_request_conn.open();
            checkpoint_nr = -1;
            if (checkpoint_request_conn.getResponseCode() == HttpsConnection.HTTP_OK) {
                checkpoint_nr = Integer.parseInt(checkpoint_request_conn.readLine());
            }
            checkpoint_request_conn.close();
            if (checkpoint_nr != -1) {
                checkpoint_obj_url = new URL(checkpoint_url_str + "." + checkpoint_nr);
                System.out.println("checkpoint obj url: " + checkpoint_obj_url);
                httpsUrlConn = (HttpsURLConnection) checkpoint_obj_url.openConnection();
                httpsUrlConn.connect();
                job_object = (Object) (new ObjectInputStream(httpsUrlConn.getInputStream())).readObject();
                httpsUrlConn.getResponseCode();
                httpsUrlConn.disconnect();
                job_class = job_object.getClass();
                this.getJobMethods(job_class);
                this.setInfo_method.invoke(job_object, new Object[] { this.server, this.iosessionid, this.jobid });
                result = job_object;
                filerestore_result = (Boolean) this.restoreCheckpointedFiles_method.invoke(job_object, new Object[] {});
                if (!filerestore_result.booleanValue()) {
                    result = null;
                }
            }
        } catch (java.lang.Exception e) {
            result = null;
            e.printStackTrace();
        }
        return result;
    }
