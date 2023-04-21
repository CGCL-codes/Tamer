    @Override
    public int getPartition(BytesWritable key, NullWritable value, int numPartitions) {
        int beginRange = 0;
        int endRange = partitions.length;
        int index = 0;
        while (beginRange < endRange) {
            index = (endRange + beginRange) / 2;
            int cmp = BytesWritable.Comparator.compareBytes(partitions[index], 0, partitions[index].length, key.getBytes(), 0, key.getLength());
            if (cmp > 0) {
                endRange = index;
            } else {
                beginRange = index + 1;
            }
        }
        return (beginRange > (numPartitions - 1)) ? numPartitions - 1 : beginRange;
    }
