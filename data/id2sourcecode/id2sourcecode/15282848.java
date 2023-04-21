    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey key = context.getJobDetail().getKey();
        l.debug("Instance " + key + " of schedulerId: " + schedulerId + ", and jobId is: " + jobId + ", and startURLId is: " + startURLId);
        websiteschema.model.domain.Job job = jobMapper.getById(jobId);
        StartURL startURL = startURLMapper.getById(startURLId);
        String siteId = startURL.getSiteId();
        l.debug("getChannelsBySiteId " + siteId);
        List<Channel> channels = channelMapper.getChannelsBySiteId(siteId);
        String jobConfig = job.getConfigure();
        l.debug(jobConfig);
        if (null != channels) {
            l.debug("there are " + channels.size() + " channels need to start.");
            Map<String, String> conf = CollectionUtil.toMap(jobConfig);
            String queueName = conf.get("QUEUE_NAME");
            RabbitQueue<Message> queue = StringUtil.isNotEmpty(queueName) ? TaskHandler.getInstance().getQueue(queueName) : TaskHandler.getInstance().getQueue();
            com.rabbitmq.client.Channel channel = null;
            try {
                try {
                    channel = queue.getChannel();
                } catch (Exception ex) {
                    l.error(ex.getMessage(), ex);
                    queue.processException(ex);
                }
                if (null != channel) {
                    for (Channel chl : channels) {
                        if (chl.getStatus() == Channel.STATUS_VALID) {
                            Task task = new Task(schedulerId);
                            try {
                                taskMapper.insert(task);
                                Message msg = create(job, chl.getUrl());
                                msg.setTaskId(task.getId());
                                queue.offer(channel, msg);
                                l.debug("Message about Job " + jobId + " has been emitted to queue: " + queue.getQueueName());
                                task.setStatus(Task.SENT);
                                taskMapper.update(task);
                            } catch (Exception ex) {
                                task.setStatus(Task.UNSENT);
                                task.setMessage(ex.getMessage());
                                taskMapper.update(task);
                                l.error(ex.getMessage(), ex);
                                break;
                            }
                        }
                    }
                } else {
                    l.debug("can not get channel from queue: " + queue.getQueueName());
                }
            } catch (Exception ex) {
                l.error(ex.getMessage(), ex);
            } finally {
                if (null != channel) {
                    try {
                        channel.close();
                    } catch (Exception ex) {
                        l.error(ex.getMessage(), ex);
                    }
                }
            }
        }
    }
