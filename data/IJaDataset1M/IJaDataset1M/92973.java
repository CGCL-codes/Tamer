package DefaultNamespace;

public interface MetaData_PortType extends java.rmi.Remote {

    public java.lang.String getMetaInformationBySample(int in0) throws java.rmi.RemoteException;

    public void uploadResult(int in0, byte[] in1) throws java.rmi.RemoteException;

    public int getClassIdForSample(int in0) throws java.rmi.RemoteException;

    public java.lang.String getSpeciesByClass(int in0) throws java.rmi.RemoteException;

    public java.lang.String getOrganByClass(int in0) throws java.rmi.RemoteException;

    public java.lang.String getGenotypeByClass(int in0) throws java.rmi.RemoteException;

    public int getIdForFileName(java.lang.String in0) throws java.rmi.RemoteException;
}
