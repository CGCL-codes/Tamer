    public static List<Map<String, String>> getChannels(String id) {
        System.out.println("Oracle Connection");
        Connection conn = null;
        String url = "jdbc:oracle:thin:@10.8.0.160:1521:fms";
        String driver = "oracle.jdbc.driver.OracleDriver";
        String userName = "fcm";
        String password = "fcm";
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, userName, password);
            System.out.println("Connected to the database");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT J.BIZ_JOB jobname, J.CHANNEL_NAME name, J.CHANNEL_URL startURL, J.SOURCE_ID id, S.SOURCE_URL url FROM JOB_INFO J left join SOURCE_INFO S on J.SOURCE_ID = S.SOURCE_ID WHERE J.VALID=1 AND J.SOURCE_ID IN (4762,4497,4481,4740,4749,4755,4754,4473,4758,4747,4476,6472,6461,6418,4993,4991,4900,4989,4988,4845,4842,4841,4839,4501,4466,4020,17562,6417,5689,5688,4843,4797,4760,4756,4750,4499,4475,4465,4745,4752,6310,4492,4373,4763,10640,5688,6488,5050,5721,17568,20831)");
            List<Map<String, String>> ret = listFromRS(rs);
            conn.close();
            System.out.println("Disconnected from database");
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
