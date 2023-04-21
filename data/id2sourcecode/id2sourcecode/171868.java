    public void envoyer_Msg(String ty, String qt, String refe, String com) {
        try {
            DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
            DocumentBuilder constructeur = fabrique.newDocumentBuilder();
            Document document = constructeur.newDocument();
            document.setXmlVersion("1.0");
            document.setXmlStandalone(true);
            Element racine = document.createElement("messages");
            racine.setAttribute("id_client", String.valueOf(id));
            Element op = document.createElement("message");
            racine.appendChild(op);
            Element Euser = document.createElement("user");
            Euser.setTextContent(user);
            op.appendChild(Euser);
            Element type = document.createElement("type");
            type.setTextContent(ty);
            op.appendChild(type);
            Element qte = document.createElement("quantite");
            qte.setTextContent(qt);
            op.appendChild(qte);
            Element ref = document.createElement("reference");
            ref.setTextContent(refe);
            op.appendChild(ref);
            Element comm = document.createElement("commentaire");
            comm.setTextContent(com);
            op.appendChild(comm);
            document.appendChild(racine);
            serveur.deposer_Msg(document);
        } catch (RemoteException e) {
            JOptionPane.showConfirmDialog(null, "Le serveur est actuellement indisponible", "Erreur", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE);
            parent.setAnnulerEnvoiMsg(true);
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
