    public Vector getChannelList(String postalCode, String zipCode, String provider, LogCallback callback, boolean debug) throws Exception {
        if (postalCode == null && zipCode == null) {
            throw new Exception("No postalcode or zipcode specified");
        }
        if (provider == null || provider.length() < 1) {
            throw new Exception("No provider specified");
        }
        Vector channels = new Vector();
        String code = postalCode;
        code = (zipCode != null ? zipCode : code);
        try {
            if (callback != null) callback.update("Getting Channels...", null);
            URL url = new URL("http://tvlistings.zap2it.com/system.asp?partner_id=national&zipcode=" + code);
            HTTPConnection con = new HTTPConnection(url);
            NVPair headers[] = new NVPair[5];
            headers[0] = new NVPair("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 1.0.3705)");
            headers[1] = new NVPair("Referer", "http://tvlistings.zap2it.com/system.asp?partner_id=national&zipcode=98103");
            headers[2] = new NVPair("Content-Type", "application/x-www-form-urlencoded");
            headers[3] = new NVPair("Accept", "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/msword, application/x-shockwave-flash, */*");
            headers[4] = new NVPair("Accept-Language", "en-us");
            con.setDefaultHeaders(headers);
            URL regUrl = new URL("http://tvlistings.zap2it.com/zipcode.asp?partner_id=national");
            NVPair regform_data[] = new NVPair[3];
            regform_data[0] = new NVPair("zipcode", code);
            regform_data[1] = new NVPair("partner_id", "national");
            regform_data[2] = new NVPair("FormName", "zipcode.asp");
            HTTPResponse resp = con.Get(regUrl.getPath(), regform_data);
            String content = resp.getText();
            NVPair form_data[] = new NVPair[5];
            form_data[0] = new NVPair("provider", provider);
            form_data[1] = new NVPair("saveProvider", "See Listings");
            form_data[2] = new NVPair("zipCode", code);
            form_data[3] = new NVPair("FormName", "system.asp");
            form_data[4] = new NVPair("page_from", "");
            if (callback != null) callback.update(null, "Trying site " + url.toString());
            resp = con.Post(url.getPath(), form_data);
            content = resp.getText();
            if (resp.getStatusCode() >= 300 || content.indexOf("your session has timed out") != -1) {
                if (callback != null) callback.update(null, "Retrying site " + url.toString());
                resp = con.Post(url.getPath(), form_data);
                content = resp.getText();
            }
            if (resp.getStatusCode() >= 300) {
                if (callback != null) callback.end("zap2it failed to give us a page\n" + "check postal/zip code or www site (maybe their down)", null, true);
                return null;
            }
            if (debug) {
                FileWriter fw = new FileWriter("channels_short.html");
                fw.write(content);
                fw.close();
            }
            URL longUrl = new URL("http://tvlistings.zap2it.com/listings_redirect.asp?partner_id=national");
            form_data = new NVPair[8];
            form_data[0] = new NVPair("spp", "0");
            resp = con.Get(longUrl.getPath(), form_data);
            content = resp.getText();
            if (resp.getStatusCode() >= 300) {
                if (callback != null) callback.end("zap2it failed to give us a page\n" + "check postal/zip code or www site (maybe their down)", null, true);
                return null;
            }
            try {
                Perl5Util util = new Perl5Util();
                if (util.match("m#>(We are sorry, [^<]*)#i", content)) {
                    String error = util.group(1);
                    error = util.substitute("s#\\n# #og", error);
                    error = util.substitute("s#\\s+# #og", error);
                    error = util.substitute("s#^\\s+##og", error);
                    error = util.substitute("s#\\s+$##og", error);
                    if (callback != null) callback.end("Error found...", "ERROR: " + error, false);
                    return null;
                }
                content = util.substitute("s/>\\s*</>\n</g", content);
                if (debug) {
                    FileWriter fw = new FileWriter("channels.html");
                    fw.write(content);
                    fw.close();
                }
                String html = util.substitute("s#<TR#<tr#og", content);
                html = util.substitute("s#</TR#</tr#og", html);
                ParserGetter kit = new ParserGetter();
                HTMLEditorKit.Parser parser = kit.getParser();
                Vector rows = new Vector();
                util.split(rows, "/<tr/", html, Perl5Util.SPLIT_ALL);
                int rowsSize = rows.size();
                for (int rowNumber = 0; rowNumber < rowsSize; rowNumber++) {
                    String row = (String) rows.get(rowNumber);
                    row = util.substitute("s/^[^>]*>//so", row);
                    row = util.substitute("s#</tr>.*##so", row);
                    row = util.substitute("s/[\r\n]+\\s*//og", row);
                    StringReader r = new StringReader(row);
                    ScrapeRow scrapecallback = new ScrapeRow();
                    parser.parse(r, scrapecallback, false);
                    String desc = scrapecallback.summarize();
                    TreeMap nchannel = new TreeMap();
                    if (util.match("m;^<td><img><br><font><b><a><text>([^<]+)</text><br><nobr><text>([^<]+)</text></nobr></a></b></font></td>;", desc) || util.match("m;^<td><img><br><b><a><font><text>([^<]+)</text><br><nobr><text>([^<]+)</text></nobr></font></a></b></td>;", desc)) {
                        nchannel.put(NUMBER, util.group(1));
                        nchannel.put(LETTERS, util.group(2));
                        String ref = scrapecallback.getSRC(2);
                        if (ref == null) {
                            if (callback != null) callback.update(null, "row decode on item 2 failed on '" + desc + "'");
                            return null;
                        } else {
                            nchannel.put(ICON, ref);
                        }
                        int offset = 0;
                        if (util.match("m;^<td><img><br><font><b><a>;", desc)) offset = 6; else if (util.match("m;^<td><img><br><b><a>;", desc)) offset = 5; else if (callback != null) callback.update(null, "coding error finding <a> in '" + desc + "'");
                        ref = scrapecallback.getHREF(offset);
                        if (ref == null) {
                            if (callback != null) callback.update(null, "row decode on item " + offset + " failed on '" + desc + "'");
                            return null;
                        }
                        if (util.match("m;listings_redirect.asp\\?station_num=(\\d+);", ref)) {
                            nchannel.put(STATION, util.group(1));
                        } else {
                            if (callback != null) callback.update(null, "row decode on item " + offset + " href failed on '" + desc + "'");
                            return null;
                        }
                    } else if (util.match("m;^<td><font><b><a><text>([^<]+)</text><br><nobr><text>([^<]+)</text></nobr></a></b></font></td>;", desc) || util.match("m;^<td><b><a><font><text>([^<]+)</text><br><nobr><text>([^<]+)</text></nobr></font></a></b></td>;", desc)) {
                        nchannel.put(NUMBER, util.group(1));
                        nchannel.put(LETTERS, util.group(2));
                        int offset = 0;
                        if (util.match("m;^<td><font><b><a>;", desc)) offset = 4; else if (util.match("m;^<td><b><a>;", desc)) offset = 3; else if (callback != null) callback.update(null, "coding error finding <a> in '" + desc + "'");
                        String ref = scrapecallback.getHREF(offset);
                        if (ref == null) {
                            if (callback != null) callback.update(null, "row decode on item " + offset + " failed on '" + desc + "'");
                            return null;
                        }
                        if (util.match("m;listings_redirect.asp\\?station_num=(\\d+);", ref)) {
                            nchannel.put(STATION, util.group(1));
                        } else {
                            if (callback != null) callback.update(null, "row decode on item " + offset + " href failed on '" + desc + "'");
                            return null;
                        }
                    } else {
                    }
                    if (nchannel.size() > 0) {
                        channels.add(nchannel);
                    }
                }
                int channelsSize = channels.size();
                if (channelsSize < 0) {
                    if (callback != null) callback.end("zap2it gave us a page with no channels", null, true);
                    return null;
                }
                for (int i = 0; i < channelsSize; i++) {
                    Map channel = (Map) channels.get(i);
                    String station = (String) channel.get(STATION);
                    if (channel.containsKey(NUMBER) && channel.containsKey(LETTERS)) {
                        channel.put(DESC, (String) channel.get(NUMBER) + " " + (String) channel.get(LETTERS));
                    } else {
                        String desc = "";
                        if (channel.containsKey(NUMBER)) desc += (String) channel.get(NUMBER);
                        if (channel.containsKey(LETTERS)) desc += (String) channel.get(LETTERS);
                        channel.put(DESC, desc);
                    }
                }
            } catch (MalformedPerl5PatternException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            if (callback != null) callback.end("Bad web address.", e.getMessage(), false);
        } catch (IOException e) {
            if (callback != null) callback.end(e.toString(), null, false);
        } catch (ModuleException e) {
            if (callback != null) callback.end("Error handling request: " + e.getMessage(), null, false);
        } catch (ParseException e) {
            if (callback != null) callback.end("Error parsing Content-Type: ", e.toString(), false);
        }
        if (callback != null) callback.update("Channels retrieved", null);
        return channels;
    }
