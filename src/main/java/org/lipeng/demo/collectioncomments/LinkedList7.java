package org.lipeng.demo.collectioncomments;

import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * List接口的双端链表实现LinkedList 7
 * List 接口的链接列表实现。实现所有可选的列表操作，并且允许所有元素（包括 null）。除了实现 List 接口外，
 * LinkedList 类还为在列表的开头及结尾 get、remove 和 insert 元素提供了统一的命名方法。这些操作允许将链接列表用作堆栈、队列或双端队列。
	此类实现 Deque 接口，为 add、poll 提供先进先出队列操作，以及其他堆栈和双端队列操作。
	所有操作都是按照双重链接列表的需要执行的。在列表中编索引的操作将从开头或结尾遍历列表（从靠近指定索引的一端）。
	注意，此实现不是同步的。如果多个线程同时访问一个链接列表，而其中至少一个线程从结构上修改了该列表，则它必须 保持外部同步。
	（结构修改指添加或删除一个或多个元素的任何操作；仅设置元素的值不是结构修改。）这一般通过对自然封装该列表的对象进行同步操作来完成。
	如果不存在这样的对象，则应该使用 Collections.synchronizedList 方法来“包装”该列表。最好在创建时完成这一操作，
	以防止对列表进行意外的不同步访问，如下所示：
    List list = Collections.synchronizedList(new LinkedList(...));
	此类的 iterator 和 listIterator 方法返回的迭代器是快速失败 的：在迭代器创建之后，如果从结构上对列表进行修改，
	除非通过迭代器自身的 remove 或 add 方法，其他任何时间任何方式的修改，迭代器都将抛出 ConcurrentModificationException。
	因此，面对并发的修改，迭代器很快就会完全失败，而不冒将来不确定的时间任意发生不确定行为的风险。
	注意，迭代器的快速失败行为不能得到保证，一般来说，存在不同步的并发修改时，不可能作出任何硬性保证。
	快速失败迭代器尽最大努力抛出 ConcurrentModificationException。因此，编写依赖于此异常的程序的方式是错误的，
	正确做法是：迭代器的快速失败行为应该仅用于检测程序错误。
 */

public class LinkedList7<E> extends AbstractSequentialList<E> implements List<E>, Deque<E>, Cloneable, java.io.Serializable {
	/**
	 *  @Question 为什么是transient的
	 */
	transient int size = 0;

	/**
	 *首节点
	 * Invariant: (first == null && last == null) ||
	 *            (first.prev == null && first.item != null)
	 */
	transient Node<E> first;

	/**
	 *最后一个节点
	 * Invariant: (first == null && last == null) ||
	 *            (last.next == null && last.item != null)
	 */
	transient Node<E> last;

	public LinkedList7() {
	}

	/**
	 * Constructs a list containing the elements of the specified
	 * collection, in the order they are returned by the collection's
	 * iterator.
	 *
	 * @param  c the collection whose elements are to be placed into this list
	 * @throws NullPointerException if the specified collection is null
	 */
	public LinkedList7(Collection<? extends E> c) {
		this();
		addAll(c);
	}

	/**
	 * 将指定元素添加到链表开头
	 */
	private void linkFirst(E e) {
		// 添加前将当前的首节点赋予f
		final Node<E> f = first;
		// 创建一个新的节点，他的上一个节点是null,下个节点是f
		final Node<E> newNode = new Node<>(null, e, f);
		// first指针指向将新创建的节点
		first = newNode;
		if (f == null)
			// 如果f为null,则新创建的节点同时为尾节点，last指针指向他
			last = newNode;
		else
			// 如果f不为null,则将他的prev指向新创建的节点
			f.prev = newNode;
		// size+1
		size++;
		// 版本+1
		modCount++;
	}

	/**
	 * 将指定元素添加到链表末尾
	 */
	void linkLast(E e) {
		// 添加前将当前的最后一个节点赋予l
		final Node<E> l = last;
		// 创建一个新的节点，他的上一个节点是l,下个节点是null
		final Node<E> newNode = new Node<>(l, e, null);
		// last指针指向将新创建的节点
		last = newNode;
		if (l == null)
			// 如果l为null,则新创建的节点同时为首节点，first指针指向他
			first = newNode;
		else
			// 如果l不为null,则将他的next指向新创建的节点
			l.next = newNode;
		// size+1
		size++;
		// 版本+1
		modCount++;
	}

