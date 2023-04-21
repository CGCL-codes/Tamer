    public static void main(String args[]) {
        try {
            ChannelScheduleDao v = new ChannelScheduleDao();
            String[][] aTemp = v.getChannelScheduleTime("00000007020180200902");
            if (aTemp != null && aTemp.length > 0) {
                for (int i = 0; i < aTemp.length; i++) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print(e);
        }
    }
