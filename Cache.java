public class Cache<T> {

	
	private IUSingleLinkedList<T> cache;
	private int cap;
	public int size;
	/**
	 * constructor to create a cache using a linked list
	 * @param capacity - size of the linked list
	 */
	public Cache(int capacity) {

		cache = new IUSingleLinkedList<T>();
		cap = capacity;
		size = 0;
	}

	/**
	 * adds the object to the cache
	 * @param element - object to be added
	 */
	public void addObject(T key, T value) {
		cache.addToFront(key,value);

		size++;

	}
	/*
	 * removes the object from the cache
	 */
	public T removeObject(T element) {
		T val = cache.remove(element);
		size--;
		return val;
	}
	/**
	 * clears the cache by making a new list
	 */
	public void clearCache() {
		cache = new IUSingleLinkedList<T>();
		size = 0;

	}
	/**
	 * checks if cache is at capacity
	 * @return true if it is at capacity
	 */
	public boolean atCap() {
		return size >= cap;
	}

	/**
	 * searches cache for element
	 * @param element - element to be searched for
	 * @return true if element is found
	 */
	public T search( T element) {

		if(cache.contains(element)) {
			T key = removeObject(element);
			addObject(element,key);

			return key;
		}
		return null;
	}
	/**
	 * adds element to cache
	 * @param element - represents element to be added
	 */
	public void addToCache(T element, T key) {
		if(!atCap()) {
			addObject(element,key);


		}else {
			cache.removeLast();
			size--;
			addObject(element,key);

		}

	}

}
