    public void downloadItem(YahooInfo legroup) throws Exception {
        String unmd5;
        String str;
        int data;
        URL myurl;
        URLConnection conn;
        InputStream in;
        FileOutputStream out;
        BigInteger hash;
        File lefic;
        File tmpfic = new File("tmpyahoo.jpg");
        File ledir;
        File newmd5 = new File("new.md5");
        MessageDigest md = MessageDigest.getInstance("MD5");
        System.out.print("Downloading images : ");
        for (int i = 0; i < UrlList.size(); i++) {
            if (UrlList.get(i).getGroup().equals(legroup.getGroup()) && UrlList.get(i).getDir().startsWith(legroup.getDir()) && UrlList.get(i).isFile()) {
                myurl = new URL(UrlList.get(i).getUrl());
                conn = myurl.openConnection();
                conn.connect();
                if (!Pattern.matches("HTTP/... 2.. .*", conn.getHeaderField(0).toString())) {
                    System.out.println(conn.getHeaderField(0).toString());
                    return;
                }
                in = conn.getInputStream();
                out = new FileOutputStream(tmpfic);
                md.reset();
                for (data = in.read(); data != -1; data = in.read()) {
                    md.update((byte) data);
                    out.write(data);
                }
                out.close();
                hash = new BigInteger(1, md.digest());
                unmd5 = new String(hash.toString(16));
                for (int j = unmd5.length(); j < 32; j++) unmd5 = "0" + unmd5;
                if (!lesmd5.contains(unmd5)) {
                    str = UrlList.get(i).getGroup() + "/" + unmd5 + UrlList.get(i).getFile();
                    lefic = new File(str);
                    ledir = new File(UrlList.get(i).getGroup());
                    if (!ledir.exists()) ledir.mkdirs();
                    tmpfic.renameTo(lefic);
                    lesmd5.add(unmd5);
                    FileWriter fw = new FileWriter(newmd5, true);
                    fw.write(unmd5 + " " + str + "\n", 0, unmd5.length() + str.length() + 2);
                    fw.close();
                    System.out.print(".");
                } else {
                    System.out.print("D");
                    tmpfic.delete();
                }
            }
        }
        System.out.println("");
    }
