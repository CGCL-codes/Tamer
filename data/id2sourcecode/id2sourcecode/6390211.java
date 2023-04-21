    private void deleteCommits() throws IOException {
        int size = commitsToDelete.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                CommitPoint commit = (CommitPoint) commitsToDelete.get(i);
                if (infoStream != null) {
                    message("deleteCommits: now decRef commit \"" + commit.getSegmentsFileName() + "\"");
                }
                int size2 = commit.files.size();
                for (int j = 0; j < size2; j++) {
                    decRef((String) commit.files.get(j));
                }
            }
            commitsToDelete.clear();
            size = commits.size();
            int readFrom = 0;
            int writeTo = 0;
            while (readFrom < size) {
                CommitPoint commit = (CommitPoint) commits.get(readFrom);
                if (!commit.deleted) {
                    if (writeTo != readFrom) {
                        commits.set(writeTo, commits.get(readFrom));
                    }
                    writeTo++;
                }
                readFrom++;
            }
            while (size > writeTo) {
                commits.remove(size - 1);
                size--;
            }
        }
    }
