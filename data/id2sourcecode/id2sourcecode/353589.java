    public void run() {
        try {
            URL url = new URL(this.testTarget);
            String[] urlSplit = this.testTarget.split("/");
            this.fileName = urlSplit[urlSplit.length - 1];
            this.target = url.getProtocol() + "://" + url.getHost();
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(this.fileName));
            URLConnection urlConnection = url.openConnection();
            InputStream in = urlConnection.getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;
            long fileSize = 0;
            long start = System.currentTimeMillis();
            while (((numRead = in.read(buffer)) != -1) && (!timedout)) {
                out.write(buffer, 0, numRead);
                fileSize += numRead;
                long timePassed = System.currentTimeMillis() - start;
                if (timePassed >= (timeout * 1000)) {
                    timedout = true;
                }
            }
            long end = System.currentTimeMillis();
            in.close();
            out.close();
            long time = end - start;
            Debug.log("Speed Test", "start: " + start + ", end: " + end + ", diff: " + time);
            Debug.log("Speed Test", "filesize: " + fileSize);
            String speed = "" + ((fileSize / time) * 1000) / 1024;
            new File(this.fileName).delete();
            this.result.put("Speed", speed);
            Debug.log("Speed Test", "download speed: " + (fileSize / time) + " B/ms = " + speed + " KB/s");
        } catch (FileNotFoundException e) {
            Debug.log("Speed Test", "Couldn't download file");
            this.result.put("Speed", "FAIL");
        } catch (Exception e) {
            Debug.log("Speed Test", "Test failed");
            this.result.put("Speed", "FAIL");
        }
    }
