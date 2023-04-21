    private org.omg.CORBA.portable.OutputStream _OB_op_mask_type(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream out = null;
        try {
            String _ob_a0 = org.omg.CosTrading.ServiceTypeNameHelper.read(in);
            mask_type(_ob_a0);
            out = handler.createReply();
        } catch (org.omg.CosTrading.IllegalServiceType _ob_ex) {
            out = handler.createExceptionReply();
            org.omg.CosTrading.IllegalServiceTypeHelper.write(out, _ob_ex);
        } catch (org.omg.CosTrading.UnknownServiceType _ob_ex) {
            out = handler.createExceptionReply();
            org.omg.CosTrading.UnknownServiceTypeHelper.write(out, _ob_ex);
        } catch (org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.AlreadyMasked _ob_ex) {
            out = handler.createExceptionReply();
            org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.AlreadyMaskedHelper.write(out, _ob_ex);
        }
        return out;
    }
