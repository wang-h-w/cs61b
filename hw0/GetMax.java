public class GetMax
{
	public static int max(int[] m){
		int maxNum = 0;
		for (int idx=0; idx<m.length; idx++)
			if (m[idx] >= maxNum)
				maxNum = m[idx];
		return maxNum;
	}

	public static void main(String[] args) {
		int[] numbers = new int[] {9, 2, 15, 2, 22, 10, 6};
		int maxNum = max(numbers);
		System.out.println("The max number is: " + maxNum);
	}
}