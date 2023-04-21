    public void startJob() {
        int start, stop;
        try {
            proc = new PProcess();
            System.out.println(proc.getClassName());
            proc.setNoOfHops("10");
            proc.setOriginIp(InetAddress.getLocalHost().getHostAddress());
            proc.setProcessId(new String() + serverportno);
            proc.setStart(ld.getStart());
            proc.setStop(ld.getStop());
            proc.setClassName(ld.getClassName());
            proc.setType(ld.getTypeOfInput());
            System.out.println(" The procees for the network" + proc.getMsg());
            processon = true;
            start = Integer.parseInt(proc.getStart());
            stop = Integer.parseInt(proc.getStop());
            int sep = (start + stop) / 2;
            procforme = new PProcess(proc.getMsg());
            procforme.setStop(new String() + sep);
            PProcessor comp = new PProcessor(procforme);
            comp.addListener(this);
            comp.startExecution();
            procforme.setRunning(true);
            proctoclient = new PProcess(proc.getMsg());
            proctoclient.setStart(new String() + (sep + 1));
            System.out.println("Process for client " + proctoclient.getMsg());
            sendMsgToClient(proctoclient.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
