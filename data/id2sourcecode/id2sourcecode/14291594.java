    public static void noticOneUserFileMsg(int msgLength, int msgType, int myQQ, int destQQ, byte filenamelen, byte[] filename, byte[] filedata) {
        String fileShortNmae = new String(filename);
        String filePath = "D:\\" + fileShortNmae;
        String msg = "�ҷ�����һ���ļ���Ϊ" + fileShortNmae + ".\r\n������:" + filePath;
        for (int i = 0; i < onlineFriendframeList.size(); i++) {
            TalkFrame talkframe = onlineFriendframeList.get(i);
            int dest = talkframe.getDestQQ();
            if (dest == myQQ) {
                talkframe.setMsg(myQQ, msg);
                talkframe.setOwerQQ(destQQ);
                talkframe.setClientThread(clientThread);
                talkframe.setVisible(true);
                talkframe.getClientThread().write(filePath, filedata);
                break;
            }
        }
    }
