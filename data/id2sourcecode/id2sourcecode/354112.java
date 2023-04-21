    public void messageFromServer(String Message) {
        StringTokenizer st = new StringTokenizer(Message);
        String msgtype, field1, field2;
        msgtype = st.nextToken();
        System.out.println("Message from server:" + serveripadd + " ::" + Message);
        if (msgtype.equalsIgnoreCase("shiftConnectionTo")) {
            System.out.println("Trying to drop connection and reconnect...");
            field1 = st.nextToken();
            field2 = st.nextToken();
            CPoller newconn = new CPoller();
            newconn.addServerListener(this);
            newconn.connect(field1, Integer.parseInt(field2));
        }
        if (msgtype.equalsIgnoreCase("needProcessor")) {
            field1 = st.nextToken();
            field2 = st.nextToken();
            if (proc == null) {
                proc = new PProcess(Message);
                int start = Integer.parseInt(proc.getStart());
                int stop = Integer.parseInt(proc.getStop());
                int sep = (start + stop) / 2;
                procforme = new PProcess(proc.getMsg());
                procforme.setStop(new String() + sep);
                System.out.println("Process for me:" + procforme.getMsg());
                PProcessor comp = new PProcessor(procforme);
                comp.addListener(this);
                comp.startExecution();
                procforme.setRunning(true);
                proctoclient = new PProcess(proc.getMsg());
                proctoclient.setStart(new String() + (sep + 1));
                System.out.println("Process to client:" + proctoclient.getMsg());
                sendMsgToClient(proctoclient.getMsg());
            } else {
                PProcess temp1 = new PProcess(Message);
                PProcessor temp = new PProcessor(temp1);
                temp.addListener(this);
                temp.startExecution();
                System.out.println("The job has been distributed successfully");
                processstack.add(Message);
            }
        }
    }
