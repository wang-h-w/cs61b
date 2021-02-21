public class HelloNumbers {
	public static void main(String[] args) {
		int x = 0; // 先声明变量类型
		while (x < 10) {
			System.out.println(x);
			x = x + 1;
		}
	}
}

/*
1. Before Java variables can be used, they must be declared. 变量必须先声明
2. Java variables must have a specific type. 变量必须有明确的类型
3. Java variable types can never change. 变量的类型不可更改
4. Types are verified before the code even runs!!! 编译的时候就会检查类型，如果错误则不会运行程序
*/