package adt.avltree;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import adt.bst.BSTImpl;
import adt.bst.BSTNode;
import adt.bt.Util;

/**
 * 
 * Performs consistency validations within a AVL Tree instance
 * 
 * @author Claudio Campelo
 *
 * @param <T>
 */
public class AVLTreeImpl<T extends Comparable<T>> extends BSTImpl<T> implements AVLTree<T> {

	public static void main(String[] args) {

		AVLTreeImpl d = new AVLTreeImpl<>();
	d.insert(11);
		d.insert(12);
		d.insert(13);
		d.insert(14);
		
		d.insert(8);
		d.insert(9);
		d.insert(10);
		d.insert(5);
		d.insert(6);
		d.insert(7);
		d.insert(15);
	d.insert(1);
		d.insert(2);
		d.insert(3);
		d.insert(4);
		System.out.println(Arrays.toString(d.preOrder()));

	}

	// AUXILIARY
	protected int calculateBalance(BSTNode<T> node) {
		return height((BSTNode<T>) node.getLeft()) - height((BSTNode<T>) node.getRight());

	}

	// AUXILIARY
	protected void rebalance(BSTNode<T> node) {
		int balance = this.calculateBalance(node);

		if (balance > 1) {
			int balanceLeft = this.calculateBalance((BSTNode<T>) node.getLeft());
			if (balanceLeft > 0) {
				Util.rightRotation(node);

			} else if (balanceLeft <= 0) {
				Util.leftRotation((BSTNode<T>) node.getLeft());
				Util.rightRotation(node);

			}

			if (this.getRoot().equals(node)) {
				this.root = (BSTNode<T>) node.getParent();
			}

		}
		if (balance < -1) {
			int balanceRight = this.calculateBalance((BSTNode<T>) node.getRight());
			if (balanceRight < 0) {
				Util.leftRotation(node);

			} else if (balanceRight >= 0) {
				Util.rightRotation((BSTNode<T>) node.getRight());
				Util.leftRotation(node);

			}

			if (this.getRoot().equals(node)) {
				this.root = (BSTNode<T>) node.getParent();
			}

		}
	}

	// AUXILIARY
	protected void rebalanceUp(BSTNode<T> node) {
		BSTNode<T> parent = (BSTNode<T>) node.getParent();
		while (parent != null) {
			rebalance(parent);
			parent = (BSTNode<T>) parent.getParent();
		}
	}

	@Override
	public void insert(T element) {
		if (element != null) {
			insert(root, element);
		}
	}

	@Override
	public void insert(BSTNode<T> node, T element) {
		if (node.isEmpty()) {

			node.setData(element);
			node.setRight(new BSTNode<>());
			node.setLeft(new BSTNode<>());
			if (node.getParent() == null) {
				this.root = node;
			}
			node.getRight().setParent(node);
			node.getLeft().setParent(node);
		} else {
			if (element.compareTo(node.getData()) > 0) {
				insert((BSTNode<T>) node.getRight(), element);
			} else if (element.compareTo(node.getData()) < 0) {
				insert((BSTNode<T>) node.getLeft(), element);
			}
			rebalance(node);
		}

	}

	@Override
	public void remove(T element) {
		BSTNode<T> node = search(element);
		if (!node.isEmpty()) {

			if (node.isLeaf()) {
				node.setData(null);
				rebalanceUp(node);
			}

			else if (node.getLeft().isEmpty() || node.getRight().isEmpty()) {
				if (node.getParent() != null) {
					if (!node.getParent().getLeft().equals(node)) {
						if (!node.getLeft().isEmpty()) {
							node.getParent().setRight(node.getLeft());
							node.getLeft().setParent(node.getParent());
						} else {
							node.getParent().setRight(node.getRight());
							node.getRight().setParent(node.getParent());
						}
					} else {
						if (!node.getLeft().isEmpty()) {
							node.getParent().setLeft(node.getLeft());
							node.getLeft().setParent(node.getParent());
						} else {
							node.getParent().setLeft(node.getRight());
							node.getRight().setParent(node.getParent());
						}
					}
				} else {
					if (node.getLeft().isEmpty()) {
						root = (BSTNode<T>) node.getRight();
					} else {
						root = (BSTNode<T>) node.getLeft();
					}
					root.setParent(null);
				}
				rebalanceUp(node);

			} else {
				T suc = sucessor(node.getData()).getData();
				remove(suc);
				node.setData(suc);

			}
		}
	}

	public T[] levelOrder() {
		T[] array = (T[]) new Comparable[this.size()];
		if (this.isEmpty())
			return array;
		levelOrder(array, root, 0);
		return array;
	}

	private void levelOrder(T[] array, BSTNode<T> node, int i) {
		Deque<BSTNode<T>> fila = new ArrayDeque<>();
		fila.add(node);
		while (!fila.isEmpty()) {
			BSTNode<T> atual = fila.removeFirst();
			array[i++] = atual.getData();
			if (!atual.getLeft().isEmpty()) {
				fila.add((BSTNode<T>) atual.getLeft());
			}
			if (!atual.getRight().isEmpty()) {
				fila.add((BSTNode<T>) atual.getRight());
			}
		}

	}

}
