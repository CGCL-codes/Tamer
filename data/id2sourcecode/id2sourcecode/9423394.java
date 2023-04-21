    public String get(Context context, String channel, String lang, String key) {
        String msg;
        if (channel == null) channel = Mustang.getConf().get("//message/@default-channel");
        if (lang == null) lang = Mustang.getConf().get("//message/@default-language");
        try {
            msg = getChannel(channel, lang).get(key);
            if (msg.indexOf(K.DOLLAR) >= 0 || msg.indexOf(K.SHARP) >= 0) {
                if (context == null) context = WMManager.getInstance().getContext();
                return WMEvaluator.crunch(context, msg);
            } else return msg;
        } catch (Exception e) {
            try {
                String recoverFileName = ConfTool.getGlobalParameter(Mustang.AUTO_CONF_DIR).concat(RECOVERY_FILE_PREFIX).concat(channel);
                if (lang != null && !K.EMPTY.equals(lang)) recoverFileName = recoverFileName.concat(K.UNDERSCORE).concat(lang);
                recoverFileName = recoverFileName.concat(K.PROPERTIES_EXTENSION);
                File f = new File(recoverFileName);
                if (!f.exists()) f.createNewFile();
                Properties p = new Properties();
                p.load(new FileInputStream(f));
                if (p.get(key) == null) FileTool.saveTextFile(f, key + " =\n", true);
            } catch (Throwable t) {
            }
            return NOT_FOUND + key;
        }
    }
