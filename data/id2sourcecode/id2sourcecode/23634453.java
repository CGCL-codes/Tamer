    public void loadData() throws IOException {
        int start = this.read("start");
        int end = this.read("end");
        String line = "";
        String mc = "", sm = "", ly = "";
        String regex = "<[^>]*>";
        for (int i = start; i <= end; i++) {
            try {
                String url = "http://www.pharmnet.com.cn/search/detail--32--" + i + ".html";
                LineIterator iterator = IOUtils.lineIterator(loadSource(url), "GB2312");
                while (iterator.hasNext()) {
                    line = iterator.nextLine();
                    String replaceStr = "<span style=\"font-size:14px; color:#444444; line-height:180%;\">";
                    if (StringUtils.containsIgnoreCase(line, "/images/menu_ico.gif")) {
                        mc = line.replaceAll(regex, "").trim();
                    }
                    if (StringUtils.containsIgnoreCase(line, "<td width=\"25%\">")) {
                        ly = line.replaceAll(regex, "").trim();
                    }
                    if (StringUtils.containsIgnoreCase(line, replaceStr)) {
                        sm = line.replaceAll(replaceStr, "").replaceAll("<br><br></span></td>", "").trim();
                    }
                }
                dao.add(mc, sm, ly);
                System.out.println(i + " : " + mc);
            } catch (Exception e) {
                e.printStackTrace();
                this.write("error", this.read("error") + "," + i);
                this.write("start", "" + i);
            }
        }
        this.write("start", (this.read("end") + 1) + "");
        System.out.println("End with: " + this.read("end"));
    }
