        public void run() {
            try {
                URL httpUrl = new URL(url);
                HttpURLConnection urlConn = (HttpURLConnection) httpUrl.openConnection();
                urlConn.setDoOutput(true);
                urlConn.setRequestMethod("POST");
                if (contentType != null) urlConn.setRequestProperty("Content-Type", contentType);
                OutputStream os = urlConn.getOutputStream();
                os.write(dataIn.getBytes());
                BufferedReader receiver = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                StringBuffer lastReceived = new StringBuffer();
                char[] rt = new char[512];
                int n = 0;
                while ((n = receiver.read(rt, 0, 512)) != -1) {
                    lastReceived.append(rt, 0, n);
                }
                dataOut = lastReceived.toString().trim();
                if (debug) System.out.println(dataOut);
            } catch (Exception e) {
                if (debug) e.printStackTrace();
                dataOut = EXCEPTION;
            }
        }
