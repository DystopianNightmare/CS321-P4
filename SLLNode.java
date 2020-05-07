/**
 * Simple node class for single linked list 
 * @author CS221 
 *
 * @param <T> generic type of elements stored in a node
 */
public class SLLNode<T>
{
	private SLLNode<T> next;		// reference to next node
	private T key;			// reference to object stored in node 
	private T value;


	/**
	 * Constructor - with given element 
	 * @param element - object of type T
	 */
	public SLLNode(T key, T value)
	{
		setKey(key);
		setValue(value);
		setNext(null);
	}

	/**
	 * Returns reference to next node
	 * @return - ref to SLLNode<T> object 
	 */
	public SLLNode<T> getNext()
	{
		return next;
	}

	/**
	 * Assign reference to next node 
	 * @param next - ref to Node<T> object 
	 */
	public void setNext(SLLNode<T> next)
	{
		this.next = next;
	}

	/**
	 * Returns reference to node stored in node 
	 * @return - ref to object of type T 
	 */
	public T getKey()
	{
		return key;
	}

	/**
	 * Sets reference to element stored at node
	 * @param element - ref to object of type T
	 */
	public void setKey(T key)
	{
		this.key = key;
	}
	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}


}
