package utils;

public class LatLngConvert {
	public static int[] toDMS(float value) {//תΪ�ȷ��뵥λ
		int[] result = new int[3];
		for(int i = 0;i<3;i++)
		{
			result[i] = (int)value;
			value = (value - result[i])*60;
		}
		return result;
	}
}
