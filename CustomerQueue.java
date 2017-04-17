// a non-default queue class implemented for this assignment

import java.util.NoSuchElementException;

public class CustomerQueue {

	// first and last are null by default

	private Customer first, last;
	private int size;

	public CustomerQueue() {
		size = 0;
	}

	// allow access to first and size

	public Customer get_first() {
		return first;
	}

	public int get_size() {
		return size;
	}

	// methods to add and remove from queue in a first-in-first-out (FIFO) manner

	public CustomerQueue enqueue(Customer new_customer) {

		// store a temporary reference to the last element

		Customer temp = last;

		// set the last element in the queue equal to the new customer
		last = new Customer(new_customer);

		if (isEmpty()) {
			first = last;
		}

		// set the next (or, the "previous") of the old last to the new last

		else {
			temp.set_next(last);
		}

		// increment the size and return the queue

		size++;
		return this;
	}

	public Customer dequeue() {
		if (isEmpty()) {
			throw new NoSuchElementException("Queue underflow.");
		}

		// store a temporary reference to the first element

		Customer temp = new Customer(first);

		// next-in-line is set to the new first

		first = first.get_next();

		if (isEmpty()) {
			last = null;
		}

		// decrement the size and return the reference to the old first

		size--;
		return temp;
	}

	// simple method to check if the queue is empty

	public boolean isEmpty() {
		if (size == 0) {
			return true;
		}

		else {
			return false;
		}
	}
}