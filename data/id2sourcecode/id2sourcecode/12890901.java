    private void _userAction(String urlAction) {
        try {
            URL url = new URL(PropsUtil.get(PropsUtil.MAIL_HOOK_RHEMS_WEB_SERVER) + PropsUtil.get(PropsUtil.MAIL_HOOK_RHEMS_LOGIN));
            URLConnection conn = url.openConnection();
            HashMap headerHashMap = new HashMap(conn.getHeaderFields());
            if (headerHashMap.containsKey("Set-Cookie")) {
                String cookieValue = headerHashMap.get("Set-Cookie").toString();
                cookieValue = cookieValue.substring(1, cookieValue.indexOf(']'));
                url = new URL(PropsUtil.get(PropsUtil.MAIL_HOOK_RHEMS_WEB_SERVER) + urlAction);
                conn = url.openConnection();
                conn.setRequestProperty("Cookie", cookieValue);
                conn.connect();
                conn.getInputStream().close();
            }
        } catch (MalformedURLException me) {
            me.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
