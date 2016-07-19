package project2;

public class Heaps {
	static int[] array = {1, 14, 9, 8, 7, 9, 3, 2, 4, 16};
	public static void main(String args[]){
		Heaps h = new Heaps();
		System.out.println("Initial:");
		h.print(array);
		h.bulidMaxHeap(array);
		System.out.println("\nMax Heapify:");
		h.print(array);
		h.bulidMinHeap(array);
		System.out.println("\nMin Heapify:");
		h.print(array);
	}

	private void print(int[] array) {
		for(int i=0;i<array.length;i++)
			System.out.print(array[i]+" ");
	}

	private void bulidMaxHeap(int[] array) {
		int size = array.length/2;
		for(int index=size;index>=0;index--){
			maxHeapify(array, index);
		}
	}

	private void maxHeapify(int[] array, int index) {
		int l = left(index);
		int r = right(index);
		int largest;
		if(l< array.length && array[l]>array[index])
			largest = l;
		else
			largest = index;
		if(r < array.length && array[r]>array[largest])
			largest = r;
		if(largest != index){
			int temp = array[index];
			array[index] = array[largest];
			array[largest] = temp;
			maxHeapify(array, largest);
		}
	}
	
	private void bulidMinHeap(int[] array) {
		int size = array.length/2;
		for(int index=size-1;index>=0;index--){
			minHeapify(array, index);
		}
	}
	
	private void minHeapify(int[] array, int index) {
		int l = left(index);
		int r = right(index);
		int smallest;
		if(l<array.length && array[l]<array[index])
			smallest = l;
		else
			smallest = index;
		if(r < array.length && array[r]<array[smallest])
			smallest = r;
		if(smallest != index){
			int temp = array[index];
			array[index] = array[smallest];
			array[smallest] = temp;
			minHeapify(array, smallest);
		}
	}

	private int right(int index) {
		return 2*index+2;
	}

	private int left(int index) {
		return 2*index+1;
	}
}
