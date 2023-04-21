    public StatCheckEvent doCheck() {
        StatCheckEvent result = null;
        final PluginInterface pi = BoxTorrentsPlugin.getPluginInterface();
        final LocaleUtilities loc = BoxTorrentsPlugin.getLocaleUtil();
        final PluginConfig pconfig = pi.getPluginconfig();
        final LoggerChannel log = BoxTorrentsPlugin.getLog();
        String strUrl = pconfig.getPluginStringParameter(PluginParamConstants.PR_USERPAGE, "").trim();
        if (strUrl.equals("") || strUrl.equals("http://www.bakabt.com/user/someid/yournick.html")) {
            return null;
        }
        try {
            URL url = new URL(strUrl);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setUseCaches(false);
            http.setRequestProperty("user-agent", "azboxtorrents/" + pi.getPluginVersion() + " Azureus/" + pi.getAzureusVersion());
            http.connect();
            int respcode = http.getResponseCode();
            if (respcode != 200) {
                log.logAlert(LoggerChannel.LT_ERROR, "Connection error, code " + respcode + "\n" + http.getResponseMessage() + "\n" + strUrl);
            } else {
                final BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
                String s = null, upload = null, download = null, ratio = null;
                Set<TorrentData> torrent = new HashSet<TorrentData>();
                while ((s = br.readLine()) != null) {
                    if (s.indexOf("Uploaded") > 0 && upload == null) {
                        s = br.readLine().trim();
                        upload = Util.substringBetween(s, ">", "<");
                    } else if (s.indexOf("Downloaded") > 0 && download == null) {
                        s = br.readLine().trim();
                        download = Util.substringBetween(s, ">", "<");
                    } else if (s.indexOf("Share ratio") > 0 && ratio == null) {
                        br.readLine();
                        s = br.readLine().trim();
                        ratio = Util.substringBetween(s, ">", "<");
                    } else if (s.indexOf("<a class=\"title\"") > 0) {
                        TorrentData td = new TorrentData();
                        String tmp;
                        tmp = Util.substringBetween(s, "\">", "</a>");
                        td.setName(tmp);
                        if (torrent.contains(td)) continue;
                        tmp = Util.substringBetween(s, "href=\"", "\"");
                        try {
                            td.setUrl(new URL("http://www.bakabt.com" + tmp));
                        } catch (Exception e) {
                        }
                        do {
                            s = br.readLine();
                        } while (s.indexOf("<td class=\"peers\"") < 0);
                        s = br.readLine();
                        tmp = Util.substringBetween(s, "\">", "</a>");
                        td.setNumSeeder(Integer.parseInt(tmp));
                        int i = s.lastIndexOf("<a href");
                        i = s.indexOf("\">", i) + 2;
                        int j = s.indexOf("</a>", i);
                        tmp = s.substring(i, j);
                        td.setNumLeecher(Integer.parseInt(tmp));
                        s = br.readLine();
                        tmp = Util.substringBetween(s, "size\">", "</td>");
                        td.setSize(tmp);
                        s = br.readLine();
                        tmp = Util.substringBetween(s, "transfer_up\">", "</td>");
                        td.setUploaded(tmp);
                        s = br.readLine();
                        tmp = Util.substringBetween(s, "transfer_down\">", "</td>");
                        td.setDownloaded(tmp);
                        s = br.readLine();
                        tmp = Util.substringBetween(s, ";\">", "</span>");
                        td.setRatio(tmp);
                        torrent.add(td);
                    }
                }
                br.close();
                if (upload != null && download != null && ratio != null) {
                    float frat = Float.parseFloat(ratio);
                    int rat = Math.round(frat * 100);
                    Date date = new Date(http.getDate());
                    result = new StatCheckEvent(date, -1, pi, upload, download, frat);
                    result.setTorrents(torrent);
                    if (rat <= pconfig.getPluginIntParameter(PluginParamConstants.PR_RATIO_DANGER, 100)) {
                        result.setEventType(StatCheckEvent.EVENT_DANGER);
                    } else if (rat <= pconfig.getPluginIntParameter(PluginParamConstants.PR_RATIO_WARN, 150)) {
                        result.setEventType(StatCheckEvent.EVENT_WARNING);
                    } else {
                        result.setEventType(StatCheckEvent.EVENT_NORMAL);
                    }
                    lastData = result;
                } else log.logAlert(LoggerChannel.LT_ERROR, loc.localise("message.gatherer.statpage.malformed") + strUrl);
            }
            http.disconnect();
        } catch (Exception e) {
            log.logAlert(loc.localise("message.gatherer.statpage.exception") + e.toString(), e);
            result = null;
        }
        return result;
    }
