    @Override
    protected Void doInBackground() {
        try {
            FileInputStream fis = new FileInputStream(file);
            FileChannel channel = fis.getChannel();
            MappedByteBuffer bb = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int) channel.size());
            CharsetDecoder decoder = Charset.forName("ISO-8859-1").newDecoder();
            CharBuffer charBuf = decoder.decode(bb);
            Matcher lm = linePattern.matcher(charBuf);
            while (lm.find()) {
                CharSequence line = lm.group(1);
                StringTokenizer tokeniser = new StringTokenizer(line.toString(), ";");
                if (tokeniser.countTokens() == 4) {
                    Date date = ModelConstant.parseDate(tokeniser.nextToken());
                    String libelle = tokeniser.nextToken();
                    float montant = ModelConstant.parseMontant(tokeniser.nextToken());
                    String type = tokeniser.nextToken();
                    if (date != null && montant != -1) {
                        final Operation op = new Operation(date, montant, libelle, compte);
                        op.setType(type);
                        OperationEngine.instance().ajoutOperation(op);
                        NotificationCenter.current().notifyChange(ModelConstant.NOTIFICATION_ENGINE_OPERATION_ADD, getClass(), op);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            if (Logger.isLoggingError()) {
                Logger.eLog("Not a available file", e);
            }
            JOptionPane.showMessageDialog(null, "Le fichier n'existe pas", "Fichier incorrect", JOptionPane.ERROR_MESSAGE);
        } catch (CharacterCodingException e) {
            if (Logger.isLoggingError()) {
                Logger.eLog("Assuming encoding is ISO-8859-1 failed", e);
            }
            JOptionPane.showMessageDialog(null, "Le fichier n'est pas au format de caractère ISO-8859-1", "Encodage incorrect", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            if (Logger.isLoggingError()) {
                Logger.eLog("Error during reading the file", e);
            }
            JOptionPane.showMessageDialog(null, "Impossible de lire le contenu du fichier", "Fichier illisible", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
