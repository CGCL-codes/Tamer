    private void getMessage() {
        if (cs == null) return;
        DataInputStream inputStream = null;
        try {
            inputStream = cs.getMessageStream();
        } catch (Exception e) {
            System.out.print("接收消息缓存错误");
            return;
        }
        try {
            String savePath = "E:\\";
            int bufferSize = 8192;
            byte[] buf = new byte[bufferSize];
            int passedlen = 0;
            long len = 0;
            long lastPer = 0;
            long thisPer = 0;
            String fileName = "";
            fileName = inputStream.readUTF();
            savePath += fileName;
            DataOutputStream fileOut = new DataOutputStream(new BufferedOutputStream(new BufferedOutputStream(new FileOutputStream(savePath))));
            len = inputStream.readLong();
            System.out.println("文件名:" + fileName + "\t文件的长度为:" + len / 1024 / 1024 + "M");
            System.out.println("开始接收文件..." + "");
            System.out.print("<");
            while (true) {
                int read = 0;
                if (inputStream != null) {
                    read = inputStream.read(buf);
                }
                passedlen += read;
                if (read == -1) {
                    break;
                }
                thisPer = passedlen * 100 / len;
                if (thisPer > lastPer) {
                    lastPer = thisPer;
                    System.out.print("=" + thisPer);
                }
                fileOut.write(buf, 0, read);
            }
            System.out.println(">");
            System.out.println("接收完成，文件存为" + savePath + "");
            fileOut.close();
        } catch (Exception e) {
            System.out.println("接收消息错误" + "");
            return;
        }
    }
