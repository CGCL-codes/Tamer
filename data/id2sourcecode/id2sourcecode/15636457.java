        public void run() {
            String err = "Error executing RemoteIndexJob: " + this.remoteWriterUrl;
            StringBuilder sb = new StringBuilder();
            for (String sUuid : this.uuids) {
                if (sb.length() > 0) sb.append(",");
                sb.append(sUuid);
            }
            try {
                String av = URLEncoder.encode(Val.chkStr(action), "UTF-8");
                String iv = URLEncoder.encode(sb.toString(), "UTF-8");
                String sUrl = this.remoteWriterUrl + "?action=" + av + "&ids=" + iv;
                URL url = new URL(sUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                int code = con.getResponseCode();
                if ((code < 200) || (code >= 300)) {
                    String s = err + " responseCode=" + code;
                    LogUtil.getLogger().severe(s);
                }
            } catch (Exception e) {
                LogUtil.getLogger().log(Level.SEVERE, err, e);
            }
        }
