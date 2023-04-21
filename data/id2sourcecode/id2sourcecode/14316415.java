    public void execute() {
        event.setType(RetrieveEvent.NOT_STARTED);
        fireEvent();
        cancelled = false;
        running = true;
        long t = System.currentTimeMillis();
        File f = new File(request.getFileName() + System.currentTimeMillis());
        while (f.exists()) {
            t++;
            f = new File(request.getFileName() + t);
        }
        URL url;
        try {
            event.setType(RetrieveEvent.CONNECTING);
            fireEvent();
            url = request.getUrl();
            request.setFileName(f.getAbsolutePath());
            System.out.println("downloading '" + url + "' to: " + f.getAbsolutePath());
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
            byte[] buffer = new byte[1024 * 256];
            fireEvent();
            is = url.openStream();
            event.setType(RetrieveEvent.TRANSFERRING);
            fireEvent();
            if (!cancelled) {
                long readed = 0;
                for (int i = is.read(buffer); !cancelled && i > 0; i = is.read(buffer)) {
                    dos.write(buffer, 0, i);
                    readed += i;
                }
                dos.close();
            }
            if (cancelled) {
                System.out.println("download cancelled (" + url + ")");
                f.delete();
            } else {
                synchronized (this) {
                    RequestManager.getInstance().addDownloadedURLRequest(request, f.getAbsolutePath());
                }
            }
            running = false;
            if (cancelled) event.setType(RetrieveEvent.REQUEST_CANCELLED); else event.setType(RetrieveEvent.REQUEST_FINISHED);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            event.setType(RetrieveEvent.REQUEST_FAILED);
        } catch (IOException e) {
            e.printStackTrace();
            event.setType(RetrieveEvent.REQUEST_FAILED);
        }
        fireEvent();
    }
