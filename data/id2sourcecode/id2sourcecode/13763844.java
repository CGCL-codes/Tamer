    @SuppressWarnings("unchecked")
    private void setcache(String cname, String cachename, String prefix, Map<String, String> settings) throws Exception {
        prefix = prefix != null ? prefix : "cache_";
        boolean flag = false;
        String[] cnames = cname.split(",");
        for (String obj : cnames) {
            String cols = null;
            String table = null;
            String sql = null;
            String conditions = null;
            if ("settings".equals(obj)) {
                conditions = "WHERE variable NOT IN ('siteuniqueid', 'mastermobile', 'closedreason', 'creditsnotify', 'backupdir', 'custombackup','maxonlines', 'newsletter')";
            } else if ("forumlinks".equals(obj) || "onlinelist".equals(obj)) {
                conditions = "ORDER BY displayorder";
            } else if ("forums".equals(obj)) {
                Map<String, Map<String, String>> datas = new HashMap<String, Map<String, String>>();
                List<Map<String, String>> forumList = dataBaseDao.executeQuery("SELECT f.fid, f.type, f.name, f.fup, ff.viewperm FROM " + tablepre + "forums f LEFT JOIN " + tablepre + "forumfields ff ON ff.fid=f.fid LEFT JOIN " + tablepre + "access a ON a.fid=f.fid AND a.allowview='1' WHERE f.status>0 AND f.type<>'group' ORDER BY f.type, f.displayorder");
                if (forumList != null && forumList.size() > 0) {
                    Map<String, Map<String, String>> forums = new HashMap<String, Map<String, String>>();
                    Map<String, Map<String, String>> subs = new HashMap<String, Map<String, String>>();
                    for (Map<String, String> forum : forumList) {
                        forum.put("name", Common.strip_tags(forum.get("name")));
                        String type = forum.get("type");
                        if (type.equals("forum")) {
                            forums.put(forum.get("fid"), forum);
                        } else if (type.equals("sub")) {
                            Map<String, String> upforum = forums.get(forum.get("fup"));
                            if (upforum != null) {
                                if (upforum.get("hasSub") == null) {
                                    upforum.put("hasSub", "true");
                                }
                            }
                            subs.put(forum.get("fid"), forum);
                        }
                    }
                    if (forums != null && forums.size() > 0) {
                        Set<String> forumids = forums.keySet();
                        for (String forumid : forumids) {
                            Map<String, String> forum = forums.get(forumid);
                            boolean hasSub = "true".equals(forum.get("hasSub"));
                            forum.remove("hasSub");
                            datas.put(forumid, forum);
                            if (hasSub) {
                                Set<String> subids = subs.keySet();
                                for (String subid : subids) {
                                    Map<String, String> sub = subs.get(subid);
                                    if (sub.get("fup").equals(forumid)) {
                                        datas.put(subid, sub);
                                    }
                                }
                            }
                        }
                    }
                    forums = null;
                    subs = null;
                }
                forumList = null;
                Map<String, String> data = new HashMap<String, String>();
                data.put("forums", dataParse.combinationChar(datas));
                datas = null;
                writeToCacheFile(prefix + cachename, arrayeval(cachename, data), "", flag);
                data = null;
                break;
            } else if ("plugins".equals(obj)) {
                List<Map<String, String>> plugins = dataBaseDao.executeQuery("SELECT pluginid, available, adminid, name, identifier, datatables, directory, copyright, modules FROM " + tablepre + "plugins");
                if (plugins != null && plugins.size() > 0) {
                    for (Map<String, String> plugin : plugins) {
                        List<Map<String, String>> queryvars = dataBaseDao.executeQuery("SELECT variable, value FROM " + tablepre + "pluginvars WHERE pluginid='" + plugin.get("pluginid") + "'");
                        if (queryvars != null && queryvars.size() > 0) {
                            Map<String, String> vars = new HashMap<String, String>();
                            for (Map<String, String> var : queryvars) {
                                vars.put(var.get("variable"), var.get("value"));
                            }
                            plugin.put("vars", dataParse.combinationChar(vars));
                        }
                        writeToCacheFile("plugin_" + plugin.get("identifier"), arrayeval(plugin.get("identifier"), plugin), "", flag);
                    }
                }
                break;
            } else if (obj.startsWith("tags_")) {
                table = "tags";
                int taglimit = Common.toDigit("viewthread".equals(obj.substring(5)) ? settings.get("viewthreadtags") : settings.get("hottags"));
                cols = "tagname, total";
                conditions = "WHERE closed=0 ORDER BY total DESC LIMIT " + taglimit;
            } else if ("announcements".equals(obj)) {
                int timestamp = Common.time();
                conditions = "WHERE starttime<='" + timestamp + "' AND (endtime>='" + timestamp + "' OR endtime='0') ORDER BY displayorder, starttime DESC, id DESC";
            } else if ("google".equals(obj)) {
                table = "settings";
                conditions = "WHERE variable = 'google'";
            } else if ("baidu".equals(obj)) {
                table = "settings";
                conditions = "WHERE variable = 'baidu'";
            } else if ("birthdays_index".equals(obj)) {
                table = "members";
                int timeoffset = (int) (Float.valueOf(settings.get("timeoffset")) * 3600);
                conditions = "WHERE RIGHT(bday, 5)='" + Common.gmdate("MM-dd", Common.time() + timeoffset) + "' ORDER BY bday LIMIT " + Common.toDigit(settings.get("maxbdays"));
            } else if ("styles".equals(obj)) {
                table = "stylevars";
                sql = "SELECT sv.* FROM " + tablepre + "stylevars sv LEFT JOIN " + tablepre + "styles s ON s.styleid = sv.styleid AND (s.available=1 OR s.styleid='0')";
            } else if ("icons".equals(obj)) {
                table = "smilies";
                conditions = "WHERE type='icon' ORDER BY displayorder";
            } else if ("secqaa".equals(obj)) {
                Random rand = new Random();
                int secqaanum = Integer.valueOf(dataBaseDao.executeQuery("SELECT COUNT(*) count FROM jrun_itempool").get(0).get("count"));
                int start_limit = secqaanum <= 10 ? 0 : rand.nextInt(secqaanum - 10);
                List<Map<String, String>> secqaas = dataBaseDao.executeQuery("SELECT question, answer FROM jrun_itempool LIMIT " + start_limit + ", 10");
                Map<String, String> datas = new HashMap<String, String>();
                int size = secqaas.size();
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        Map<String, String> secqaa = secqaas.get(i);
                        datas.put(String.valueOf(i), dataParse.combinationChar(secqaa));
                    }
                } else {
                    datas.put("0", null);
                }
                while ((size = datas.size()) < 10) {
                    datas.put(size + "", datas.get(rand.nextInt(size) + ""));
                }
                writeToCacheFile(prefix + cachename, arrayeval(obj, datas), "", flag);
                datas = null;
                break;
            } else if ("medals".equals(obj)) {
                table = "medals";
                cols = "medalid, name, image";
                conditions = "WHERE available='1'";
            } else if ("censor".equals(obj)) {
                table = "words";
            } else if ("faqs".equals(obj)) {
                conditions = "WHERE identifier!='' AND keyword!=''";
            } else if ("announcements_forum".equals(obj)) {
                int timestamp = Common.time();
                table = "announcements";
                conditions = "WHERE type!=2 AND groups = '' AND starttime<='" + timestamp + "' ORDER BY displayorder, starttime DESC, id DESC LIMIT 1";
            } else if ("globalstick".equals(obj)) {
                table = "forums";
                conditions = "WHERE status>0 AND type IN ('forum', 'sub') ORDER BY type";
            } else if ("bbcodes".equals(obj)) {
                table = "bbcodes";
                conditions = "WHERE available='1' AND icon!=''";
            } else if ("ranks".equals(obj)) {
                table = "ranks";
                cols = "ranktitle, postshigher, stars, color";
                conditions = "ORDER BY postshigher DESC";
            } else if ("bbcodes_display".equals(obj)) {
                table = "bbcodes";
                conditions = "WHERE available='1' AND icon!=''";
            } else if ("smilies_display".equals(obj)) {
                table = "imagetypes";
                conditions = "WHERE type='smiley' ORDER BY displayorder";
            } else if ("smilies".equals(obj)) {
                table = "smilies";
                sql = "SELECT s.* FROM " + tablepre + "smilies s LEFT JOIN " + tablepre + "imagetypes t ON t.typeid=s.typeid WHERE s.type='smiley' AND s.code<>'' AND t.typeid IS NOT NULL ORDER BY LENGTH(s.code) DESC";
            } else if ("profilefields".equals(obj)) {
                table = "profilefields";
                cols = "fieldid, invisible, title, description, required, unchangeable, selective, choices";
                conditions = "WHERE available='1' ORDER BY displayorder";
            } else if (obj.length() > 5 && obj.substring(0, 5).equals("advs_")) {
                Map<String, String> datas = new HashMap<String, String>();
                Map advs = advertisement(obj.substring(5));
                datas.put(obj.substring(0, 4), advs != null && advs.size() > 0 ? dataParse.combinationChar(advs) : "");
                advs = null;
                writeToCacheFile(prefix + cachename, arrayeval(obj.substring(0, 4), datas), "", flag);
                datas = null;
                flag = true;
                continue;
            } else if ("smilies_var".equals(cachename)) {
                String smrows = settings.get("smrows");
                String smcols = settings.get("smcols");
                int spp = Common.toDigit(smcols) * Common.toDigit(smrows);
                List<Map<String, String>> imagetypes = dataBaseDao.executeQuery("select typeid,name,directory from " + tablepre + "imagetypes order by displayorder");
                if (imagetypes != null && imagetypes.size() > 0) {
                    StringBuffer return_type = new StringBuffer("var smthumb = '20';var smilies_type = new Array();");
                    StringBuffer return_datakey = new StringBuffer("var smilies_array = new Array();");
                    for (Map<String, String> stypes : imagetypes) {
                        List<Map<String, String>> smileslist = dataBaseDao.executeQuery("SELECT id, code, url FROM " + tablepre + "smilies WHERE type='smiley' AND code<>'' AND typeid='" + stypes.get("typeid") + "' ORDER BY displayorder");
                        if (smileslist != null && smileslist.size() > 0) {
                            int j = 1;
                            int i = 0;
                            return_type.append("smilies_type[" + stypes.get("typeid") + "] = ['" + stypes.get("name").replace("'", "\\'") + "','" + stypes.get("directory").replace("'", "\\'") + "'];");
                            return_datakey.append("smilies_array[" + stypes.get("typeid") + "] = new Array();");
                            return_datakey.append("smilies_array[" + stypes.get("typeid") + "][" + j + "]=[");
                            for (Map<String, String> smiles : smileslist) {
                                if (i >= spp) {
                                    return_datakey.deleteCharAt(return_datakey.length() - 1);
                                    return_datakey.append("];");
                                    j++;
                                    return_datakey.append("smilies_array[" + stypes.get("typeid") + "][" + j + "]=[");
                                    i = 0;
                                }
                                i++;
                                String smileycode = smiles.get("code").replace("'", "\\'");
                                String url = smiles.get("url").replace("'", "\\'");
                                int windth = 20;
                                URL imgurl = Thread.currentThread().getContextClassLoader().getResource("../../images/smilies/" + stypes.get("directory") + "/" + url);
                                if (imgurl != null) {
                                    String path = Common.decode(imgurl.getPath());
                                    File file = new File(path);
                                    if (file.exists()) {
                                        windth = ImageIO.read(file).getWidth();
                                    }
                                }
                                return_datakey.append("['" + smiles.get("id") + "','" + smileycode + "','" + url + "','20','20','" + windth + "'],");
                            }
                            return_datakey.deleteCharAt(return_datakey.length() - 1);
                            return_datakey.append("];");
                        }
                        smileslist = null;
                    }
                    imagetypes = null;
                    writeToJsCacheFile("smilies", return_type.toString() + return_datakey.toString(), "_var");
                }
                break;
            } else if ("usergroups".equals(obj)) {
                sql = "SELECT * FROM " + tablepre + "usergroups u LEFT JOIN " + tablepre + "admingroups a ON u.groupid=a.admingid";
            }
            this.getDataList(sql, table != null ? table : obj, cols, conditions, cachename, obj, prefix, flag);
            flag = true;
        }
        if ("threadtypes".equals(cachename)) {
            Map<String, String> datas = new HashMap<String, String>();
            List<Map<String, String>> dataList = dataBaseDao.executeQuery("SELECT t.typeid, tt.optionid, tt.title, tt.type, tt.rules, tt.identifier, tt.description, tv.required, tv.unchangeable, tv.search FROM " + tablepre + "threadtypes t LEFT JOIN " + tablepre + "typevars tv ON t.typeid=tv.typeid	LEFT JOIN " + tablepre + "typeoptions tt ON tv.optionid=tt.optionid WHERE t.special='1' AND tv.available='1' ORDER BY tv.displayorder");
            Map<Integer, Map<Integer, Map<String, String>>> typelists = new HashMap<Integer, Map<Integer, Map<String, String>>>();
            Map<Integer, String> templatedata = new HashMap<Integer, String>();
            if (dataList != null && dataList.size() > 0) {
                Map<Integer, Map<String, String>> typelist = null;
                for (Map<String, String> data : dataList) {
                    Map<String, String> rules = dataParse.characterParse(data.get("rules"), false);
                    Integer typeid = Integer.valueOf(data.get("typeid"));
                    String type = data.get("type");
                    typelist = typelists.get(typeid);
                    if (typelist == null) {
                        typelist = new HashMap<Integer, Map<String, String>>();
                    }
                    if (rules != null && rules.size() > 0) {
                        if ("select".equals(type) || "checkbox".equals(type) || "radio".equals(type)) {
                            String[] choices = rules.get("choices").split("(\r\n|\n|\r)");
                            StringBuffer temp = new StringBuffer();
                            for (String choice : choices) {
                                String[] items = choice.split("=");
                                if (items.length == 2) {
                                    temp.append("," + items[1].trim());
                                }
                            }
                            data.put("choices", temp.length() > 0 ? temp.substring(1).replaceAll(",", "\\\\n") : "");
                        } else if ("text".equals(type) || "textarea".equals(type)) {
                            data.put("maxlength", rules.get("maxlength"));
                        } else if ("image".equals(type)) {
                            data.put("maxwidth", rules.get("maxwidth"));
                            data.put("maxheight", rules.get("maxheight"));
                        } else if ("number".equals(type)) {
                            data.put("maxnum", rules.get("maxnum"));
                            data.put("minnum", rules.get("minnum"));
                        }
                    }
                    data.remove("rules");
                    typelist.put(Integer.valueOf(data.get("optionid")), data);
                    typelists.put(typeid, typelist);
                }
            }
            dataList = dataBaseDao.executeQuery("SELECT typeid, template FROM " + tablepre + "threadtypes WHERE special='1'");
            if (dataList != null && dataList.size() > 0) {
                for (Map<String, String> data : dataList) {
                    templatedata.put(Integer.valueOf(data.get("typeid")), data.get("template"));
                }
            }
            dataList = null;
            Set<Integer> typeids = typelists.keySet();
            for (Integer typeid : typeids) {
                datas.put("dtype", dataParse.combinationChar(typelists.get(typeid)));
                datas.put("dtypeTemplate", templatedata.get(typeid));
                this.writeToCacheFile(String.valueOf(typeid), arrayeval("threadtype", datas), "threadtype_", false);
            }
            datas = null;
            typeids = null;
            templatedata = null;
            typelists = null;
        }
    }
