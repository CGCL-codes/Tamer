    @Override
    public void run() {
        try {
            IRequest request = (IRequest) marshaler.decode(transport.recieve());
            Object result = request.process(servicesMap, objectsMap);
            transport.send(marshaler.encode(result));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MarshalerException e) {
            e.printStackTrace();
        } finally {
            try {
                this.transport.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
