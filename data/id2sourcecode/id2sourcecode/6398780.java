    @Override
    public void login() {
        try {
            loginsuccessful = false;
            url = new URL("http://mediafire.com/");
            conn = url.openConnection();
            ukeycookie = conn.getHeaderField("Set-Cookie");
            ukeycookie = ukeycookie.substring(0, ukeycookie.indexOf(";"));
            NULogger.getLogger().info(ukeycookie);
            content = "login_email=" + URLEncoder.encode(getUsername(), "UTF-8") + "&login_pass=" + URLEncoder.encode(getPassword(), "UTF-8") + "&submit_login.x=0&submit_login.y=0";
            u = new URL("http://www.mediafire.com/dynamic/login.php");
            uc = (HttpURLConnection) u.openConnection();
            uc.setDoOutput(true);
            uc.setRequestProperty("Host", "www.mediafire.com");
            uc.setRequestProperty("Connection", "keep-alive");
            uc.setRequestProperty("Referer", "http://www.mediafire.com/");
            uc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1");
            uc.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            uc.setRequestProperty("Accept-Encoding", "html");
            uc.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
            uc.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
            uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            uc.setRequestProperty("Cookie", ukeycookie);
            uc.setRequestMethod("POST");
            uc.setInstanceFollowRedirects(false);
            pw = new PrintWriter(new OutputStreamWriter(uc.getOutputStream()), true);
            pw.print(content);
            pw.flush();
            pw.close();
            br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String httpResp = br.readLine();
            NULogger.getLogger().info("Getting skey value........");
            Map<String, List<String>> headerFields = uc.getHeaderFields();
            if (headerFields.containsKey("Set-Cookie")) {
                List<String> header = headerFields.get("Set-Cookie");
                for (int i = 0; i < header.size(); i++) {
                    String tmp = header.get(i);
                    if (tmp.contains("skey")) {
                        skeycookie = tmp;
                    }
                    if (tmp.contains("user")) {
                        usercookie = tmp;
                    }
                }
            } else {
                loginsuccessful = false;
                username = "";
                password = "";
                JOptionPane.showMessageDialog(NeembuuUploader.getInstance(), "<html><b>" + HOSTNAME + "</b> " + TranslationProvider.get("neembuuuploader.accounts.loginerror") + "</html>", HOSTNAME, JOptionPane.WARNING_MESSAGE);
                AccountsManager.getInstance().setVisible(true);
                return;
            }
            skeycookie = skeycookie.substring(0, skeycookie.indexOf(";"));
            NULogger.getLogger().info(skeycookie);
            if (usercookie.isEmpty()) {
                loginsuccessful = false;
                username = "";
                password = "";
                NULogger.getLogger().info("MediaFire login not successful");
                JOptionPane.showMessageDialog(NeembuuUploader.getInstance(), "<html><b>" + HOSTNAME + "</b> " + TranslationProvider.get("neembuuuploader.accounts.loginerror") + "</html>", HOSTNAME, JOptionPane.WARNING_MESSAGE);
                AccountsManager.getInstance().setVisible(true);
            } else {
                loginsuccessful = true;
                username = getUsername();
                password = getPassword();
                usercookie = usercookie.substring(0, usercookie.indexOf(";"));
                NULogger.getLogger().info(usercookie);
                NULogger.getLogger().info("MediaFire login success");
            }
            u = null;
            uc = null;
        } catch (Exception ex) {
            NULogger.getLogger().log(Level.SEVERE, "{0}Error in MediaFire Login:- \n {1}", new Object[] { getClass().getName(), ex });
            loginsuccessful = false;
            username = "";
            password = "";
            NeembuuUploaderProperties.setProperty(KEY_USERNAME, "");
            NeembuuUploaderProperties.setEncryptedProperty(KEY_PASSWORD, "");
            NULogger.getLogger().info("MediaFire login not successful");
            JOptionPane.showMessageDialog(NeembuuUploader.getInstance(), "<html><b>" + HOSTNAME + "</b> " + TranslationProvider.get("neembuuuploader.accounts.loginerror") + "</html>", HOSTNAME, JOptionPane.WARNING_MESSAGE);
            AccountsManager.getInstance().setVisible(true);
        }
    }
