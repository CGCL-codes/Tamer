    public void run() {
        try {
            app_path = AppPath.get();
            URL url;
            if (AppConst.DIST.equals("exe")) url = new URL("http://champions.zarzu.ch/autoupdate_after_0_4/log.txt"); else if (AppConst.DIST.equals("jar5") && !AppConst.not_app) url = new URL("http://champions.zarzu.ch/autoupdate_after_0_4/log_jar5.txt"); else url = new URL("http://champions.zarzu.ch/autoupdate_after_0_4/log_jar6.txt");
            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String input = "";
            while (br.ready()) {
                input += br.readLine() + ",";
            }
            br.close();
            is.close();
            isr.close();
            System.out.println("http://champions.zarzu.ch" + input.split("[,]")[1]);
            download_link = "http://champions.zarzu.ch" + input.split(",")[2];
            String cur_version = AppConst.VERSION;
            if (!cur_version.equals(input.split("[,]")[0]) && AppConst.DIST.equals("exe")) {
                url = new URL("http://champions.zarzu.ch" + input.split("[,]")[1]);
                OutputStream os = null;
                URLConnection url_con = null;
                new File(app_path + "updates").mkdir();
                user_interface.setTopText(sys_link.translate("newer_version").replace("(1)", input.split("[,]")[0]), 0);
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                }
                try {
                    byte[] buf;
                    int ByteRead, ByteWritten = 0;
                    os = new BufferedOutputStream(new FileOutputStream(app_path + "updates/new.zip"));
                    url_con = url.openConnection();
                    is = url_con.getInputStream();
                    buf = new byte[size];
                    Double percentage;
                    while ((ByteRead = is.read(buf)) != -1) {
                        os.write(buf, 0, ByteRead);
                        ByteWritten += ByteRead;
                        percentage = new Double(ByteWritten) / new Double(url_con.getContentLength()) * 100;
                        user_interface.setTopText(sys_link.translate("down_percentage").replace("(1)", Integer.toString(percentage.intValue())), 0);
                    }
                    os.close();
                    user_interface.setTopText(sys_link.translate("click_update").replace("(1)", input.split("[,]")[0]), 1);
                } catch (Exception e) {
                    user_interface.setTopText(sys_link.translate("click_download").replace("(1)", input.split("[,]")[0]), 2);
                }
            } else if (!cur_version.equals(input.split("[,]")[0])) {
                user_interface.setTopText(sys_link.translate("click_download").replace("(1)", input.split("[,]")[0]), 2);
            }
        } catch (MalformedURLException e) {
            sys_link.writeError("autoupdater", e);
            e.printStackTrace();
        } catch (IOException e) {
            sys_link.writeError("autoupdater", e);
            e.printStackTrace();
        }
    }
