    private void verifyMembershipInfo(Protocol protocol) throws IOException, DatabaseException, ExitException {
        NodeGroupInfo nodeGroup = (Protocol.NodeGroupInfo) (protocol.read(namedChannel));
        final RepGroupImpl group = repNode.getGroup();
        RepNodeImpl node = group.getNode(nodeGroup.getNodeName());
        try {
            if (nodeGroup.getNodeId() != replicaNameIdPair.getId()) {
                throw new ExitException("The replica node id sent during protocol negotiation: " + replicaNameIdPair + " differs from the one sent in the MembershipInfo " + "request: " + nodeGroup.getNodeId());
            }
            if (node == null) {
                try {
                    repNode.getRepGroupDB().ensureMember(nodeGroup);
                    node = repNode.getGroup().getMember(nodeGroup.getNodeName());
                    if (node == null) {
                        throw EnvironmentFailureException.unexpectedState("Node: " + nodeGroup.getNameIdPair() + " not found");
                    }
                } catch (InsufficientReplicasException e) {
                    throw new ExitException(e);
                } catch (InsufficientAcksException e) {
                    throw new ExitException(e);
                } catch (NodeConflictException e) {
                    throw new ExitException(e);
                }
            } else if (node.isRemoved()) {
                throw new ExitException("Node: " + nodeGroup.getNameIdPair() + " is no longer a member of the group." + " It was explicitly removed.");
            }
            doGroupChecks(nodeGroup, group);
            doNodeChecks(nodeGroup, node);
        } catch (ExitException exception) {
            LoggerUtils.info(logger, repNode.getRepImpl(), exception.getMessage());
            protocol.write(protocol.new NodeGroupInfoReject(exception.getMessage()), namedChannel);
            throw exception;
        }
        replicaNameIdPair.update(node.getNameIdPair());
        namedChannel.setNameIdPair(replicaNameIdPair);
        LoggerUtils.fine(logger, repNode.getRepImpl(), "Channel Mapping: " + replicaNameIdPair + " is at " + namedChannel.getChannel());
        protocol.write(protocol.new NodeGroupInfoOK(group.getUUID(), replicaNameIdPair), namedChannel);
    }
