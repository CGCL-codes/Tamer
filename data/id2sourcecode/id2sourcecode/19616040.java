    private void handleHttpConnection(HttpConnection anHttpConnection) {
        WriteFileTask writeTask = new WriteFileTask(new File("C:\\write.html"), anHttpConnection, getHandleName(), anHttpConnection.getPath().getBytes());
        try {
            enqueue("filereader", writeTask);
        } catch (TaskQueueException e) {
            e.printStackTrace();
        } catch (UnknownTaskQueueException e) {
            e.printStackTrace();
        }
    }
