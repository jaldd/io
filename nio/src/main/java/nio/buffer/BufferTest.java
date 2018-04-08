package nio.buffer;

import java.nio.IntBuffer;

public class BufferTest {

	public BufferTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		IntBuffer buff = IntBuffer.allocate(10);
		int[] array = new int[] { 3, 5, 1 };
		buff = buff.wrap(array);
		// buff=buff.wrap(array,0,2);
		buff.put(0, 7);
		for (int i = 0; i < buff.limit(); i++) {
			System.out.println(buff.get());
		}
		for(int a:array) {
			System.out.println(a);
		}
		buff.flip();
//		buff.clear();
		IntBuffer buff2=buff.duplicate();
		System.out.println(buff2);
	}
}
