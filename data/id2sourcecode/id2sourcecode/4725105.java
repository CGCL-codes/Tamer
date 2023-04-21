    private void go() throws UploadException, IOException {
        int state = BEFORE_FILE_STATE;
        Comparator comparator = new ByteArrayComparator();
        int count = 0;
        long previous_newline_pos = 0L;
        long start_pos = 0L;
        long stop_pos = 0L;
        List headers = new LinkedList();
        upload_results = new LinkedList();
        UploadResults current_results = null;
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Long current = (Long) iter.next();
            long current_newline_pos = current.longValue();
            long difference = current_newline_pos - previous_newline_pos;
            switch(state) {
                case BEFORE_FILE_STATE:
                    if (difference != start_boundary_length) {
                        throw new UploadException("Not a file upload: First line not a boundary");
                    } else {
                        previous_newline_pos = current_newline_pos + 2;
                        state = READ_HEADERS_STATE;
                    }
                    break;
                case READ_HEADERS_STATE:
                    if (difference == 0) {
                        current_results = _walkHeaders(headers);
                        upload_results.add(current_results);
                        headers.clear();
                        state = READ_FILE_STATE;
                        start_pos = current_newline_pos;
                    } else {
                        file.seek(previous_newline_pos);
                        count = file.read(buffer, 0, (int) difference);
                        headers.add(new String(buffer, 0, count));
                    }
                    previous_newline_pos = current_newline_pos + 2;
                    break;
                case READ_FILE_STATE:
                    if (difference == start_boundary_length) {
                        file.seek(previous_newline_pos);
                        file.read(buffer, 0, (int) difference);
                        if (comparator.compare(start_boundary, buffer) == 0) {
                            try {
                                stop_pos = previous_newline_pos;
                                _readwrite(start_pos, stop_pos, current_results);
                            } catch (Exception oops) {
                            }
                            state = READ_HEADERS_STATE;
                        }
                    } else if (difference == end_boundary_length) {
                        file.seek(previous_newline_pos);
                        file.read(buffer, 0, (int) difference);
                        if (comparator.compare(end_boundary, buffer) == 0) {
                            try {
                                stop_pos = previous_newline_pos;
                                _readwrite(start_pos, stop_pos, current_results);
                            } catch (Exception oops) {
                            }
                            state = READ_HEADERS_STATE;
                        }
                    }
                    previous_newline_pos = current_newline_pos + 2;
                    break;
            }
        }
    }
