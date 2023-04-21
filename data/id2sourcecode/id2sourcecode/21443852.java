        @Override
        public EmptyRecord next() {
            if (!hasNext()) throw new NoSuchElementException();
            if (count == recordCount - 1L) {
                return new EmptyRecord(count++, posStats.topPosition);
            }
            while (depth > 0 && stack[depth * 4 + 2] == stack[(depth - 1) * 4 + 2]) {
                depth--;
            }
            if (depth > 0) {
                stack[depth * 4 + 0] = stack[depth * 4 + 2];
                stack[depth * 4 + 1] = stack[depth * 4 + 3];
                stack[depth * 4 + 2] = stack[(depth - 1) * 4 + 2];
                stack[depth * 4 + 3] = stack[(depth - 1) * 4 + 3];
            }
            while (true) {
                long bottomOrdinal = stack[depth * 4 + 0];
                long bottomPosition = stack[depth * 4 + 1];
                long topOrdinal = stack[depth * 4 + 2];
                long topPosition = stack[depth * 4 + 3];
                long ord = (bottomOrdinal + topOrdinal) / 2;
                if (ord == bottomOrdinal || ord == topOrdinal) {
                    count++;
                    return new EmptyRecord(bottomOrdinal, bottomPosition);
                }
                reader.setPosition(ord * fixedBitSize);
                long err = reader.readLong(fixedBitSize);
                long pos;
                if (err == invalid) {
                    pos = findPosition(ord);
                } else {
                    if (err >= negativeBoundary) err |= negativeMask;
                    long est = (topPosition + bottomPosition) / 2;
                    pos = est + err;
                }
                depth++;
                stack[depth * 4 + 0] = bottomOrdinal;
                stack[depth * 4 + 1] = bottomPosition;
                stack[depth * 4 + 2] = ord;
                stack[depth * 4 + 3] = pos;
            }
        }
