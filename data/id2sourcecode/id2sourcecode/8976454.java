        private byte[] fetchClassFile(String className, String agentName) throws IMTPException, ClassNotFoundException {
            if (myLogger.isLoggable(Logger.FINE)) myLogger.log(Logger.FINE, "Fetching class " + className);
            String fileName = className.replace('.', '/') + ".class";
            InputStream classStream = getClass().getClassLoader().getResourceAsStream(fileName);
            if (classStream == null) {
                classStream = ClassLoader.getSystemResourceAsStream(fileName);
            }
            if (classStream == null) {
                if (myLogger.isLoggable(Logger.FINER)) myLogger.log(Logger.FINER, "Class not found as a system resource. Try manually");
                classStream = manualGetResourceAsStream(fileName);
            }
            if (classStream == null && agentName != null) {
                try {
                    AgentManagementService amSrv = (AgentManagementService) myFinder.findService(AgentManagementService.NAME);
                    ClassLoader cLoader = amSrv.getCodeLocator().getAgentClassLoader(new AID(agentName, AID.ISGUID));
                    classStream = cLoader.getResourceAsStream(fileName);
                } catch (NullPointerException npe) {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (classStream == null) {
                if (myLogger.isLoggable(Logger.WARNING)) {
                    myLogger.log(Logger.WARNING, "Class " + className + " not found");
                }
                throw new ClassNotFoundException(className);
            }
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] bytes = new byte[SIZE_JAR_BUFFER];
                int read = 0;
                DataInputStream dis = new DataInputStream(classStream);
                while ((read = dis.read(bytes)) >= 0) {
                    baos.write(bytes, 0, read);
                }
                dis.close();
                if (myLogger.isLoggable(Logger.FINER)) {
                    myLogger.log(Logger.FINER, "Class " + className + " fetched");
                }
                return (baos.toByteArray());
            } catch (IOException ioe) {
                throw new ClassNotFoundException("IOException reading class bytes. " + ioe.getMessage());
            }
        }
