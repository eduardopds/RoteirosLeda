package adt.heap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import util.Util;

/**
 * O comportamento de qualquer heap é definido pelo heapify. Neste caso o
 * heapify dessa heap deve comparar os elementos e colocar o maior sempre no
 * topo. Ou seja, admitindo um comparador normal (responde corretamente 3 > 2),
 * essa heap deixa os elementos maiores no topo. Essa comparação não é feita
 * diretamente com os elementos armazenados, mas sim usando um comparator. Dessa
 * forma, dependendo do comparator, a heap pode funcionar como uma max-heap ou
 * min-heap.
 */
public class HeapImpl<T extends Comparable<T>> implements Heap<T> {

	public static void main(String[] args) {
		// minHeap
		HeapImpl<Integer> heapAux = new HeapImpl<Integer>((o1, o2) -> o2 - o1);

		heapAux.buildHeap( new Integer[] {4,-4,6,2,74,8,65});
	}

	protected T[] heap;
	protected int index = -1;
	/**
	 * O comparador é utilizado para fazer as comparações da heap. O ideal é mudar
	 * apenas o comparator e mandar reordenar a heap usando esse comparator. Assim
	 * os metodos da heap não precisam saber se vai funcionar como max-heap ou
	 * min-heap.
	 */
	protected Comparator<T> comparator;

	private static final int INITIAL_SIZE = 20;
	private static final int INCREASING_FACTOR = 10;

	/**
	 * Construtor da classe. Note que de inicio a heap funciona como uma min-heap.
	 */
	@SuppressWarnings("unchecked")
	public HeapImpl(Comparator<T> comparator) {
		this.heap = (T[]) (new Comparable[INITIAL_SIZE]);
		this.comparator = comparator;
	}

	// /////////////////// METODOS IMPLEMENTADOS
	private int parent(int i) {
		return (i - 1) / 2;
	}

	/**
	 * Deve retornar o indice que representa o filho a esquerda do elemento indexado
	 * pela posicao i no vetor
	 */
	private int left(int i) {
		return (i * 2 + 1);
	}

	/**
	 * Deve retornar o indice que representa o filho a direita do elemento indexado
	 * pela posicao i no vetor
	 */
	private int right(int i) {
		return (i * 2 + 1) + 1;
	}

	@Override
	public boolean isEmpty() {
		return (index == -1);
	}

	@Override
	public T[] toArray() {
		ArrayList<T> resp = new ArrayList<T>();
		for (T elem : this.heap) {
			if (elem != null) {
				resp.add(elem);
			}
		}
		return (T[]) resp.toArray(new Comparable[0]);
	}

	// ///////////// METODOS A IMPLEMENTAR
	/**
	 * Valida o invariante de uma heap a partir de determinada posicao, que pode ser
	 * a raiz da heap ou de uma sub-heap. O heapify deve colocar os maiores
	 * (comparados usando o comparator) elementos na parte de cima da heap.
	 */
	private void heapify(int position) {
		int l, r;
		int largest = position;
		l = left(position);
		r = right(position);
		if (l <= index && comparator.compare(heap[l], heap[position]) > 0) {
			largest = l;
		}
		if (r <= index && comparator.compare(heap[r], heap[largest]) > 0) {
			largest = r;
		}
		if (largest != position) {
			Util.swap(heap, position, largest);
			heapify(largest);
		}
	}

	@Override
	public void insert(T element) {
		// ESSE CODIGO E PARA A HEAP CRESCER SE FOR PRECISO. NAO MODIFIQUE
		if (index == heap.length - 1) {
			heap = Arrays.copyOf(heap, heap.length + INCREASING_FACTOR);
		}

		int i = size();
		while (i > 0 && comparator.compare(heap[parent(i)], element) < 0) {
			heap[i] = heap[parent(i)];
			i = parent(i);

		}
		heap[i] = element;
		index++;

	}

	@Override
	public void buildHeap(T[] array) {
		heap = array;
		index = array.length - 1;
		for (int i = array.length / 2; i >= 0; i--) {
			heapify(i);

		}
	}

	@Override
	public T extractRootElement() {
		T root = null;
		if (size() > 0) {
			root = heap[0];
			heap[0] = heap[index];
			heap[index] = null;
			index--;
			heapify(0);
		}

		return root;

	}

	@Override
	public T rootElement() {
		T root = null;
		if (size() > 0) {
			root = heap[0];

		}
		return root;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public T[] heapsort(T[] array) {

		// abordagem 1 com min heap.
		// HeapImpl heapAux = new HeapImpl<Integer>((o1, o2) -> o2 - o1);
		// for (int i = 0; i < array.length; i++) {
		// heapAux.insert(array[i]);
		// }
		//
		// for (int i = 0; i < array.length; i++) {
		// array[i] = (T) heapAux.extractRootElement();
		//
		// }
		//
		// return array;

		// abordagem 1 com max heap

		// HeapImpl heapAux = new HeapImpl<Integer>((o1, o2) -> o1 - o2);

		// for (int i = 0; i < array.length; i++) {
		// heapAux.insert(array[i]);
		// }
		// for (int i = heapAux.size() - 1; i >= 0; i--) {
		// array[i] = (T) heapAux.extractRootElement();

		// }
		// return array;

		// abordagem 2(In-place) Max Heap

		HeapImpl heapAux = new HeapImpl<Integer>((o1, o2) -> o1 - o2);
		heapAux.buildHeap(array);
		for (int k = array.length - 1; k >= 0; k--) {

			// Move current root to end
			Util.swap(array, 0, k);
			heapAux.index--;
			heapAux.heapify(0);
		}
		return array;

	}

	@Override
	public int size() {
		return this.index + 1;
	}

	public Comparator<T> getComparator() {
		return comparator;
	}

	public void setComparator(Comparator<T> comparator) {
		this.comparator = comparator;
	}

	public T[] getHeap() {
		return heap;
	}

	// Breadth-first search (BFS) ou Encaminhamento em Largura é uma forma de
	// percorrer uma árvore visitando os nós vizinhos de um determinado nível desta
	// árvore.
	public List<T> elementsByLevel(int level) {
		if (level > this.index || level < 0) {
			throw new RuntimeException("Level inexistente");
		}
		// 2 ^ level - 1
		int comecoLevel = (int) (Math.pow(2, level) - 1);
		int levelFinal = comecoLevel * 2;
		List<T> elementosPorLevel = new ArrayList<T>();

		for (int i = comecoLevel; i <= levelFinal && heap[i] != null; i++) {

			elementosPorLevel.add(heap[i]);
		}

		return elementosPorLevel;

	}

	// Merge de dois arrays usando heap min
	@SuppressWarnings("unchecked")
	public T[] merge(T[] arrayA, T[] arrayB) {

		// Ordem crescente (min Heap)
		HeapImpl<T> heapAux = new HeapImpl<T>((o1, o2) -> (Integer) o2 - (Integer) o1);
		// insere os elementos da primeira lista
		for (T element : arrayA) {

			heapAux.insert(element);

		}

		// insere os elementos da segunda lista
		for (T element : arrayB) {

			heapAux.insert(element);

		}

		T[] arrayC = (T[]) new Comparable[heapAux.size()];

		// Retira da heap e coloca no arrayC
		for (int index = 0; index < arrayC.length; index++) {

			arrayC[index] = heapAux.extractRootElement();

		}

		return arrayC;

	}

	private int calculaQuantidadeNiveis() {
		return (int) (Math.log((size() + 1) / 2) / Math.log(2));
	}

}
