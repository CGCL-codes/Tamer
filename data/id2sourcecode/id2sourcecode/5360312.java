    public String getExp(String expinfo) {
        System.out.println(expinfo);
        try {
            String re = "";
            if (!"".equals(expinfo) && expinfo.split(" ").length == 3) {
                expNumber = expinfo.split(" ")[1].trim();
                number = expinfo.split(" ")[2].trim();
            } else {
                return "[FormatNumber error..] 格式错误";
            }
            StringBuffer sb = new StringBuffer("http://www.kuaidi100.com/api?");
            sb.append("id=").append(EX_KEY).append("&");
            sb.append("com=").append(expNumber).append("&");
            sb.append("nu=").append(number.trim()).append("&");
            sb.append("show=").append("3").append("&muti=1");
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
            String str = "";
            json = str = new String(b, "utf8");
            log.info("快递查询 res : " + json);
            return json;
        } catch (Exception e) {
            log.info("快递查询查询异常.... ");
            return "";
        }
    }
