    public static String push(String name, int action, int x, int y, int power) {
        String result = "";
        try {
            String line;
            URL url = new URL("http://hackathon20091219-sensor.appspot.com/recieveEvent");
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.setRequestMethod("POST");
            uc.setRequestProperty("Accept-Language", "ja");
            uc.setDoOutput(true);
            PrintWriter writer;
            BufferedReader reader;
            String text = "{\"name\":\"" + name + "\"" + ",\"value\":" + "{\"y\":" + Integer.toString(y) + ",\"power\":" + Integer.toString(power) + ",\"x\":" + Integer.toString(x) + "},\"action\":" + action + "}";
            System.out.println(text);
            writer = new PrintWriter(uc.getOutputStream());
            writer.print("event=" + text);
            writer.close();
            reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            while ((line = reader.readLine()) != null) {
                result += line;
            }
            reader.close();
            uc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
