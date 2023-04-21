    public void testThreaded() throws IOException {
        String link;
        URL url;
        URLConnection connection;
        BufferedInputStream in;
        int index;
        long begin;
        double bytes_per_second;
        int delay;
        Stream stream;
        long time1;
        long time2;
        Thread thread;
        long available1;
        long available2;
        link = "http://htmlparser.sourceforge.net/javadoc_1_3/index-all.html";
        try {
            url = new URL(link);
            System.gc();
            index = 0;
            connection = url.openConnection();
            connection.connect();
            in = new BufferedInputStream(connection.getInputStream());
            begin = System.currentTimeMillis();
            while (-1 != in.read()) index++;
            bytes_per_second = 1000.0 * index / (System.currentTimeMillis() - begin);
            in.close();
            delay = (int) (1.5 * 1000 * bytes_per_second / 72400);
            System.gc();
            index = 0;
            available1 = 0;
            connection = url.openConnection();
            connection.connect();
            in = new BufferedInputStream(connection.getInputStream());
            try {
                Thread.sleep(delay);
            } catch (Exception e) {
                e.printStackTrace();
            }
            begin = System.currentTimeMillis();
            do {
                index++;
                if (0 == index % 1000) available1 += in.available();
            } while (-1 != in.read());
            time1 = System.currentTimeMillis() - begin;
            in.close();
            System.gc();
            index = 0;
            available2 = 0;
            connection = url.openConnection();
            connection.connect();
            int length = connection.getContentLength();
            stream = new Stream(connection.getInputStream(), length);
            thread = new Thread(stream);
            thread.setPriority(Thread.NORM_PRIORITY - 1);
            thread.start();
            try {
                Thread.sleep(delay);
            } catch (Exception e) {
                e.printStackTrace();
            }
            begin = System.currentTimeMillis();
            do {
                index++;
                if (0 == index % 1000) available2 += stream.available();
            } while (-1 != stream.read());
            time2 = System.currentTimeMillis() - begin;
            stream.close();
            double samples = index / 1000;
            assertTrue("slower (" + time2 + ") vs. (" + time1 + ")", time2 < time1);
            assertTrue("average available bytes not greater (" + available2 / samples + ") vs. (" + available1 / samples + ")", available2 > available1);
        } catch (MalformedURLException murle) {
            fail("bad url " + link);
        }
    }
