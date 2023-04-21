    public static TorrentMetadata createFromFile(File file) throws IOException, MalformedMetadataException {
        BDecoder decoder = new BDecoder(new FileInputStream(file));
        BElement bDec = null;
        try {
            bDec = decoder.decodeNext();
        } catch (BEncodingException e) {
            throw new MalformedMetadataException("File " + file.getAbsolutePath() + " is not a well-formed .torrent", e);
        }
        TorrentMetadata result = new TorrentMetadata();
        try {
            BDictionary root = (BDictionary) bDec;
            bDec = root.get(new BString("announce"));
            if (bDec == null) throw new MalformedMetadataException("announce not present in .torrent");
            result.announce = ((BString) bDec).getValue();
            bDec = root.get(new BString("creation date"));
            if (bDec != null) result.creationDate = new Date(((BInteger) bDec).getValue());
            bDec = root.get(new BString("comment"));
            if (bDec != null) result.comment = ((BString) bDec).getValue();
            bDec = root.get(new BString("created by"));
            if (bDec != null) result.createdBy = ((BString) bDec).getValue();
            root = (BDictionary) root.get(new BString("info"));
            if (root == null) throw new MalformedMetadataException("announce not present in .torrent");
            ByteArrayOutputStream info = new ByteArrayOutputStream(1024);
            BEncoder encoder = new BEncoder(info);
            encoder.encode(root);
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            byte[] data = Arrays.copyOf(info.toByteArray(), info.size());
            sha1.update(data);
            result.infoHash = sha1.digest();
            bDec = root.get(new BString("piece length"));
            if (bDec == null) throw new MalformedMetadataException("piece length not present in .torrent");
            result.pieceLength = (int) (((BInteger) bDec).getValue());
            bDec = root.get(new BString("pieces"));
            if (bDec == null) throw new MalformedMetadataException("pieces not present in .torrent");
            byte[] hashes = ((BString) bDec).getBytes();
            if (hashes.length % 20 != 0) throw new MalformedMetadataException("incorrect length of pieces");
            for (int bytesRead = 0; bytesRead < hashes.length; bytesRead += 20) {
                result.pieceHashes.add(Arrays.copyOfRange(hashes, bytesRead, bytesRead + 20));
            }
            bDec = root.get(new BString("length"));
            if (bDec != null) {
                result.directory = "";
                ContainedFile cFile = new ContainedFile();
                cFile.length = ((BInteger) bDec).getValue();
                bDec = root.get(new BString("name"));
                if (bDec == null) throw new MalformedMetadataException("file name not present in .torrent");
                cFile.name = ((BString) bDec).getValue();
                result.files.add(cFile);
            } else if ((bDec = root.get(new BString("files"))) != null) {
                bDec = root.get(new BString("name"));
                if (bDec == null) throw new MalformedMetadataException("file name not present in .torrent");
                result.directory = ((BString) bDec).getValue();
                BList list = (BList) root.get(new BString("files"));
                if (list.size() == 0) throw new MalformedMetadataException(".torrent contains no files");
                for (BElement next : list) {
                    BDictionary dir = (BDictionary) next;
                    ContainedFile cFile = new ContainedFile();
                    bDec = dir.get(new BString("length"));
                    if (bDec == null) throw new MalformedMetadataException("file length not present");
                    cFile.length = ((BInteger) bDec).getValue();
                    bDec = dir.get(new BString("path"));
                    if (bDec == null) throw new MalformedMetadataException("file path not present");
                    BList pathList = (BList) bDec;
                    if (pathList.size() == 0) throw new MalformedMetadataException("file path not present");
                    StringBuffer path = new StringBuffer();
                    for (BElement element : pathList) {
                        path.append(((BString) element).getValue());
                        path.append(File.separator);
                    }
                    path.deleteCharAt(path.length() - 1);
                    cFile.name = path.toString();
                    result.files.add(cFile);
                }
            } else {
                throw new MalformedMetadataException("no file found in .torrent");
            }
            result.totalLength = 0;
            for (ContainedFile tmpFile : result.files) {
                result.totalLength += tmpFile.getLength();
            }
        } catch (ClassCastException e) {
            throw new MalformedMetadataException("not valid structure", e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return result;
    }
