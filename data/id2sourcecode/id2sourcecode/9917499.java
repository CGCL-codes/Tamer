    private void sendMessageToYouMi() {
        int code = -1;
        try {
            code = AndroidUtil.getChannelId(this);
            Class clazz = Class.forName("net.youmi.activate.Counter");
            Method method = clazz.getDeclaredMethod("asyncActivate", Context.class, int.class);
            method.invoke(null, this, code);
            Log.d("Youmi", "channelId:" + code + "发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("YoumiError", "channelId:" + code + "发送失败");
        }
    }
