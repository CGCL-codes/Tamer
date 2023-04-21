    public int put(BufferedReader data, String remotePath) {
        if (passive) {
            toControl.print("TYPE I\n");
            toControl.flush();
            if (getResult() != 200) System.out.println("put(): Cant set Type to I");
            toControl.print("PASV\n");
            toControl.flush();
            int[] i = parseParentheses(getResultString());
            String datahost = i[0] + "." + i[1] + "." + i[2] + "." + i[3];
            int dataport = (i[4] * 256) + i[5];
            BufferedWriter out = null;
            int lastread = 0;
            char[] buffer = new char[2048];
            try {
                try {
                    dataSocket = new Socket(datahost, dataport);
                } catch (UnknownHostException e) {
                    System.out.println("UnknownHost");
                    return -2;
                }
                out = new BufferedWriter(new OutputStreamWriter(dataSocket.getOutputStream()));
            } catch (IOException e) {
                System.out.println("IOExc!");
                return -1;
            }
            System.out.println("CWD " + remotePath.substring(0, remotePath.lastIndexOf('/')));
            toControl.print("CWD " + remotePath.substring(0, remotePath.lastIndexOf('/')) + "\n");
            toControl.flush();
            getResult();
            System.out.println("STOR " + remotePath.substring(remotePath.lastIndexOf('/') + 1));
            toControl.print("STOR " + remotePath.substring(remotePath.lastIndexOf('/') + 1) + "\n");
            toControl.flush();
            try {
                for (int c = 0; (lastread = data.read(buffer, 0, 2048)) != -1; ) {
                    out.write(buffer, 0, lastread);
                    c += lastread;
                }
                out.close();
            } catch (IOException e) {
                return -2;
            }
            getResult();
            getResult();
        }
        return 0;
    }
