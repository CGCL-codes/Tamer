    public static void main(String[] args) {
        vtkFileOutputWindow outWin = new vtkFileOutputWindow();
        outWin.SetInstance(outWin);
        outWin.SetFileName("MVSVTKViewer.log");
        vtkGDCMTesting t = new vtkGDCMTesting();
        String directory = t.GetGDCMDataRoot();
        String file0 = directory + "/SIEMENS_MAGNETOM-12-MONO2-FileSeq0.dcm";
        String file1 = directory + "/SIEMENS_MAGNETOM-12-MONO2-FileSeq1.dcm";
        String file2 = directory + "/SIEMENS_MAGNETOM-12-MONO2-FileSeq2.dcm";
        String file3 = directory + "/SIEMENS_MAGNETOM-12-MONO2-FileSeq3.dcm";
        vtkStringArray s = new vtkStringArray();
        System.out.println("adding : " + file0);
        s.InsertNextValue(file0);
        s.InsertNextValue(file1);
        s.InsertNextValue(file2);
        s.InsertNextValue(file3);
        vtkGDCMImageReader reader = new vtkGDCMImageReader();
        reader.SetFileNames(s);
        reader.Update();
        System.out.println("Success reading: " + file0);
        vtkMetaImageWriter writer = new vtkMetaImageWriter();
        writer.DebugOn();
        writer.SetCompression(false);
        writer.SetInput(reader.GetOutput());
        writer.SetFileName("ReadSeriesIntoVTK.mhd");
        writer.Write();
        System.out.println("Success writing: " + writer.GetFileName());
    }
