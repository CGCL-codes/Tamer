    public static boolean runCard(boolean swiped, String track1, String track2, String number, String expdate, int amount) {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("x_login=XXXXXXXXXXXX&");
            sb.append("x_tran_key=XXXXXXXXXXXX&");
            sb.append("x_cpversion=1.0&");
            sb.append("x_test_request=FALSE&");
            sb.append("x_market_type=2&");
            sb.append("x_device_type=5&");
            sb.append("x_amount=").append(String.format("%.2f", (float) amount / 100)).append("&");
            sb.append("x_response_format=1&");
            sb.append("x_delim_char=|&");
            sb.append("x_relay_response=FALSE&");
            if (swiped) {
                if (track1 != null) sb.append("x_track1=").append(track1).append("&");
                if (track2 != null) sb.append("x_track2=").append(track2).append("&");
            } else {
                sb.append("x_card_num=").append(number).append("&");
                sb.append("x_exp_date=").append(expdate).append("&");
            }
            sb.append("x_user_ref=ZonePOS Transaction&");
            URL url = new URL("https://test.authorize.net/gateway/transact.dll");
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(sb.toString().getBytes());
            out.flush();
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            line = in.readLine();
            in.close();
            System.err.println(line);
            Vector ccrep = split("|", line);
            System.out.print("Response Code: ");
            System.out.println(ccrep.elementAt(0));
            System.out.print("Human Readable Response Code: ");
            System.out.println(ccrep.elementAt(3));
            System.out.print("Approval Code: ");
            System.out.println(ccrep.elementAt(4));
            System.out.print("Trans ID: ");
            System.out.println(ccrep.elementAt(6));
            if (ccrep.size() > 10) {
                System.out.print("MD5 Hash Server: ");
                System.out.println(ccrep.elementAt(37));
            }
            return ccrep.elementAt(1).equals("1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
