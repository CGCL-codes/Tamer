    @SuppressWarnings("unchecked")
    public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                monitor.beginTask(Messages.getString("AvailableUpdatesRunnable.search.for.update"), IProgressMonitor.UNKNOWN);
            }

            ;
        });
        InputStream istream = null;
        try {
            URL url = new URL(fConnection + IUpdateable.UPDATE_XML);
            URLConnection conn = url.openConnection();
            if (monitor.isCanceled()) {
                throw new InterruptedException();
            }
            istream = conn.getInputStream();
            if (monitor.isCanceled()) {
                throw new InterruptedException();
            }
            UpdateXmlReader reader = new UpdateXmlReader(istream);
            if (monitor.isCanceled()) {
                throw new InterruptedException();
            }
            listOfUpdates = reader.getUpdateableModules();
            if (monitor.isCanceled()) {
                throw new InterruptedException();
            }
            url = new URL(fConnection + IUpdateable.MIRROR_LIST);
            conn = url.openConnection();
            istream = conn.getInputStream();
            if (monitor.isCanceled()) {
                throw new InterruptedException();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(istream));
            String line = "";
            ArrayList<String> al = new ArrayList<String>();
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("#")) {
                    StringTokenizer st = new StringTokenizer(line, "=");
                    if (st.hasMoreTokens()) {
                        String mirror = st.nextToken();
                        if (st.hasMoreTokens()) {
                            al.add(mirror);
                            mirrorMap.put(mirror, st.nextToken());
                        }
                    }
                }
            }
            mirrors = al.toArray(new String[0]);
        } catch (InterruptedException e) {
        } catch (MalformedURLException e) {
            new RuntimeException(e);
        } catch (IOException e) {
            new RuntimeException(e);
        } catch (Exception e) {
            MessageSystem.logException("", getClass().getName(), "run", null, e);
            new RuntimeException(e);
        } finally {
            try {
                if (istream != null) {
                    istream.close();
                }
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                monitor.done();
            }
        });
    }
