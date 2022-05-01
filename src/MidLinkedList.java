/**
 * The only import used in this design is java.util.List;
 * The three remaining imports java.util.Collection,
 * java.util.Iterator, and java.ListIterator are only imported
 * to satisfy the remaining List interface methods signature requirements
 * despite that the those methods are not implemented.
 */
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * MidLinkedList is a class that builds off of a DoublyLinkedList which includes
 * an additional node that references the middle of the list for improved runtime 
 * for the add methods, the remove methods, and the get methods when referencing elements that
 * are closer to the middle of the list. This class uses a generic of type E to
 * allow the type to be declared at runtime instead of declaration when compiling.
 * @author Ilan Zar
 */
public class MidLinkedList<E> implements List<E>{
	
	/**
	 * Nested class that holds the design of the individual nodes being
	 * added to the list. This class uses a generic of type E to
	 * allow the type to be declared at runtime instead of declaration when compiling.
	 * Each instance holds an the element of type E, a reference pointing towards the next Node
	 * and a reference pointing towards the previous Node.
	 * The nested class structure is based on the DoublyLinkedList code fragments found in 
	 * the "EECS 2011: Arrays, LinkedLists" pdf.
	 */
	private static class Node<E>{
		private E element;
		private Node<E> next;
		private Node<E> prev;
		/**
		 * A three parameter constructor that creates a new node with the given values.
		 * @param element holds the value for this.element
		 * @param next holds the value for this.next
		 * @param prev holds the value for this.prev
		 */
		Node(E element, Node<E> next, Node<E> prev){
			this.element = element;
			this.next = next;
			this.prev = prev;
		}
	}
	
	/**
	 * Four attributes exist in the MidLinkedList class
	 * Three separate instances of the Node class holding the head, tail, and middle
	 * respectively all of which following the generic type.
	 * One integer holding the current size of the List.
	 * All attributes are set to default values for simplicity.
	 */
	private Node<E> head = null;
	private Node<E> tail = null;
	private Node<E> middle = null;
	private int size = 0;
	
	/**
	 * A no-argument constructor that initializes an empty MidLinkedList.
	 * Since the attributes are defined with default values no additional 
	 * assignment is required.
	 */
	MidLinkedList(){}
	
	/**
	 * A helper method that updates the position of the middle node upon successful
	 * adding of a new Node.
	 * @param index Holds the index value of type integer of the Node just added.
	 */
	private void middleUpdateAdd(int index) {
		int oldRem = (this.size - 1) % 2;
		int oldMid = (this.size - 1)/2 - 1 + oldRem;
		int rem = this.size % 2;
		
		if(index > oldMid && rem == 1) 
			this.middle = this.middle.next;
		else if(index <= oldMid && rem == 0) 
			this.middle = this.middle.prev;
	}
	
	/**
	 * A helper method that updates the position of the middle node prior to 
	 * the successful removal of an existing Node.
	 * @param index Holds the index value of type integer of the Node being removed.
	 */
	private void middleUpdateRem(int index) {
		int rem = this.size % 2;
		int mid = (this.size)/2 - 1 + rem;
		
		if(index > mid && rem == 1) 
			this.middle = this.middle.prev;
		else if(index < mid && rem == 0) 
			this.middle = this.middle.next;
		else if(index == mid) {
			if(rem == 0)
				this.middle = this.middle.next;
			else
				this.middle = this.middle.prev;
		}
	}

	/**
	 * This method is used to convert the existing MidLinkedList into the form of a String
	 * @return String This returns the String representing the MidLinkedList
	 */
	@Override
	public String toString() {
		String fin;
		if(this.size == 0)
			fin = "[]";
		else {
			fin = "[";
			Node<E> temp = this.head;
			fin += temp.element;
			if(size > 1) {
				for (int i = 1; i < this.size - 1; i++){
					fin += ", " + temp.next.element;
					temp = temp.next;
				}
				fin += ", " + temp.next.element;
			}
			fin += "]";
		}
		return fin;
	}
	
	/**
	 * This method is used to return the current size of the MidLinkedList.
	 * @return int This returns the integer value of the size.
	 */
	@Override
	public int size() {
		return this.size;
	}

	@Override
	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray(Object[] a) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This method is used to add new elements to the end of the MidLinkedList
	 * @param e Holds the generic value of the element the user wants to add.
	 * @return boolean This returns the boolean value based on if the add was successful or not.
	 * @exception NullPointerException Thrown if value of e is equal to null.
	 * @see NullPointerException
	 * Basic structure is based on the DoublyLinkedList code fragments found in 
	 * the "EECS 2011: Arrays, LinkedLists" pdf.
	 */
	@Override
	public boolean add(E e) {
		if(e == null)
			throw new NullPointerException();
		if (this.size == 0) {
			Node<E> temp = new Node<E> (e, null, null);
			this.head = temp;
			this.tail = temp;
			this.middle = temp;
			this.size++;
		}
		else {
			Node<E> temp = new Node<E> (e, null, this.tail);
			this.tail.next = temp;
			this.tail = temp;
			this.size++;
			middleUpdateAdd(this.size);
		}
		return true;
	}
	
