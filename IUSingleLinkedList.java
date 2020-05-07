import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
/**
 * 
 * @author joshgandolfo
 * @instructor MhThomas cs221-3
 * @param <T> generic type of items in the linked list
 */
public class IUSingleLinkedList<T>  {

	private SLLNode<T> head, tail; // Nodes used to represent the head and tail of the list
	private SLLNode<T> tmp, current; // nodes used as temporary variables 
	private int count;	//used to track number of nodes
	private int modCount; //used to track modifications to the list


	/**
	 * default constructor
	 */
	public IUSingleLinkedList() {
		count = 0;
		head = new SLLNode<T>(null, null);
		modCount = 0;
		tail = head;

	}
	//	@Override
	public void addToFront(T key, T value) {
		if(count == 0 ) {
			SLLNode<T> newNode = new SLLNode<T>(key, value);

			tail = newNode;
			head = newNode;
			head.setNext(null);
			modCount++;
			count++;

		}else {
			SLLNode<T> newNode = new SLLNode<T>(key, value);
			tmp = head;
			newNode.setNext(tmp);
			head = newNode;
			modCount++;
			count++;

		}
	}

	//	@Override
	//	public void addToRear(T element) {
	//		if(count == 0) {
	//			SLLNode<T> newNode = new SLLNode<T>(element);
	//			head = newNode;
	//			tail = newNode;
	//			count++;
	//			modCount++;
	//		}else {
	//			SLLNode<T> newNode = new SLLNode<T>(element);
	//			tmp = tail;
	//			tail = newNode;
	//			tmp.setNext(tail);
	//			count++;
	//			modCount++;
	//		}
	//	}
	//	@Override
	//	public void add(T element) {
	//		SLLNode<T> newNode = new SLLNode<T>(element);
	//		if(count == 0) {
	//			head = newNode;
	//		}else {
	//			tail.setNext(newNode);
	//		}
	//		tail = newNode;
	//		count++;
	//		modCount++;
	//
	//	}
	//
	//	@Override
	//	public void addAfter(T element, T target) {
	//		int index = indexOf(target);
	//		if(index == -1) {
	//			throw new NoSuchElementException("no such element");
	//		}
	//		SLLNode<T> newNode = new SLLNode<T>(element);
	//		current = head;
	//		if(index == 0) {
	//			newNode.setNext(current.getNext());
	//			current.setNext(newNode);
	//
	//			if(count == 1) {
	//				tail = newNode;
	//			}
	//			modCount++;
	//			count++;
	//		}else if(index == count-1) {
	//			tmp = tail;
	//			tail.setNext(newNode);
	//			tail = newNode;
	//			modCount++;
	//			count++;
	//		}else{
	//			for(int i = 0; i < index-1; i++) {
	//				current = current.getNext();
	//			}
	//			tmp = current.getNext();
	//			current.setNext(newNode);
	//			newNode.setNext(tmp);
	//			modCount++;
	//			count++;
	//		}
	//
	//	}
	//
	//	@Override
	//	public void add(int index, T element) {
	//		SLLNode<T> newNode = new SLLNode<T>(element);
	//		current = head;
	//		if (index < 0 || index > count) {
	//			throw new IndexOutOfBoundsException(" add(int,T) method index out of range");
	//		}
	//
	//
	//		if(index != count && index != 0){
	//			for(int i = 0; i < index-1; i++) {
	//				current = current.getNext();
	//			}
	//				newNode.setNext(current.getNext());
	//				current.setNext(newNode);
	//		}
	//			
	//		if(index == 0) {
	//
	//			addToFront(element);
	//			count--;
	//			modCount++;
	//			if(index == count){
	//				
	//				tail = newNode;
	//
	//			}}
	//	
	//		if(index == count) {
	//			tail.setNext(newNode);
	//			tail = newNode;
	//		}
	//		
	//		count++;
	//		modCount++;
	//		}
	//	
	//
	//	@Override
	public T removeFirst() {
		T value = null;
		if(count == 0 ) {
			throw new NoSuchElementException("no such element");
		}else {
			current = head;
			value = head.getValue();
			head = current.getNext();
			if(head == null) {
				tail = null;
			}
			current.setNext(null);
			modCount++;
			count--;
		}
		return value;
	}

	//	@Override
	public T removeLast() {
		T value = null;
		if(count == 0 ) {
			throw new NoSuchElementException("no such element");
		}else if(count == 1){
			value = removeFirst();
		}
		else if(count == 2) {

			current = head;
			value = current.getNext().getValue();
			current.setNext(null);
			tail = head;
			count--;
			modCount++;
		}

		else {
			current = head;
			for(int i = 0; i < count-1; i++) {

				current = current.getNext();
			}

			value = tail.getValue();
			current.setNext(null);
			tail = current;
			modCount++;
			count--;
		}

		return value;
	}

