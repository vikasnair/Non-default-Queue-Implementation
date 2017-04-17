// main class which mimics a shop implementing the queue data structure with customer objects

import java.util.Scanner;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.IllegalArgumentException;

public class Shop {

	// accepts file input through args in main

	public static void main(String[] args) {
		if (args.length != 2) 
			throw new IllegalArgumentException("Please enter a customers and queries filename.");

		// initialize queue, lists of customers

		CustomerQueue queue = new CustomerQueue();
		List<Customer> all_customers = initialize_customers(args[0]);
		List<Customer> satisfied_customers = new ArrayList<>();

		// initialize accumulators

		int current_time = 30600;
		int total_idle_time = 0;
		int current_idle_time = 0;
		int idle_time_max = 0;
		int queue_max = 0;
		int customers_served = 0;

		// enter a loop from the start to end of simulated 9-5

		while (current_time < 61200) {

			// conditionals to account for idle times

			if (queue.isEmpty() && current_time > 32400)
				current_idle_time++;

			else {
				total_idle_time += current_idle_time;

				if (current_idle_time > idle_time_max)
					idle_time_max = current_idle_time;

				current_idle_time = 0;
			}

			// iterate through the list of customers and enqueue when they arrive to shop

			for (Customer customer : all_customers) {
				if (customer.get_arrival_time() == current_time) {
					queue.enqueue(customer);
					customers_served++;
				}
			}

			// dequeue customers after they have been served and add to the second array list

			if (queue.get_first() != null && queue.get_first().get_serve_time() == 300)
					satisfied_customers.add(queue.dequeue());

			// conditional to handle customers who arrive too late

			if (queue.get_first() != null && (current_time + queue.get_first().get_serve_time() > 61200)) {

				// serve the first-in-line

				queue.get_first().set_serve_time(300);
				satisfied_customers.add(queue.dequeue());

				// kick the rest out of the line

				while (queue.get_first() != null) {
					queue.get_first().set_wait_time(61200 - queue.get_first().get_arrival_time());
					satisfied_customers.add(queue.dequeue());
					customers_served--;
				}
			}

			// iterate through the queue

			Customer current = queue.get_first();

			while (current != null) {

				// update the serve time of the first-in-line, and the wait time of the rest

				if (current_time >= 32400 && current.equals(queue.get_first()))
					current.set_serve_time(current.get_serve_time() + 1);
				else
					current.set_wait_time(current.get_wait_time() + 1);

				current = current.get_next();
			}

			// conditional to account for max queue size at any given time

			if (queue.get_size() > queue_max)
				queue_max = queue.get_size();

			current_time++;
		}

		// send all data to a method to print results

		print_results(args[1], satisfied_customers, customers_served, total_idle_time, idle_time_max, queue_max);
	}

	// method which prints results to bash

	public static void print_results(String file, List<Customer> satisfied_customers, int customers_served, int total_idle_time, int idle_time_max, int queue_max) {
		System.out.println("\nTotal idle time: " + time_to_string(total_idle_time));
		System.out.println("Longest break time: " + time_to_string(idle_time_max));
		System.out.println("Largest queue size: " + queue_max);

		for (Customer customer : satisfied_customers)
			System.out.println("\nID: " + customer.get_id() + "\nArrival time: " + time_to_string(customer.get_arrival_time()) + "\nWait time: " + time_to_string(customer.get_wait_time()) + "\nServe time: " + time_to_string(customer.get_serve_time()));
		System.out.println();

		try {
			Scanner reader = new Scanner(new File(file));

			while (reader.hasNextLine()) {
				String current = reader.nextLine();

				if (current.contains("NUMBER-OF-CUSTOMERS-SERVED"))
					System.out.println("NUMBER-OF-CUSTOMERS-SERVED: " + customers_served);
				if (current.contains("LONGEST-BREAK-LENGTH"))
					System.out.println("LONGEST-BREAK-LENGTH: " + time_to_string(idle_time_max));
				if (current.contains("TOTAL-IDLE-TIME"))
					System.out.println("TOTAL-IDLE-TIME: " + time_to_string(total_idle_time));
				if (current.contains("MAXIMUM-NUMBER-OF-PEOPLE-IN-QUEUE-AT-ANY-TIME"))
					System.out.println("MAXIMUM-NUMBER-OF-PEOPLE-IN-QUEUE-AT-ANY-TIME:" + queue_max);
				if (current.contains("WAITING-TIME-OF 1"))
					System.out.println("WAITING-TIME-OF 1: " + time_to_string(satisfied_customers.get(0).get_wait_time()));
				if (current.contains("WAITING-TIME-OF 2"))
					System.out.println("WAITING-TIME-OF 2: " + time_to_string(satisfied_customers.get(1).get_wait_time()));
				if (current.contains("WAITING-TIME-OF 3"))
					System.out.println("WAITING-TIME-OF 3: " + time_to_string(satisfied_customers.get(2).get_wait_time()));
				if (current.contains("WAITING-TIME-OF 7"))
					System.out.println("WAITING-TIME-OF 7: " + time_to_string(satisfied_customers.get(6).get_wait_time()));
			}

			System.out.println();

			reader.close();
		} 

		catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	// method to convert seconds into readable time

	public static String time_to_string(int time) {
		String time_string = "";

		if (time / 3600 < 10)
			time_string += "0";
		time_string += String.valueOf((time / 3600)) + ":";

		if ((time % 3600 / 60) < 10)
			time_string += "0";
		time_string += String.valueOf((time % 3600) / 60) + ":";
		
		if (time % 60 < 10)
			time_string += "0";
		time_string += String.valueOf(time % 60);

		return time_string;
	}

	// method which reads data from file into an array list of customers

	public static List<Customer> initialize_customers(String file) {
		List<Customer> all_customers = new ArrayList<>();

		try {
			Scanner reader = new Scanner(new File(file));
			int id_count = 1;

			while (reader.hasNextLine()) {
				String current = reader.nextLine();				

				if (current.contains("ARRIVAL")) {
					int[] arrival_times = Arrays.stream(current.substring(14, current.length()).split(":")).map(String::trim).mapToInt(Integer::parseInt).toArray();	
					int seconds;

					if (arrival_times[0] >= 8)
						seconds = (arrival_times[0] * 3600) + (arrival_times[1] * 60) + arrival_times[2];
					else
						seconds = ((arrival_times[0] + 12) * 3600) + (arrival_times[1] * 60) + arrival_times[2];

					Customer new_customer = new Customer(id_count++, seconds);
					all_customers.add(new_customer);
				}
			}
		}

		catch (FileNotFoundException ex) { System.out.println("Error reading from file."); }

		finally { return all_customers; }
	}
}