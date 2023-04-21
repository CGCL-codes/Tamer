        private void writeEntries(int bottomOrdinal, long bottomPosition, int topOrdinal, long topPosition) {
            if (bottomOrdinal == topOrdinal || bottomOrdinal + 1 == topOrdinal) return;
            int index = (bottomOrdinal + topOrdinal) / 2;
            long err = positions[index];
            long est = (bottomPosition + topPosition) / 2;
            long pos = est + err;
            writeEntries(bottomOrdinal, bottomPosition, index, pos);
            writeEntry(index, pos, err);
            writeEntries(index, pos, topOrdinal, topPosition);
        }
