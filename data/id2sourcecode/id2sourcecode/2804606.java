    public void testConcurrentModification() {
        repository.addClient(client1);
        Iterator clients = repository.getClients().iterator();
        repository.addClient(client2);
        try {
            clients.next();
        } catch (ConcurrentModificationException e) {
            fail("Can't read and write in the client list concurrently");
        }
    }