	/**
	 * 在一个非null节点前添加元素
	 */
	void linkBefore(E e, Node<E> succ) {
		// 先将succ的上一个节点赋予pred
		final Node<E> pred = succ.prev;
		// 创建一个新的节点，他的上一个节点是pred,下一个节点是succ
		final Node<E> newNode = new Node<>(pred, e, succ);
		// 将succ的上一个节点指向新节点
		succ.prev = newNode;
		if (pred == null)
			// 如果pred为null,那么将新创建的节点设置为头节点
			first = newNode;
		else
			// 如果pred不为null，那么将pred的下一个节点指向新创建的节点
			pred.next = newNode;
		// size+1
		size++;
		// 版本+1
		modCount++;
	}

	/**
	 * 分离首节点（即将首节点脱离链表），并返回元素
	 */
	private E unlinkFirst(Node<E> f) {
		// 首节点元素
		final E element = f.item;
		// 下个节点
		final Node<E> next = f.next;
		// 首节点的元素和next指针指向null, help GC
		f.item = null;
		f.next = null; // help GC
		// 将next节点作为新的首节点，赋予first指针
		first = next;
		if (next == null)
			// 如果next节为null,将last指针指向null,(原先last指针和first指针指向同一个节点)
			last = null;
		else
			// 如果next节点不为null,则作为首节点，他的上一个节点为null,即prev指向null
			next.prev = null;
		size--;
		modCount++;
		return element;
	}

	/**
	 * 分离尾节点（即将尾节点脱离链表），并返回元素
	 */
	private E unlinkLast(Node<E> l) {
		// 尾节点元素
		final E element = l.item;
		// 尾节点的前一个节点
		final Node<E> prev = l.prev;
		// 尾节点的元素和prev指针指向null, help GC
		l.item = null;
		l.prev = null; // help GC
		// 将prev节点作为新的尾节点，赋予last指针
		last = prev;
		if (prev == null)
			// 如果prev节为null,将first指针指向null,(原先last指针和first指针指向同一个节点)
			first = null;
		else
			// 如果prev节点不为null,则作为尾节点，他的下一个节点为null,即next指向null
			prev.next = null;
		size--;
		modCount++;
		return element;
	}

	/**
	 * 将一个节点脱离链表，并返回元素
	 */
	E unlink(Node<E> x) {
		final E element = x.item;
		final Node<E> next = x.next;
		final Node<E> prev = x.prev;

		if (prev == null) {
			// 如果x是首节点
			// 那么将首节点指针指向next节点
			first = next;
		} else {
			// 如果x不是首节点
			prev.next = next;
			// 将x.prev指向null,
			x.prev = null;
		}

		if (next == null) {
			// 如果x是尾节点
			// 那么将last指针指向prev节点
			last = prev;
		} else {
			// 如果x不是尾节点
			next.prev = prev;
			x.next = null;
		}

		x.item = null;
		size--;
		modCount++;
		return element;
	}

	/**
	 * 返回链表的第一个元素，如果第一个节点为null,抛出NoSuchElementException异常
	 */
	public E getFirst() {
		final Node<E> f = first;
		if (f == null)
			throw new NoSuchElementException();
		return f.item;
	}

	/**
	 *	返回链表的最后一个元素,如果最后一个节点为null,抛出NoSuchElementException异常
	 */
	public E getLast() {
		final Node<E> l = last;
		if (l == null)
			throw new NoSuchElementException();
		return l.item;
	}

	/**
	 *	删除第一个元素 ，如果第一个节点为null,抛出NoSuchElementException异常
	 */
	public E removeFirst() {
		final Node<E> f = first;
		if (f == null)
			throw new NoSuchElementException();
		return unlinkFirst(f);
	}

	/**
	 * 删除最后一个元素,如果最后一个节点为null,抛出NoSuchElementException异常
	 */
	public E removeLast() {
		final Node<E> l = last;
		if (l == null)
			throw new NoSuchElementException();
		return unlinkLast(l);
	}

	/**
	 * 将指定元素添加到链表开头
	 */
	public void addFirst(E e) {
		linkFirst(e);
	}

	/**
	 * 将指定元素添加到链表末尾
	 */
	public void addLast(E e) {
		linkLast(e);
	}

	public boolean contains(Object o) {
		return indexOf(o) != -1;
	}

	public int size() {
		return size;
	}

	/**
	 * 将指定元素添加到链表末尾
	 */
	public boolean add(E e) {
		linkLast(e);
		return true;
	}

