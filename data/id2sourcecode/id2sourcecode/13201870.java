    public static void main(String[] args) {
        try {
            String re = "";
            String s = "<meta http-equiv='Pragma' content='no-cache'><meta http-equiv='Refresh'content='0;URL=/view/42545465DZWE中文06.htm'>";
            String encoder = URLEncoder.encode("菊花");
            System.out.println(encoder);
            StringBuffer sb = new StringBuffer("http://baike.baidu.com/searchword/?word=%BE%D5%BB%A8&pic=1&sug=1&enc=gbk&oq=post&clk=1&rsp=0");
            URL url = new URL(sb.toString());
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            InputStream is = connect.getInputStream();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buff = new byte[256];
            int rc = 0;
            while ((rc = is.read(buff, 0, 256)) > 0) {
                outStream.write(buff, 0, rc);
            }
            byte[] b = outStream.toByteArray();
            outStream.close();
            is.close();
            connect.disconnect();
            re = new String(b, "utf8");
            System.out.println(re);
            String regEx = "/view/([0-9]+.*).htm";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(re);
            if (m.find()) {
                System.out.println(m.group(0));
                StringBuffer tourlBuffer = new StringBuffer("http://baike.baidu.com");
                tourlBuffer.append(m.group(0));
                URL url2 = new URL(tourlBuffer.toString());
                HttpURLConnection connect2 = (HttpURLConnection) url2.openConnection();
                InputStream is2 = connect2.getInputStream();
                ByteArrayOutputStream outStream2 = new ByteArrayOutputStream();
                byte[] buff2 = new byte[256];
                int rc2 = 0;
                while ((rc2 = is2.read(buff2, 0, 256)) > 0) {
                    outStream2.write(buff2, 0, rc2);
                }
                byte[] b2 = outStream2.toByteArray();
                outStream2.close();
                is2.close();
                connect2.disconnect();
                re = new String(b2, "gbk");
                System.out.println(re);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
