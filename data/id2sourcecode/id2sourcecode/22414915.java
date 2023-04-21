    private void aboutChannel() {
        MessageBrokerController controller = MessageBrokerController.getInstance();
        if (command.length == 1) {
            Set<String> channels = controller.getChannelSet();
            Iterator<String> iterator = channels.iterator();
            System.out.println("===========CHANNELS===========\n");
            while (iterator.hasNext()) {
                String channelName = iterator.next().toString();
                System.out.println(channelName + " queues: " + controller.get(channelName).getQueueSet().size());
            }
            System.out.println("\n=========CHANNELS END=========\n");
            System.out.println("\nTo obtain detailed information about channel please type 'viewchannel channelName'");
        } else if (command.length == 2 && channelCheck()) {
            System.out.println(controller.get(command[1]).getName() + " channel contains:\n");
            System.out.println("Channel workers number: " + controller.get(command[1]).getWorkersPoolSize());
            Set<String> queues = controller.get(command[1]).getQueueSet();
            System.out.println("Channel logger: " + controller.get(command[1]).getLogger().getName() + "\n");
            Iterator<String> iterator = queues.iterator();
            System.out.println("===========QUEUES===========\n");
            while (iterator.hasNext()) {
                String queueName = iterator.next().toString();
                System.out.println(queueName + "\nmessages: " + controller.get(command[1]).getQueue(queueName).getCurrentLength() + "\nmax massages possible: " + controller.get(command[1]).getQueue(queueName).getMaxLength() + "\nqueue type: " + controller.get(command[1]).getQueue(queueName).getType() + "\n");
            }
            System.out.println("=========QUEUES END=========\n");
        } else System.out.println("Wrong channel name. Please try again.");
    }
