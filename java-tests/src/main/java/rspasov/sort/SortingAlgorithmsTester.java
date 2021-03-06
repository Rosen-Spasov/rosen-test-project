package rspasov.sort;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

public class SortingAlgorithmsTester {

	private static boolean printArr = false;

	private static class StopWatch {
		Instant start, end;

		public void start() {
			start = now();
		}

		public void end() {
			end = now();
		}

		private Instant now() {
			return Instant.now();
		}

		public void printDuration() {
			end();
			System.out.println(Duration.between(start, end).getSeconds() + " seconds.");
		}

	}

	private static StopWatch stopWatch = new StopWatch();

	public static void main(String[] args) {
		int size = 100000;

		System.out.print("Heap sort... ");
		int[] arr = generateArray(size);
		stopWatch.start();
		heapSort(arr);
		stopWatch.printDuration();
		checkOrder(arr);

		System.out.print("Merge sort... ");
		arr = generateArray(size);
		stopWatch.start();
		mergeSort(arr);
		stopWatch.printDuration();
		checkOrder(arr);

		System.out.print("Selection sort... ");
		arr = generateArray(size);
		stopWatch.start();
		selectionSort(arr);
		stopWatch.printDuration();
		checkOrder(arr);

		System.out.print("Insertion sort... ");
		arr = generateArray(size);
		stopWatch.start();
		insertionSort(arr);
		stopWatch.printDuration();
		checkOrder(arr);

		System.out.print("Bubble sort... ");
		arr = generateArray(size);
		stopWatch.start();
		bubbleSort(arr);
		stopWatch.printDuration();
		checkOrder(arr);
	}

	private static void print(int[] arr) {
		if (printArr) {
			System.out.println(Arrays.toString(arr));
		}
	}

	private static int[] generateArray(int size) {
		int[] result = new int[size];
		for (int index = 0; index < size; index++) {
			result[index] = (int) Math.round(Math.random() * size);
		}
		print(result);
		return result;
	}

	private static void checkOrder(int[] arr) {
		for (int index = 0; index < arr.length - 1; index++) {
			if (arr[index] > arr[index + 1]) {
				throw new RuntimeException("Array is not sorted at index " + index + ". Array: " + Arrays.toString(arr));
			}
		}
	}

	private static void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
		print(arr);
	}

	public static void heapSort(int[] arr) {
		buildHeap(arr, arr.length);
		int index = arr.length - 1;
		while (index >= 0) {
			swap(arr, 0, index--);
			siftDown(arr, 0, index);
		}
	}

	private static void buildHeap(int[] arr, int length) {
		int index = 0;
		while (index < length) {
			int swapIndex = index;
			// For each element, compare the parent with its left and right
			// child and swap it with the smallest element of them three.
			int childIndex = index * 2 + 1; // Compare with left child
			if (childIndex < length && arr[swapIndex] < arr[childIndex]) {
				swapIndex = childIndex;
			}
			childIndex = index * 2 + 2; // Compare with right child
			if (childIndex < length && arr[swapIndex] < arr[childIndex]) {
				swap(arr, index, childIndex);
				siftUp(arr, index);
			}
			if (swapIndex != index) {
				swap(arr, index, swapIndex);
				siftUp(arr, index);
			} else {
				index++;
			}
		}
		checkHeap(arr, length);
	}

	private static void checkHeap(int[] arr, int length) {
		for (int index = 0; index < length; index++) {
			int childIndex = index * 2 + 1;
			if (childIndex < length && arr[index] < arr[childIndex]) { // Compare
																		// with
																		// left
																		// child
				throw new RuntimeException("Heap is out of order at index " + index + ". Heap: " + Arrays.toString(arr));
			}
			childIndex = index * 2 + 2;
			if (childIndex < length && arr[index] < arr[childIndex]) { // Compare
																		// with
																		// right
																		// child
				throw new RuntimeException("Heap is out of order at index " + index + ". Heap: " + Arrays.toString(arr));
			}
		}
	}

	private static void siftUp(int[] arr, int childIndex) {
		int parentIndex = (childIndex - 1) / 2;
		while (parentIndex >= 0 && arr[parentIndex] < arr[childIndex]) {
			swap(arr, parentIndex, childIndex);
			childIndex = parentIndex;
			parentIndex = (childIndex - 1) / 2;
		}
	}

	private static void siftDown(int[] arr, int parentIndex, int end) {
		int childIndex = parentIndex * 2 + 1;
		while (childIndex <= end) {
			int swapIndex = parentIndex;
			if (arr[swapIndex] < arr[childIndex]) {
				swapIndex = childIndex;
			}
			if (childIndex + 1 <= end && arr[swapIndex] < arr[childIndex + 1]) {
				swapIndex = childIndex + 1;
			}
			if (swapIndex == parentIndex) {
				return;
			} else {
				swap(arr, parentIndex, swapIndex);
				parentIndex = swapIndex;
				childIndex = parentIndex * 2 + 1;
			}
		}
	}

	public static void selectionSort(int[] arr) {
		for (int i = 0; i < arr.length - 1; i++) {
			int minIndex = i;
			for (int j = i + 1; j < arr.length; j++) {
				if (arr[minIndex] > arr[j]) {
					minIndex = j;
				}
			}
			swap(arr, i, minIndex);
		}
	}

	public static void insertionSort(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = i; j > 0 && arr[j - 1] > arr[j]; j--) {
				swap(arr, j - 1, j);
			}
		}
	}

	public static void bubbleSort(int[] arr) {
		int n = arr.length;
		do {
			int lastSwappedIndex = 0;
			for (int i = 1; i < n; i++) {
				if (arr[i - 1] > arr[i]) {
					swap(arr, i - 1, i);
					lastSwappedIndex = i;
				}
			}
			n = lastSwappedIndex;
		} while (n > 0);
	}

	public static void mergeSort(int[] arr) {
		mergeSort(arr, new int[arr.length], 0, arr.length - 1);
	}

	public static void mergeSort(int[] arr, int[] helper, int start, int end) {
		if (start < end) {
			int middle = start + (end - start) / 2;
			mergeSort(arr, helper, start, middle);
			mergeSort(arr, helper, middle + 1, end);
			merge(arr, helper, start, middle, end);
		}
	}

	private static void merge(int[] arr, int[] helper, int start, int middle, int end) {
		for (int i = start; i <= end; i++) {
			helper[i] = arr[i];
		}

		int i = start;
		int j = middle + 1;
		int k = start;

		while (i <= middle && j <= end) {
			if (helper[i] <= helper[j]) {
				arr[k] = helper[i++];
			} else {
				arr[k] = helper[j++];
			}
			k++;
		}

		while (i <= middle) {
			arr[k++] = helper[i++];
		}
	}

}
