    @Override
    public void run() {
        Date d = Calendar.getInstance().getTime();
        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dfm.setTimeZone(TimeZone.getTimeZone("Mexico/BajaNorte"));
        this.getBot().sendMessage(this.getChannel(), "太平洋时间(蒂华纳): " + dfm.format(d));
        dfm.setTimeZone(TimeZone.getTimeZone("Mexico/BajaSur"));
        this.getBot().sendMessage(this.getChannel(), "山区时间(奇瓦瓦): " + dfm.format(d));
        dfm.setTimeZone(TimeZone.getTimeZone("Mexico/General"));
        this.getBot().sendMessage(this.getChannel(), "中央时间(墨西哥城): " + dfm.format(d));
        dfm.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        this.getBot().sendMessage(this.getChannel(), "北京时间: " + dfm.format(d));
    }
