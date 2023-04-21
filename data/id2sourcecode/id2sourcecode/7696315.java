    protected Object internalRead() {
        if (nbElt_ <= 0) {
            return null;
        }
        final TDoubleArrayList time = new TDoubleArrayList();
        final RubarSolutionSequentielResult r = new RubarSolutionSequentielResult();
        r.nbVar_ = nbValues_;
        r.nbElt_ = nbElt_;
        FileChannel ch = null;
        try {
            final String s = in_.readLine();
            time.add(Double.parseDouble(s));
            r.timeLength_ = getCurrentPosInReader();
            r.nbLigne_ = (int) Math.ceil(nbElt_ / nbValueByLigne_);
            in_.readLine();
            long pos = getCurrentPosInReader();
            r.resultLength_ = pos - r.timeLength_;
            if (r.nbLigne_ > 1) {
                for (int i = r.nbLigne_ - 2; i > 0; i--) {
                    in_.readLine();
                }
                pos = getCurrentPosInReader();
                in_.readLine();
                r.resultLastLineLength_ = getCurrentPosInReader() - pos;
            } else {
                r.resultLastLineLength_ = r.resultLength_;
                r.resultLength_ = 0;
            }
            pos = getCurrentPosInReader();
            in_.close();
            in_ = null;
            long lengthForVariable = r.resultLastLineLength_ + (r.nbLigne_ - 1) * r.resultLength_;
            ch = new FileInputStream(f_).getChannel();
            ch.position(pos + (nbValues_ - 1) * lengthForVariable);
            final ByteBuffer buf = ByteBuffer.allocate((int) r.timeLength_);
            lengthForVariable = lengthForVariable * nbValues_;
            while (ch.position() < (ch.size() - r.timeLength_)) {
                buf.rewind();
                if (ch.read(buf) != r.timeLength_) {
                    break;
                }
                buf.rewind();
                final String str = new String(buf.array());
                final char c = str.charAt(str.length() - 1);
                if (c != '\r' && c != '\n') {
                    lineError_ = true;
                }
                try {
                    time.add(Double.parseDouble(str));
                } catch (final NumberFormatException e) {
                    analyze_.addError(e.getMessage(), time.size());
                    isTruncatedResultats_ = true;
                    break;
                }
                ch.position(ch.position() + lengthForVariable);
            }
            if (ch.size() - ch.position() < 0) {
                analyze_.addError(H2dResource.getS("Fichier tronqu�"), -1);
                isTruncatedResultats_ = true;
                timeRemoved_ = time.remove(time.size() - 1);
            }
        } catch (final EOFException e) {
        } catch (final NumberFormatException e) {
            e.printStackTrace();
            analyze_.manageException(e);
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ch != null) {
                    ch.close();
                }
            } catch (final IOException _evt) {
                FuLog.error(_evt);
            }
        }
        r.timeStep_ = time.toNativeArray();
        return r;
    }
