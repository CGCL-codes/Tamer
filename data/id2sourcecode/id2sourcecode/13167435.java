    public void run() {
        PrintWriter printwriter = null;
        try {
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "8859_1"));
            InputStream inputstream = socket.getInputStream();
            BufferedOutputStream bufferedoutputstream = new BufferedOutputStream(socket.getOutputStream());
            printwriter = new PrintWriter(new OutputStreamWriter(bufferedoutputstream, "8859_1"));
            String s = bufferedreader.readLine();
            HashMap hashmap = new HashMap(6);
            String s1;
            while ((s1 = bufferedreader.readLine()).length() != 0) {
                System.out.println("Header : " + s1);
                if (s1.startsWith("Host:")) {
                    String s2 = s1.substring(0, 4);
                    String s3 = s1.substring(5);
                    hashmap.put(s2, s3);
                } else {
                    int i = s1.lastIndexOf(":");
                    String s4 = s1.substring(0, i);
                    String s5 = s1.substring(i + 1);
                    hashmap.put(s4, s5);
                }
            }
            handleRequest(bufferedreader, inputstream, bufferedoutputstream, printwriter, s, hashmap);
            bufferedoutputstream.close();
            printwriter.close();
            socket.close();
        } catch (UnsupportedEncodingException unsupportedencodingexception) {
            System.out.println("Unsupported encoding: " + unsupportedencodingexception);
            outputError(printwriter, unsupportedencodingexception);
        } catch (IOException ioexception) {
            System.out.println("Could not get appropriate streams: " + ioexception);
            outputError(printwriter, ioexception);
        } catch (Exception exception) {
            System.out.println("unexpected exception: " + exception);
            outputError(printwriter, exception);
        }
    }
