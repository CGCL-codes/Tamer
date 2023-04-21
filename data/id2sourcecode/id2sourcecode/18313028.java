    public void run() {
        String ips = this.getArgs()[2];
        String country;
        String area;
        area = getArea(ips);
        country = getCountry(ips);
        this.getBot().sendAction(this.getChannel(), "知道 " + this.getArgs()[1] + " 来自 国家： " + country + ", 地区：" + area);
    }
