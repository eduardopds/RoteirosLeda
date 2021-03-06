package adt.stack;



public class StackImpl<T> implements Stack<T> {

	private T[] array;
	private int top;
	private int tail;
	
	@SuppressWarnings("unchecked")
	public StackImpl(int size) {
		array = (T[]) new Object[size];
		top = -1;
	}

	@Override
	public T top() {
		T element = null;
		if (!isEmpty()) {
			element = array[this.top];

		}

		return element;
	}

	@Override
	public boolean isEmpty() {
		return (this.top == -1);
	}

	@Override
	public boolean isFull() {
		return (this.top == array.length - 1);
	}

	@Override
	public void push(T element) throws StackOverflowException {
		if (!isFull()) {
			if (element != null) {
				this.top++;
				array[this.top] = element;
			}

		} else {
			throw new StackOverflowException();
		}

	}

	@Override
	public T pop() throws StackUnderflowException {

		if (isEmpty()) {
			throw new StackUnderflowException();
		}
		T element = array[this.top];
		top--;
		return element;
	}

}