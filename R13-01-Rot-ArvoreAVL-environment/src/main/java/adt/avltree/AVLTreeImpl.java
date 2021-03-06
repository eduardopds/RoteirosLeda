package adt.avltree;

import java.util.Arrays;

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
		AVLTreeImpl<Integer> tree = new AVLTreeImpl<>();
		tree.insert(2);

		tree.insert(87);

		tree.insert(44);


	}

	public T estatisticadeOrdem(int n) {
		T estatistica = null;
		if (n <= size()) {
			if (n == 1) {
				estatistica = minimum().getData();
			} else {
				T value = minimum().getData();
				while (n > 1) {
					BSTNode<T> node = sucessor(value);
					value = node.getData();
					n--;

				}
				estatistica = value;
			}

		}
		return estatistica;

	}

	// AUXILIARY
	protected int calculateBalance(BSTNode<T> node) {
		return height((BSTNode<T>) node.getLeft()) - height((BSTNode<T>) node.getRight());

	}

	// AUXILIARY
	protected void rebalance(BSTNode<T> node) {
		int balance = this.calculateBalance(node);

		if (!node.isEmpty() && !(Math.abs(balance) <= 1)) {

			// no pesa pra esquerda
			if (balance > 0) {
				int balanceLeft = this.calculateBalance((BSTNode<T>) node.getLeft());
				// filho a esquerda pesa pra direita
				if (balanceLeft < 0) {
					Util.leftRotation((BSTNode<T>) node.getLeft());
				}
				Util.rightRotation(node);

			} else {
				// no pesa pra direita
				int balanceRight = this.calculateBalance((BSTNode<T>) node.getRight());
				// filho a direita pesa pra esquerda
				if (balanceRight > 0) {

					Util.rightRotation((BSTNode<T>) node.getRight());
				}
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
}