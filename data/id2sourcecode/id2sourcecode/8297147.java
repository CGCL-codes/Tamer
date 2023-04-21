    static void run() {
        String prevVal = null;
        while (true) {
            BufferedInputStream reader = null;
            try {
                URLConnection connection = new URL(url).openConnection();
                reader = new BufferedInputStream(connection.getInputStream());
                connection.setReadTimeout(2000);
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            int c = 0;
            String source = "";
            try {
                while ((c = reader.read()) != -1) source = source + (char) c;
                if (prevVal == null) {
                    prevVal = source;
                    System.out.print(source + Calendar.getInstance().getTime().toString());
                } else if (!prevVal.equals(source)) System.out.print(source + Calendar.getInstance().getTime().toString());
                prevVal = source;
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
