    public Result queryOrder() {
        log.debug("-------------------query order start-------------------");
        Result rs = new Result();
        HttpGet httpget = new HttpGet(Constants.QUERY_ORDER_URL);
        StringBuilder responseBody = new StringBuilder();
        BufferedReader br = null;
        try {
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            br = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                responseBody.append(line);
            }
            String msg = Util.removeTagFromHtml(responseBody.toString());
            if (!msg.isEmpty()) {
                int index = msg.indexOf("-->");
                msg = msg.substring(index + 4);
                String[] allInfo = msg.split("！");
                if (allInfo.length > 1) {
                    String usefulInfo = allInfo[1];
                    if (usefulInfo.contains("待支付")) {
                        rs.setState(Result.HAVE_NO_PAY_TICKET);
                        rs.setMsg(usefulInfo);
                    } else if (usefulInfo.contains("取消次数过多")) {
                        rs.setState(Result.CANCEL_TIMES_TOO_MUCH);
                        rs.setMsg(usefulInfo);
                    } else {
                        rs.setMsg(usefulInfo);
                    }
                } else {
                    rs.setState(Result.NO_BOOKED_TICKET);
                    rs.setMsg(msg);
                }
            } else {
                rs.setMsg(msg);
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.debug("-------------------query order end---------------------");
        return rs;
    }
