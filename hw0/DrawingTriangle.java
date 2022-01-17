public class DrawingTriangle
{
	public static void TriGen(int size)
	{
		for (int i=0; i<size; i++)
			System.out.print("*");
		System.out.println();
	}

	public static void main(String[] args)
	{
		for (int i=1; i<=5; i++)
			TriGen(i);
	}
}