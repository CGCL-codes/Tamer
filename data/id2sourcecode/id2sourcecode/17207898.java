    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(syncAlreadyInProgress ? 1 : 0);
        parcel.writeInt(tooManyDeletions ? 1 : 0);
        parcel.writeInt(tooManyRetries ? 1 : 0);
        parcel.writeInt(databaseError ? 1 : 0);
        parcel.writeInt(fullSyncRequested ? 1 : 0);
        parcel.writeInt(partialSyncUnavailable ? 1 : 0);
        parcel.writeInt(moreRecordsToGet ? 1 : 0);
        stats.writeToParcel(parcel, flags);
    }