	//	@Override
	public T remove(T element) {
		int i = indexOf(element);
		if(i == -1) {
			throw new NoSuchElementException("no such element");
		}

		return remove(i);
	}

	//	@Override
	public T remove(int index) {

		current = head;
		T value = null;

		if (index < 0 || index >= count) {
			throw new IndexOutOfBoundsException(" add(int,T) method index out of range");
		}
		if(index == 0) {
			value =	removeFirst();
		}
		else if(index == count-1) {
			value = removeLast();

		}else {

			for(int i = 0; i < index-1; i++) {
				current = current.getNext();
			}
			tmp = current.getNext();
			value = tmp.getValue();
			current.setNext(tmp.getNext());
			tmp.setNext(null);
			modCount++;
			count--;
		}
		return value;
	}

	//	@Override
	//	public void set(int index, T element) {
	//		if (index < 0 || index >= count) {
	//			throw new IndexOutOfBoundsException(" add(int,T) method index out of range");
	//		}
	//		SLLNode<T> node = head;
	//		for(int i = 0; i < index; i++) {
	//			node = node.getNext();
	//		}
	//		modCount++;
	//		node.setElement(element);
	//
	//	}
	//
	//	@Override
	//	public T get(int index) {
	//		if (index < 0 || index >= count) {
	//			throw new IndexOutOfBoundsException(" add(int,T) method index out of range");
	//		}
	//		current = head;
	//		for(int i = 0; i < index; i++) {
	//			current = current.getNext();
	//		}
	//
	//		return current.getElement();
	//	}
	//
	//	@Override
	public int indexOf(T element) {
		boolean found = false;
		current = head;
		int index = 0;
		int pos = -1;
		while(!found && index < count) {

			if(current.getKey().equals(element)) {
				found = true;
				pos = index;
			}
			current = current.getNext();
			index++;
		}
		return pos;
	}

	//	@Override
	//	public T first() {
	//		if(count == 0) {
	//			throw new NoSuchElementException("no such element");
	//		}
	//		T element = head.getElement();
	//		return element;
	//	}
	//
	//	@Override
	//	public T last() {
	//		if(count == 0) {
	//			throw new NoSuchElementException("no such element");
	//		}
	//		T element = tail.getElement();
	//		return element;
	//	}
	//
	//	@Override
	public boolean contains(T target) {
		boolean found = false;
		current = head;
		int index = 0;

		while(!found && index < count) {

			if(current.getKey().equals(target)) {
				return true;

			}
			current = current.getNext();
			index++;
		}

		return false;
	}

	//	@Override
	public boolean isEmpty() {

		return (count == 0);
	}

	//	@Override
	public int size() {

		return count;
	}

	//	@Override
	public Iterator<T> iterator() {

		return new NodeIterator();
	}

	//	@Override
	public ListIterator<T> listIterator() {

		throw new UnsupportedOperationException("unsupported operation");

	}

	//	@Override
	public ListIterator<T> listIterator(int startingIndex) {

		throw new UnsupportedOperationException("unsupported operation");

	}

	private class NodeIterator implements Iterator<T> {

		private boolean canRemove;	//check if we can remove from the list
		private int  itrModCount;		// tracks the count of modifications to the list
		private int  itr;	// tracks the number of times the next method is called
		private SLLNode<T> next, current, previous;	// node used for traversing the list

		public NodeIterator() {
			next = head;
			current = new SLLNode<T>(null,null);
			current.setNext(next);
			canRemove = false;
			itrModCount = modCount;
			itr = 0;
		}
		public void checkMod() {
			if (modCount != itrModCount) {
				throw new ConcurrentModificationException("Modification during iteration");
			}
		}

		@Override
		public boolean hasNext() {
			checkMod();
			if(itr == count) {
				return false;
			}
			if(previous == tail ) {
				return false;
			}

			return (current != tail);
		}

		@Override
		public T next() {
			checkMod();
			if(!hasNext())   {
				throw new NoSuchElementException("no such element");
			}
			if(previous == tail) {
				current = next;
				canRemove = false;

			}else if(itr == count){
				canRemove = true;
				previous = current;
				current=next;

			}else {
				canRemove = true;
				previous = current;
				current = next;
				next = next.getNext();
			}
			itr++;
			return current.getKey();
		}
		/**
		 * optional remove method - removes the node at the particular point
		 */
		public void remove() {
			checkMod();
			if(canRemove == false) {
				throw new IllegalStateException("illegal state");
			}

			if(current == head) {
				current.setNext(null);
				previous.setNext(null);
				head = next;
			}else if(current == tail) {
				previous.setNext(null);
				current.setNext(null);
				tail = previous;

			}else {
				previous.setNext(next);
				current.setNext(null);
			}

			canRemove = false;

		}

	}
}