	/**
	 * A helper method used to add new elements to the beginning of the MidLinkedList
	 * @param e Holds the generic value of the element the user wants to add.
	 * @return boolean This returns the boolean value based on if the add was successful or not.
	 * Since this helper is private and cannot be explicitly called the exceptions associated
	 * with this method are handled in the add(int index, E element) method.
	 * Basic structure is based on the DoublyLinkedList code fragments found in 
	 * the "EECS 2011: Arrays, LinkedLists" pdf.
	 */
	private boolean addFirst(E e) {
		if (this.size == 0) {
			Node<E> temp = new Node<E> (e, null, null);
			this.head = temp;
			this.tail = temp;
			this.middle = temp;
			this.size++;
		}
		else {
			Node<E> temp = new Node<E> (e, this.head, null);
			this.head.prev = temp;
			this.head = temp;
			this.size++;
			middleUpdateAdd(0);
		}
		return true;
	}
	
	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index, Collection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This method deletes all elements in the MiddleLinkedList by setting the values
	 * of the class attributes back to default. Java handles the removal of the now 
	 * inaccessible elements so there is no need to do it manually.
	 */
	@Override
	public void clear() {
		this.head = null;
		this.tail = null;
		this.middle = null;
		this.size = 0;
		
	}

	/**
	 * This method returns the element of Node stored at the specified index.
	 * @param index Holds the integer value of the index of the Node that is being accessed
	 * within the MidLinkedList.
	 * @return E This returns the element stored at said Node of type E.
	 * @exception IndexOutOfBoundsException Thrown if the parameter is less than 0 or
	 * greater than or equal to the size of the MidLinkedList. 
	 * @see IndexOutOfBoundsException
	 */
	@Override
	public E get(int index) {
		if(index < 0 || index >= size())
			throw new IndexOutOfBoundsException();
		Node<E> temp = getNode(index);
		return temp.element;
	}
	
	/**
	 * A helper method that specifically returns the Node at specified index.
	 * @param index Holds the integer value of the  index of the Node that is being accessed
	 * within the MidLinkedList.
	 * @return Node<E> This returns the Node stored at said index of type E.
	 */
	private Node<E> getNode(int index){
		int rem = this.size % 2;
		int mid = (this.size/2 - 1) + rem;
		Node<E> temp;
		if(index > mid) {
			if(this.size - 1 - index > index - (mid + 1)) {
				temp = this.middle.next;
				for(int i = 0; i < (index - (mid + 1)); i++)
					temp = temp.next;
			}
			else {
				temp = this.tail;
				for(int i = 0; i < (this.size - 1 - index); i++) 
					temp = temp.prev;
			}
		}
		else {
			if(index > mid - index) {
				temp = this.middle;
				for(int i = 0; i < mid - index; i++) 
					temp = temp.prev;
			}
			else {
				temp = this.head;
				for(int i = 0; i < index; i++)
					temp = temp.next;
			}
		}
		return temp;
	}
	

	@Override
	public Object set(int index, Object element) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This method adds a new node at the specified index with the specified value.
	 * Upon successful addition, the remaining nodes that had an index greater than or equal 
	 * to the one specified are shifted to the right.
	 * @param index Holds the integer value of the index of the Node that is being added
	 * to the MidLinkedList.
	 * @param element Holds the generic value for the Node that is being added to the MidLinkedList.
	 * @exception NullPointerException Thrown if the element value is equal to null.
	 * @see NullPointerException
	 * @exception IndexOutOfBoundsException Thrown if the index is less than 0 or greater than 
	 * the size of the MidLinkedList.
	 * @see IndexOutOfBoundsException
	 */
	@Override
	public void add(int index, E element) {
		if(element == null) 
			throw new NullPointerException();
		if(index < 0 || index > size())
			throw new IndexOutOfBoundsException();
		if (this.size == 0 || index == this.size) 
			add(element);
		else if (index == 0) 
			addFirst(element);
		else {
			Node<E> tempNext = getNode(index);
			Node<E> tempPrev = tempNext.prev;
			Node<E> temp = new Node<E>(element, tempNext, tempPrev);
			tempPrev.next = temp;
			tempNext.prev = temp;
			this.size++;
			middleUpdateAdd(index);
		}
		
	}

	/**
	 * This method removes the Node at the specified index from the MidLinkedList.
	 * @param index Holds the integer value of index of the Node that is being removed
	 * from the MidLinkedList.
	 * @return E Returns the generic type element of the Node being remove.
	 * @exception IndexOutOfBoundsException Thrown if the value of the index is less than 0 or
	 * greater than or equal to the size of the MidLinkedList.
	 * @see IndexOutOfBoundsException
	 */
	@Override
	public E remove(int index) {
		if(index < 0 || index >= size())
			throw new IndexOutOfBoundsException();
		Node<E> temp = getNode(index);
		if(this.size == 1)
			clear();
		else {
			middleUpdateRem(index);
			if(index == 0) {
				temp = this.head;
				this.head = this.head.next;
				this.head.prev = null;
				this.size--;
			}
			else if(index == this.size - 1) {
				temp = this.tail;
				this.tail = this.tail.prev;
				this.tail.next = null;
				this.size--;
			}
			else {
				Node<E> tempPrev = temp.prev;
				Node<E> tempNext = temp.next;
				tempPrev.next = tempNext;
				tempNext.prev = tempPrev;
				this.size--;
			}
		}
		return temp.element;
	}

	@Override
	public int indexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int lastIndexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator listIterator(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}

}
