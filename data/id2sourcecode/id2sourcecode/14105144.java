    public ReaderWriterServiceTopic newReaderWriterServiceTopic(Object topicDataType, String topicName, QoSParameters readerQos, QoSParameters writerQos) throws ClassNotFoundException, InstantiationException, IllegalAccessException, ImpossibleToCreateDDSTopic {
        ReaderWriterServiceTopic res = new ReaderWriterServiceTopic(topicDataType, topicName, participant, subscriber, publisher, writerQos, readerQos);
        return res;
    }
