    public Room getRoom(String roomID) {
        if (myRooms.size() == 0) return null;
        synchronized (myRooms) {
            int start = 0;
            int end = myRooms.size() - 1;
            while (start <= end) {
                int mid = (end + start) / 2;
                int comp = ((Room) myRooms.elementAt(mid)).roomID().compareToIgnoreCase(roomID);
                if (comp == 0) return (Room) myRooms.elementAt(mid); else if (comp > 0) end = mid - 1; else start = mid + 1;
            }
        }
        return null;
    }
