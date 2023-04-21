    @Override
    public void run() {
        try {
            federationRequest.getWriter().write(federationRequest.getReader().readLine() + '\n');
            federationRequest.getWriter().flush();
            federationRequest.closeIt();
        } catch (Exception e) {
            System.err.println("Service handling failed: " + e);
            e.printStackTrace();
        }
    }
