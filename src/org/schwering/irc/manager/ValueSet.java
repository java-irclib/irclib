package org.schwering.irc.manager;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * TODO can be deleted (probably)
 */
class ValueSet {
	private Set entrySet;
	
	public ValueSet(Map map) {
		entrySet = map.entrySet();
	}

	public boolean add(Object arg0) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(Collection arg0) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public boolean contains(Object o) {
		if (o instanceof User) {
			return entrySet.contains(((User)o).getNick());
		} else {
			return false;
		}
	}

	public boolean containsAll(Collection c) {
		for (Iterator it = c.iterator(); it.hasNext(); ) {
			if (!contains(it.next())) {
				return false;
			}
		}
		return true;
	}

	public boolean isEmpty() {
		return entrySet.isEmpty();
	}

	public Iterator iterator() {
		return new Iterator() {
			private Iterator it = entrySet.iterator();

			public boolean hasNext() {
				return it.hasNext();
			}

			public Object next() {
				return ((Map.Entry)it.next()).getValue();
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection arg0) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection arg0) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		return entrySet.size();
	}

	public Object[] toArray() {
		Object[] arr = new Object[size()];
		int i = 0;
		for (Iterator it = iterator(); it.hasNext(); ) {
			arr[i++] = it.next();
		}
		return arr;
	}

	public Object[] toArray(Object[] arr) {
		if (arr.length < size()) {
			arr = (Object[])Array.newInstance(arr[0].getClass(), size());
		}
		int i = 0;
		for (Iterator it = iterator(); it.hasNext(); ) {
			arr[i++] = it.next();
		}
		return arr;
	}
}
