    public void run() {
        try {
            m_client = m_clientClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        for (String m_sPipeEndPoint : m_sPipeEndPoints) {
            String OnPipeReaderFunction = "OnPipeRead" + m_sPipeEndPoint;
            Class[] partypes = new Class[2];
            partypes[0] = String[].class;
            partypes[1] = Object[].class;
            try {
                Method thisPipeReader;
                thisPipeReader = m_clientClass.getMethod(OnPipeReaderFunction, partypes);
                m_pipeReaders.put(OnPipeReaderFunction, thisPipeReader);
            } catch (NoSuchMethodException e) {
                System.out.println("Unable to find " + OnPipeReaderFunction + " in class " + m_clientClass.getName());
                System.out.println("Ok, if this class only expects to write to this pipe, otherwise reads will fail!");
            }
        }
        for (String s : m_sPoolEndPoints) {
            String OnPoolUpdater = "OnPoolNotify" + s;
            Class[] partypes = new Class[2];
            partypes[0] = String.class;
            partypes[1] = String[].class;
            try {
                Method thisPoolUpdateFunction;
                thisPoolUpdateFunction = m_clientClass.getMethod(OnPoolUpdater, partypes);
                m_poolUpdaters.put(OnPoolUpdater, thisPoolUpdateFunction);
            } catch (NoSuchMethodException e) {
                System.out.println("Unable to find " + OnPoolUpdater + " in class " + m_clientClass.getName());
                System.out.println("Ok, if this class is purely a publisher to this pool, otherwise reads will fail!");
            }
        }
        try {
            Method thisRun;
            thisRun = m_clientClass.getMethod("run");
            try {
                thisRun.invoke(m_client);
            } catch (InvocationTargetException e) {
                e.printStackTrace(System.out);
            } catch (IllegalAccessException e) {
                e.printStackTrace(System.out);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace(System.out);
        }
    }
