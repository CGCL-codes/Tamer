        public SiscStreamsProxy() {
            try {
                PipedInputStream inputStream = new PipedInputStream();
                PipedOutputStream outputStream = new PipedOutputStream(inputStream);
                mSiscInputStream = outputStream;
                final AppContext appContext = new AppContext();
                Context.setDefaultAppContext(appContext);
                URL heapUrl = SchemeScriptPlugin.findFile(new Path("/lib/sisc.shp"));
                appContext.addHeap(AppContext.openHeap(heapUrl));
                final DynamicEnvironment env = new DynamicEnvironment(appContext, inputStream, mOutputMonitor);
                Thread replRhread = new Thread() {

                    public void run() {
                        REPL repl = new REPL(env, REPL.getCliProc(appContext));
                        repl.go();
                    }
                };
                replRhread.run();
            } catch (IOException exception) {
                SchemeScriptPlugin.logException("unable to start sisc", exception);
            } catch (ClassNotFoundException exception) {
                SchemeScriptPlugin.logException("unable to start sisc", exception);
            }
        }
