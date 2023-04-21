                @Override
                public void topicUpdated(TopicUpdatedEvent tue) {
                    System.out.println("New topic for: " + tue.getChannel() + " -> " + tue.getNewTopic());
                }
