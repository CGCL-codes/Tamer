    public static void main(String[] args) {
        if (args == null) {
            System.out.println("usage ChannelTest channel");
            System.exit(0);
        } else if (args.length == 0) {
            System.out.println("usage ChannelTest channel");
            System.exit(0);
        }
        String chname = args[0];
        System.out.println("chname = " + chname);
        Channel channel = ChannelFactory.defaultFactory().getChannel(chname);
        channel.connectAndWait();
        String id = channel.getId();
        System.out.println("id = " + id);
        try {
            double[] value = channel.getArrDbl();
            for (int i = 0; i < value.length; i++) {
                System.out.println("value[" + i + "] = " + value[i]);
            }
            double dvalue = channel.getValDbl();
            System.out.println("dvalue = " + dvalue);
            Date d = channel.getTimeRecord().getTimestamp().getDate();
            System.out.println("d = " + d);
            System.out.println("milliseconds " + d.getTime());
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (GetException e) {
            e.printStackTrace();
        }
        channel.disconnect();
    }
