    public String toString() {
        StringBuffer bf = new StringBuffer();
        try {
            bf.append("---------\n");
            bf.append("Fserv ").append(trigger.getTrigger()).append(" on ").append(trigger.getUser()).append("@").append(trigger.getChannel()).append("\n");
            bf.append("Queues: (").append(curqueue).append("/").append(maxqueue).append("), Sends: (").append(cursend).append("/").append(maxsend).append(")\n");
            bf.append("Files on Server: ").append(files.size()).append("\n");
            bf.append("# own files in queue: ").append(myqueues.length).append(" / in send: ").append(mysends.length).append("\n");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return bf.toString();
    }
