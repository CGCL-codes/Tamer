        ChunkCheckThread(FlatFileTrancheServer ffts, UserZipFile user, boolean isMetaData, BufferedWriter writer, long threadNumber, long totalThreadCount) {
            this.ffts = ffts;
            this.user = user;
            this.isMetaData = isMetaData;
            this.writer = writer;
            this.isServersToUse = InjectDataIntoTrancheNetwork.serversToUse.size() > 0;
            if (this.isServersToUse) {
                requiredReps = InjectDataIntoTrancheNetwork.serversToUse.size();
            } else {
                requiredReps = 3;
            }
            this.threadNumber = threadNumber;
            this.totalThreadCount = totalThreadCount;
            this.startingOffset = 0 + this.threadNumber * hashBatchSize;
            String type = null;
            if (isMetaData) {
                type = "meta data";
            } else {
                type = "data";
            }
            System.out.println("ChunkCheckThread #" + this.threadNumber + " for " + type + " starting with " + this.startingOffset);
        }