	/**
	 * 删除指定元素，如果存在的话
	 */
	public boolean remove(Object o) {
		if (o == null) {
			for (Node<E> x = first; x != null; x = x.next) {
				if (x.item == null) {
					unlink(x);
					return true;
				}
			}
		} else {
			for (Node<E> x = first; x != null; x = x.next) {
				if (o.equals(x.item)) {
					unlink(x);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 将指定集合中的全部元素添加至链表末尾
	 */
	public boolean addAll(Collection<? extends E> c) {
		return addAll(size, c);
	}

	/**
	 * 将指定集合中的全部元素添加至链表，从index位置开始，注：操作时一定考虑首尾节点
	 */
	public boolean addAll(int index, Collection<? extends E> c) {
		// 检查是否越界
		checkPositionIndex(index);
		// 将集合中的全部元素先存入一个数组
		Object[] a = c.toArray();
		// 数组长度
		int numNew = a.length;
		if (numNew == 0)
			return false;
		// 开始加入链表，先找到index处的节点和他的前一个节点
		Node<E> pred, succ;
		if (index == size) {
			succ = null;
			pred = last;
		} else {
			succ = node(index);
			pred = succ.prev;
		}
		// 遍历数组，将全部元素加入链表
		for (Object o : a) {
			@SuppressWarnings("unchecked")
			E e = (E) o;
			// 创建一个新节点，他的前一个节点是pred，后一个节点是null
			Node<E> newNode = new Node<>(pred, e, null);
			if (pred == null)
				// 如果pred为null,则新创建的节点同时为首节点，first指针指向他
				first = newNode;
			else
				// 如果pred不为null,则将他的next指向新创建的节点
				pred.next = newNode;
			// 将pred指针指向新创建的节点
			pred = newNode;
		}

		if (succ == null) {
			// 如果succ为null,则新创建的节点同时为尾节点，last指针指向他
			last = pred;
		} else {
			// 将数组的最后一个元素的节点（pred）的下一个节点的指针指向succ,succ的上一个节点指向pred
			pred.next = succ;
			succ.prev = pred;
		}

		size += numNew;
		modCount++;
		return true;
	}

	/**
	 * Returns an iterator over the elements in this list (in proper
	 * sequence).<p>
	 * <p>
	 * This implementation merely returns a list iterator over the list.
	 *
	 * @return an iterator over the elements in this list (in proper sequence)
	 */
	@Override
	public Iterator<E> iterator() {
		return super.iterator();
	}

	/**
	 * 移除链表的全部元素
	 */
	public void clear() {
		// @Question 注释是什么意思？
		// Clearing all of the links between nodes is "unnecessary", but:
		// - helps a generational GC if the discarded nodes inhabit
		// more than one generation
		// - is sure to free memory even if there is a reachable Iterator

		// 遍历每个节点，使他的节点的元素为null,prev和next指针都指向null
		for (Node<E> x = first; x != null;) {
			Node<E> next = x.next;
			x.item = null;
			x.next = null;
			x.prev = null;
			x = next;
		}
		// 将头节点和尾节点指向null
		first = last = null;
		size = 0;
		modCount++;
	}

	/*********************指定位置index的节点的操作************************/
	/**
	 * 获取指定位置的元素
	 */
	public E get(int index) {
		// 判断是否越界
		checkElementIndex(index);
		return node(index).item;
	}

	/**
	 * 替换指定位置的元素
	 */
	public E set(int index, E element) {
		// 检查是否越界
		checkElementIndex(index);
		// 找到指定位置的非null节点
		Node<E> x = node(index);
		E oldVal = x.item;
		// 设置元素
		x.item = element;
		// 将原有的元素返回
		return oldVal;
	}

	/**
	 * 将指定元素添加到链表的指定位置
	 */
	public void add(int index, E element) {

		checkPositionIndex(index);
		if (index == size)
			// 如果index=size,则将元素插入末尾
			linkLast(element);
		else
			// 先查到index位置对应的节点，然后再添加element到该节点的前面
			linkBefore(element, node(index));
	}

	/**
	 * 删除指定位置的元素，然后将后面的全部元素向左移动一位
	 */
	public E remove(int index) {
		checkElementIndex(index);
		return unlink(node(index));
	}

	/**
	 *判断索引是否在0到size-1之间
	 */
	private boolean isElementIndex(int index) {
		return index >= 0 && index < size;
	}

	/**
	 * 判断索引是否在0到size之间
	 */
	private boolean isPositionIndex(int index) {
		return index >= 0 && index <= size;
	}

	/**
	 * Constructs an IndexOutOfBoundsException detail message.
	 * Of the many possible refactorings of the error handling code,
	 * this "outlining" performs best with both server and client VMs.
	 */
	private String outOfBoundsMsg(int index) {
		return "Index: " + index + ", Size: " + size;
	}

	private void checkElementIndex(int index) {
		if (!isElementIndex(index))
			throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
	}

	private void checkPositionIndex(int index) {
		if (!isPositionIndex(index))
			throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
	}

	/**
	 *获取指定位置的节点
	 */
	Node<E> node(int index) {
		// 如果要查找的位置在列表的前半部分，从首节点开始向后遍历，直到找到第index个节点
		if (index < (size >> 1)) {
			Node<E> x = first;
			for (int i = 0; i < index; i++)
				x = x.next;
			return x;
		} else {
			// 如果要查找的位置在列表的后半部分，从尾节点开始想钱遍历，直到找到第index个节点
			Node<E> x = last;
			for (int i = size - 1; i > index; i--)
				x = x.prev;
			return x;
		}
	}

	/*************************** 查找元素操作***************************/

	/**
	 * 查找元素第一次出现的位置
	 */
	public int indexOf(Object o) {
		int index = 0;
		if (o == null) {
			for (Node<E> x = first; x != null; x = x.next) {
				if (x.item == null)
					return index;
				index++;
			}
		} else {
			for (Node<E> x = first; x != null; x = x.next) {
				if (o.equals(x.item))
					return index;
				index++;
			}
		}
		return -1;
	}

	/**
	 * 查找元素最后一次出现的位置
	 */
	public int lastIndexOf(Object o) {
		int index = size;
		if (o == null) {
			for (Node<E> x = last; x != null; x = x.prev) {
				index--;
				if (x.item == null)
					return index;
			}
		} else {
			for (Node<E> x = last; x != null; x = x.prev) {
				index--;
				if (o.equals(x.item))
					return index;
			}
		}
		return -1;
	}

	/*************************** Queue 接口相关操作***************************/

	/**
	 * 获取第一个元素，但是不删除，如果首节点为null,不会抛出异常，返回null
	 */
	public E peek() {
		final Node<E> f = first;
		return (f == null) ? null : f.item;
	}

	/**
	 * 获取第一个元素，但是不删除，如果列表为空，则抛出NoSuchElementException 
	 */
	public E element() {
		return getFirst();
	}

	/**
	 * 获取并移除此队列的头，如果此队列为空，则返回 null。
	 */
	public E poll() {
		final Node<E> f = first;
		return (f == null) ? null : unlinkFirst(f);
	}

	/**
	 * 获取并移除此队列的头，如果列表为空，则抛出NoSuchElementException 
	 */
	public E remove() {
		return removeFirst();
	}

	/**
	 * 增加一个元素到列表尾部，因为linkedlist容量没有限制，若以此方法总是返回true
	 */
	public boolean offer(E e) {
		return add(e);
	}

	/*************************** Deque 接口相关操作***************************/
	/**
	 * 将指定的元素插入此双端队列的开头
	 */
	public boolean offerFirst(E e) {
		addFirst(e);
		return true;
	}

	/**
	 * 将指定的元素插入此双端队列的末尾
	 */
	public boolean offerLast(E e) {
		addLast(e);
		return true;
	}

	/**
	 *  获取第一个元素，但是不删除，如果首节点为null,不会抛出异常，返回null
	 */
	public E peekFirst() {
		final Node<E> f = first;
		return (f == null) ? null : f.item;
	}

	/**
	 *  获取最后一个元素，但是不删除，如果首节点为null,不会抛出异常，返回null
	 */
	public E peekLast() {
		final Node<E> l = last;
		return (l == null) ? null : l.item;
	}

	/**
	 * 获取并移除此队列的头，如果此队列为空，则返回 null。
	 */
	public E pollFirst() {
		final Node<E> f = first;
		return (f == null) ? null : unlinkFirst(f);
	}

	/**
	 * 获取并移除此队列的尾节点，如果此队列为空，则返回 null。
	 */
	public E pollLast() {
		final Node<E> l = last;
		return (l == null) ? null : unlinkLast(l);
	}

	/**
	 * 将一个元素推入此双端队列所表示的堆栈（换句话说，此双端队列的头部）
	 */
	public void push(E e) {
		addFirst(e);
	}

	/**
	 *从此双端队列所表示的堆栈中弹出一个元素，换句话说返回头节点的元素并删除，
	 *如果链表为空，则抛出NoSuchElementException异常
	 */
	public E pop() {
		return removeFirst();
	}

	/**
	 * 从此双端队列移除第一次出现的指定元素。
	 */
	public boolean removeFirstOccurrence(Object o) {
		return remove(o);
	}

	/**
	 * 从此双端队列移除最后一次出现的指定元素
	 */
	public boolean removeLastOccurrence(Object o) {
		if (o == null) {
			for (Node<E> x = last; x != null; x = x.prev) {
				if (x.item == null) {
					unlink(x);
					return true;
				}
			}
		} else {
			for (Node<E> x = last; x != null; x = x.prev) {
				if (o.equals(x.item)) {
					unlink(x);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns a list-iterator of the elements in this list (in proper
	 * sequence), starting at the specified position in the list.
	 * Obeys the general contract of {@code List.listIterator(int)}.<p>
	 *
	 * The list-iterator is <i>fail-fast</i>: if the list is structurally
	 * modified at any time after the Iterator is created, in any way except
	 * through the list-iterator's own {@code remove} or {@code add}
	 * methods, the list-iterator will throw a
	 * {@code ConcurrentModificationException}.  Thus, in the face of
	 * concurrent modification, the iterator fails quickly and cleanly, rather
	 * than risking arbitrary, non-deterministic behavior at an undetermined
	 * time in the future.
	 *
	 * @param index index of the first element to be returned from the
	 *              list-iterator (by a call to {@code next})
	 * @return a ListIterator of the elements in this list (in proper
	 *         sequence), starting at the specified position in the list
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 * @see List#listIterator(int)
	 */
	public ListIterator<E> listIterator(int index) {
		checkPositionIndex(index);
		return new ListItr(index);
	}

	private class ListItr implements ListIterator<E> {
		private Node<E> lastReturned = null;
		private Node<E> next;
		private int nextIndex;
		private int expectedModCount = modCount;

		ListItr(int index) {
			// assert isPositionIndex(index);
			next = (index == size) ? null : node(index);
			nextIndex = index;
		}

		public boolean hasNext() {
			return nextIndex < size;
		}

		public E next() {
			checkForComodification();
			if (!hasNext())
				throw new NoSuchElementException();

			lastReturned = next;
			next = next.next;
			nextIndex++;
			return lastReturned.item;
		}

		public boolean hasPrevious() {
			return nextIndex > 0;
		}

		public E previous() {
			checkForComodification();
			if (!hasPrevious())
				throw new NoSuchElementException();

			lastReturned = next = (next == null) ? last : next.prev;
			nextIndex--;
			return lastReturned.item;
		}

		public int nextIndex() {
			return nextIndex;
		}

		public int previousIndex() {
			return nextIndex - 1;
		}

		public void remove() {
			checkForComodification();
			if (lastReturned == null)
				throw new IllegalStateException();

			Node<E> lastNext = lastReturned.next;
			unlink(lastReturned);
			if (next == lastReturned)
				next = lastNext;
			else
				nextIndex--;
			lastReturned = null;
			expectedModCount++;
		}

		public void set(E e) {
			if (lastReturned == null)
				throw new IllegalStateException();
			checkForComodification();
			lastReturned.item = e;
		}

		public void add(E e) {
			checkForComodification();
			lastReturned = null;
			if (next == null)
				linkLast(e);
			else
				linkBefore(e, next);
			nextIndex++;
			expectedModCount++;
		}

		final void checkForComodification() {
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
		}
	}

	/**
	 * 
	 * @author lipeng
	 * @date 2016年6月5日
	 */
	private static class Node<E> {
		// 节点存储的元素
		E item;
		// 下一个节点
		Node<E> next;
		// 上一个节点
		Node<E> prev;

		Node(Node<E> prev, E element, Node<E> next) {
			this.item = element;
			this.next = next;
			this.prev = prev;
		}
	}

	/**
	 * @since 1.6
	 */
	public Iterator<E> descendingIterator() {
		return new DescendingIterator();
	}

	/**
	 * Adapter to provide descending iterators via ListItr.previous
	 */
	private class DescendingIterator implements Iterator<E> {
		private final ListItr itr = new ListItr(size());

		public boolean hasNext() {
			return itr.hasPrevious();
		}

		public E next() {
			return itr.previous();
		}

		public void remove() {
			itr.remove();
		}
	}

	@SuppressWarnings("unchecked")
	private LinkedList7<E> superClone() {
		try {
			return (LinkedList7<E>) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}

	/**
	 * Returns a shallow copy of this {@code LinkedList}. (The elements
	 * themselves are not cloned.)
	 *
	 * @return a shallow copy of this {@code LinkedList} instance
	 */
	public Object clone() {
		LinkedList7<E> clone = superClone();

		// Put clone into "virgin" state
		clone.first = clone.last = null;
		clone.size = 0;
		clone.modCount = 0;

		// Initialize clone with our elements
		for (Node<E> x = first; x != null; x = x.next)
			clone.add(x.item);

		return clone;
	}

	/**
	 * Returns an array containing all of the elements in this list
	 * in proper sequence (from first to last element).
	 *
	 * <p>The returned array will be "safe" in that no references to it are
	 * maintained by this list.  (In other words, this method must allocate
	 * a new array).  The caller is thus free to modify the returned array.
	 *
	 * <p>This method acts as bridge between array-based and collection-based
	 * APIs.
	 *
	 * @return an array containing all of the elements in this list
	 *         in proper sequence
	 */
	public Object[] toArray() {
		Object[] result = new Object[size];
		int i = 0;
		for (Node<E> x = first; x != null; x = x.next)
			result[i++] = x.item;
		return result;
	}

	/**
	 * Returns an array containing all of the elements in this list in
	 * proper sequence (from first to last element); the runtime type of
	 * the returned array is that of the specified array.  If the list fits
	 * in the specified array, it is returned therein.  Otherwise, a new
	 * array is allocated with the runtime type of the specified array and
	 * the size of this list.
	 *
	 * <p>If the list fits in the specified array with room to spare (i.e.,
	 * the array has more elements than the list), the element in the array
	 * immediately following the end of the list is set to {@code null}.
	 * (This is useful in determining the length of the list <i>only</i> if
	 * the caller knows that the list does not contain any null elements.)
	 *
	 * <p>Like the {@link #toArray()} method, this method acts as bridge between
	 * array-based and collection-based APIs.  Further, this method allows
	 * precise control over the runtime type of the output array, and may,
	 * under certain circumstances, be used to save allocation costs.
	 *
	 * <p>Suppose {@code x} is a list known to contain only strings.
	 * The following code can be used to dump the list into a newly
	 * allocated array of {@code String}:
	 *
	 * <pre>
	 *     String[] y = x.toArray(new String[0]);</pre>
	 *
	 * Note that {@code toArray(new Object[0])} is identical in function to
	 * {@code toArray()}.
	 *
	 * @param a the array into which the elements of the list are to
	 *          be stored, if it is big enough; otherwise, a new array of the
	 *          same runtime type is allocated for this purpose.
	 * @return an array containing the elements of the list
	 * @throws ArrayStoreException if the runtime type of the specified array
	 *         is not a supertype of the runtime type of every element in
	 *         this list
	 * @throws NullPointerException if the specified array is null
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		if (a.length < size)
			a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
		int i = 0;
		Object[] result = a;
		for (Node<E> x = first; x != null; x = x.next)
			result[i++] = x.item;

		if (a.length > size)
			a[size] = null;

		return a;
	}

	private static final long serialVersionUID = 876323262645176354L;

	/**
	 * Saves the state of this {@code LinkedList} instance to a stream
	 * (that is, serializes it).
	 *
	 * @serialData The size of the list (the number of elements it
	 *             contains) is emitted (int), followed by all of its
	 *             elements (each an Object) in the proper order.
	 */
	private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
		// Write out any hidden serialization magic
		s.defaultWriteObject();

		// Write out size
		s.writeInt(size);

		// Write out all elements in the proper order.
		for (Node<E> x = first; x != null; x = x.next)
			s.writeObject(x.item);
	}

	/**
	 * Reconstitutes this {@code LinkedList} instance from a stream
	 * (that is, deserializes it).
	 */
	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
		// Read in any hidden serialization magic
		s.defaultReadObject();

		// Read in size
		int size = s.readInt();

		// Read in all elements in the proper order.
		for (int i = 0; i < size; i++)
			linkLast((E) s.readObject());
	}
}
