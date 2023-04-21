    public static void main(String[] args) {
        String filename = args[0];
        vtkGDCMImageReader reader = new vtkGDCMImageReader();
        reader.SetFileName(filename);
        reader.Update();
        vtkMedicalImageProperties prop = reader.GetMedicalImageProperties();
        System.out.println(prop.GetPatientName());
        vtkMatrix4x4 dircos = reader.GetDirectionCosines();
        dircos.Invert();
        double[] cosines = new double[6];
        cosines[0] = dircos.GetElement(0, 0);
        cosines[1] = dircos.GetElement(1, 0);
        cosines[2] = dircos.GetElement(2, 0);
        cosines[3] = dircos.GetElement(0, 1);
        cosines[4] = dircos.GetElement(1, 1);
        cosines[5] = dircos.GetElement(2, 1);
        reader.GetMedicalImageProperties().SetDirectionCosine(cosines);
        String outfilename = args[1];
        vtkGDCMImageWriter writer = new vtkGDCMImageWriter();
        writer.SetMedicalImageProperties(reader.GetMedicalImageProperties());
        writer.SetDirectionCosines(dircos);
        writer.SetShift(reader.GetShift());
        writer.SetScale(reader.GetScale());
        writer.SetImageFormat(reader.GetImageFormat());
        writer.SetFileName(outfilename);
        writer.SetInput(reader.GetOutput());
        writer.Write();
        System.out.println("Success reading: " + filename);
    }
