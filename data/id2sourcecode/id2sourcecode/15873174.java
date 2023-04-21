    public void handleException(Throwable throwable, ExceptionHandler.errors errorNumber, Object sender) {
        assert errorNumber.ordinal() > errors.NULL.ordinal() : "Vorbedingung nicht erf�llt: Zu kleine Errornummer";
        assert errorNumber.ordinal() < errors.MAX.ordinal() : "Vorbedingung nicht erf�llt: Zu grosse Errornummer";
        assert sender != null : "Erzeuger der Exception muss mitgeliefert werden";
        MessageDialog dialog = MessageDialog.getInstance();
        if (!isMaskedException(errorNumber, sender) && !m_testingMode) {
            assert m_netManager != null : "Vorbedingung nicht erf�llt: m_netManager ist null " + errorNumber;
            assert m_lockManager != null : "Vorbedingung nicht erf�llt: m_lockManager ist null";
            switch(errorNumber) {
                case WrongClientIDFormatClient:
                    writeLog(throwable, "Falsche ClientID vom Client.");
                    break;
                case WrongClientIDFormatServer:
                    writeLog(throwable, "Falsche ClientID vom Server.");
                    break;
                case WrongObjectReceived:
                    writeLog(throwable, "Falsches Objektformat");
                    break;
                case HandshakeError:
                    break;
                case NetworkSendingError:
                    maskException(errors.NetworkSendingError, sender);
                    maskException(errors.NetworkReceivingError, sender);
                    maskException(errors.WrongObjectReceived, sender);
                    removeClient(sender);
                    break;
                case NetworkReceivingError:
                    maskException(errors.NetworkReceivingError, sender);
                    maskException(errors.NetworkSendingError, sender);
                    maskException(errors.WrongObjectReceived, sender);
                    removeClient(sender);
                    break;
                case DestinationClientIDNotExist:
                    break;
                case InterruptedDuringGetID:
                    break;
                case ThreadUncaughtException:
                    writeLog(throwable, "Ein Thread ist abgest�rtzt.");
                    break;
                case ServerThreadFinalize:
                    break;
                case SimpleNetworkManagerFinalize:
                    break;
                case ClientThreadFinalize:
                    break;
                case DispatcherLoadEOF:
                    maskException(errorNumber, sender);
                    break;
                case NetParamError:
                    writeLog(throwable, "Falscher Socket Parameter");
                    break;
                case Test:
                    break;
                case XMLFileReadError:
                    writeLog(throwable, "XML Datei " + sender + " konnte nicht ge�ffnet werden.");
                    dialog.showMessageDialog("Could not open XML File " + sender + ".\nPlease consult the support.\n" + "Die XML Datei " + sender + " konnte nicht gelesen werden.\nBitte kontaktieren Sie ihren Administrator.", "Could not open XML File", JOptionPane.ERROR_MESSAGE);
                    break;
                case XMLFileError:
                    writeLog(throwable, "XML Datei " + sender + " ist korrupt.");
                    dialog.showMessageDialog("Could not parse XML File " + sender + ".\nPlease consult the support.\n" + "Die XML Konfigurationsdatei " + sender + " ist nicht korrekt.\nBitte kontaktieren Sie ihren Administrator.", "Could not open XML File", JOptionPane.ERROR_MESSAGE);
                    break;
                case XMLParseError:
                    writeLog(throwable, "Parser Error");
                    break;
                case XPathError:
                    writeLog(throwable, "XPath Error");
                    break;
                case CouldNotCopyXMLFile:
                    dialog.showMessageDialog("Could not copy XML file " + sender + "to our application directory.\n" + "Die XML Datei " + sender + " konnte nicht in das Verzeichnis der Applikation kopiert werden.", "Error Copy XML File", JOptionPane.ERROR_MESSAGE);
                    break;
                case LiteralNotFound:
                    dialog.showMessageDialog("Could not read literal " + throwable.getMessage() + " from XML file. Please consult your administrator.\n" + "Konnte den Text f�r " + throwable.getMessage() + " nicht lesen. Bitte kontaktieren Sie ihren Administrator.", "Error Reading XML File", JOptionPane.ERROR_MESSAGE);
                    break;
                case ParameterNotFound:
                    dialog.showMessageDialog("Could not read parameter " + throwable.getMessage() + " from XML file. Please consult your administrator.\n" + "Konnte die Einstellung f�r " + throwable.getMessage() + " nicht lesen. Bitte kontaktieren Sie ihren Administrator.", "Error Reading XML File", JOptionPane.ERROR_MESSAGE);
                    break;
                default:
                    assert false : "Unbekannter ErrorCode ";
                    break;
            }
        }
    }
