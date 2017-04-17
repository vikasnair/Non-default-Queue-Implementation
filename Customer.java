// a class which describes every customer

public class Customer {
	private int id;
	private int arrival_time;
	private int wait_time;
	private int serve_time;
	private Customer next;

	// constructor, with default values for wait and serve time

	public Customer(int id, int arrival_time) {
		this.id = id;
		this.arrival_time = arrival_time;
		this.wait_time = 0;
		this.serve_time = 0;
		this.next = next;
	}

	public Customer(Customer new_customer) {
		id = new_customer.get_id();
		arrival_time = new_customer.get_arrival_time();
		wait_time = new_customer.get_wait_time();
		serve_time = new_customer.get_serve_time();
	}

	// getters and setters to work around package private access

	public int get_id() {
		return id;
	}

	public void set_id(int id) {
		this.id = id;
	}

	public int get_arrival_time() {
		return arrival_time;
	}

	public void set_arrival_time(int arrival_time) {
		this.arrival_time = arrival_time;
	}

	public int get_wait_time() {
		return wait_time;
	}

	public void set_wait_time(int wait_time) {
		this.wait_time = wait_time;
	}

	public int get_serve_time() {
		return serve_time;
	}

	public void set_serve_time(int serve_time) {
		this.serve_time = serve_time;
	}

	public Customer get_next() {
		return next;
	}

	public void set_next(Customer next) {
		this.next = next;
	}
}