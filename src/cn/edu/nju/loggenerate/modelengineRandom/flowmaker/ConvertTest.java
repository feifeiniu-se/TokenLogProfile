package cn.edu.nju.loggenerate.modelengineRandom.flowmaker;

public class ConvertTest {

	public static void main(String[] args) {
		String sourceFolder = "C:\\Users\\Chuanyi Li\\Documents\\Research\\TAR\\Data\\ModelsB\\25.4";
		String desFolder = "C:\\Users\\Chuanyi Li\\Documents\\Research\\TAR\\Data\\ModelsP\\25.4";
		
		ConvertBeehiveZPIPE.fromBeehiveZtoPIPE(sourceFolder, desFolder);
		
//		String sourceFolder = "C:\\Users\\Chuanyi Li\\Documents\\Research\\TAR\\Data\\Models\\PIPE";
//		String desFolder = "C:\\Users\\Chuanyi Li\\Documents\\Research\\TAR\\Data\\Models\\BeehiveZ";
//		
//		ConvertBeehiveZPIPE.fromPIPEtoBeehiveZ(sourceFolder, desFolder);
	}
}
