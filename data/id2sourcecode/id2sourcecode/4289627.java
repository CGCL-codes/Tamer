    private void handleManagerEvents(Object event) {
        if (event instanceof NewChannelEvent) {
            NewChannelEvent newChannelEvent = (NewChannelEvent) event;
            if (newChannelEvent.getChannelState() == 4 && Call.get(newChannelEvent.getUniqueId()) == null) {
                Call.put(newChannelEvent.getUniqueId(), new Call(newChannelEvent));
            }
        } else if (event instanceof LinkEvent) {
            LinkEvent linkEvent = (LinkEvent) event;
            Call call = Call.get(linkEvent.getUniqueId1());
            if (call != null) {
                call.setCalledChannel(linkEvent.getChannel2());
            }
        } else if ((event instanceof HoldEvent)) {
            System.out.println(event);
        } else if (event instanceof HangupEvent) {
            Call call = Call.get(((HangupEvent) event).getUniqueId());
            if (call != null) {
                call.notifyFinish();
            }
        } else if (event instanceof AgentLoginEvent) {
        } else if (event instanceof AgentLogoffEvent) {
        } else if (event instanceof AgentRingNoAnswerEvent) {
        } else if (event instanceof AgentConnectEvent) {
            AgentConnectEvent evt = (AgentConnectEvent) event;
            String agentName = evt.getMember().split("/")[1];
            Call call = Call.get(evt.getUniqueId());
            Agent agent = getAgent(agentName);
            call.setAgent(agent);
            call.setCampaign(getQueues().get(evt.getQueue()).getCampaign());
            call.setTalkStart();
            agent.setCurrentCall(call);
            notifyCallToAgent(call);
            notifyCampaignToAgent(call);
        } else if (event instanceof AgentCompleteEvent) {
            AgentCompleteEvent evt = (AgentCompleteEvent) event;
            Call call = Call.get(evt.getUniqueId());
            call.setTalkEnd();
            notifyHangupToAgent(evt);
        } else if ((event instanceof QueueMemberStatusEvent) && ((QueueMemberStatusEvent) event).getStatus() == QueueMemberStatusEvent.AST_DEVICE_BUSY) {
        } else if ((event instanceof QueueMemberStatusEvent) && ((QueueMemberStatusEvent) event).getStatus() == QueueMemberStatusEvent.AST_DEVICE_NOT_INUSE) {
        } else if (event instanceof JoinEvent) {
            JoinEvent evt = (JoinEvent) event;
            Call call = Call.get(evt.getUniqueId());
            call.setQPosition(evt.getPosition());
            if (call.getCampaign() != null) {
                call.getCampaign().getStats().notifyAnsweredCall();
            }
        } else if (event instanceof QueueCallerAbandonEvent) {
            QueueCallerAbandonEvent evt = (QueueCallerAbandonEvent) event;
            Call call = Call.get(evt.getUniqueId());
            call.setQTime(evt.getHoldTime());
            if (call.getCampaign() != null) {
                call.getCampaign().getStats().notifyDrop(evt.getHoldTime());
            }
        } else if (event instanceof NotifyCallDroppedEvent) {
            NotifyCallDroppedEvent notifyCallDroppedEvent = (NotifyCallDroppedEvent) event;
            if (getCampaigns().containsKey(notifyCallDroppedEvent.getCampaign())) {
                getCampaigns().get(notifyCallDroppedEvent.getCampaign()).getStats().notifyDrop(Long.valueOf(notifyCallDroppedEvent.getDuration()));
            }
        } else if (event instanceof IVRChannelEvent) {
            IVRChannelEvent evt = (IVRChannelEvent) event;
            if (getCampaigns().containsKey(evt.getCampaign())) {
                NotifyIVRChannel(evt);
                Call.get(evt.getUniqueId()).setCampaign(getCampaigns().get(evt.getCampaign()));
            }
        } else if (event instanceof NotifyAgentCallEvent) {
            NotifyAgentCallEvent evt = (NotifyAgentCallEvent) event;
            System.out.println(evt);
            if (getCampaigns().containsKey(evt.getCampaign())) {
                Call call = Call.get(evt.getUniqueId());
                call.setCampaign(getCampaigns().get(evt.getCampaign()));
                call.setAgent(getAgent(evt.getAgent()));
                call.setTalkStart();
                notifyCallToAgent(call);
                notifyCampaignToAgent(call);
            }
        } else if (event instanceof NotifyAgentEndEvent) {
            System.out.println((NotifyAgentEndEvent) event);
            NotifyAgentEndEvent evt = (NotifyAgentEndEvent) event;
            Call call = Call.get(evt.getUniqueId());
            call.setTalkEnd();
            notifyHangupToAgent(evt);
        } else if (event instanceof InboundCallEvent) {
            InboundCallEvent evt = (InboundCallEvent) event;
            Call call = Call.get(evt.getUniqueId());
            call.setCallType(Call.TYPE_INBOUND);
            call.setDNIS(evt.getDNIS());
        } else if (event instanceof OutboundCallEvent) {
            OutboundCallEvent evt = (OutboundCallEvent) event;
            Call call = Call.get(evt.getUniqueId());
            call.setCallType(Call.TYPE_OUTBOUND);
        } else if (event instanceof ConsultationCallEvent) {
            ConsultationCallEvent evt = (ConsultationCallEvent) event;
            Call call = Call.get(evt.getUniqueId());
            call.setCallType(Call.TYPE_CONSULTATION);
        }
    }
