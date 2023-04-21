    public boolean validateReader(int readerID) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("validateReader", true);
            $out.write_ulong(readerID);
            $in = _invoke($out);
            boolean $result = $in.read_boolean();
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return validateReader(readerID);
        } finally {
            _releaseReply($in);
        }
    }
