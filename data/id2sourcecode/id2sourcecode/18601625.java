    public static boolean rebuildMD5UserCode(EboContext ctx) {
        try {
            boObjectList users = boObjectList.list(ctx, "select iXEOUser where id is not null", 999999999, 999999999);
            users.beforeFirst();
            String md5Code;
            boObject user;
            int count = 0;
            String x;
            while (users.next()) {
                user = users.getObject();
                x = user.getAttribute("id").getValueString();
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte bytes[] = md.digest(x.toLowerCase().getBytes());
                user.getAttribute("MD5Code").setValueString(ClassUtils.byteArrayToHexString(bytes));
                user.update();
                count++;
            }
            return true;
        } catch (boRuntimeException e) {
        } catch (Exception e) {
        }
        return false;
    }
