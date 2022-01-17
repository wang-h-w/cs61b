public class WindowSum
{
	public static void windowPosSum(int[] a, int n) {
		for (int i=0; i<a.length; i++) {
			int temp = 0;
			if (a[i] < 0)
				continue;
			for (int j = i; j <= i+n; j++) {
				if (j >= a.length)
					break;
				temp = temp + a[j];
			}
			a[i] = temp;
		}
	}

	public static void main(String[] args) {

		int[] a = {1, 2, -3, 4, 5, 4};
		int n1 = 3;
		windowPosSum(a, n1);
		// Should print 4, 8, -3, 13, 9, 4
		System.out.println(java.util.Arrays.toString(a));

		int[] b = {1, -1, -1, 10, 5, -1};
		int n2 = 2;
		windowPosSum(b, n2);
		// Should print -1, -1, -1, 14, 4, -1
		System.out.println(java.util.Arrays.toString(b));
	}
}