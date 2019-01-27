package inverse;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class Matrix {
	private ArrayList<ArrayList<Double>> matrix;
	private int width, length;
	private int n_threads = 4;
	private ArrayList<Integer> numbers; // массив границ для потоков 
	private ExecutorService tp;
	

	Matrix(int w) {
		tp = Executors.newFixedThreadPool(n_threads);
		width = w;
		length = w;
		matrix = new ArrayList<ArrayList<Double>>();
		for (int i = 0; i < length; ++i) {
			ArrayList<Double> line = new ArrayList<Double>();
			for (int j = 0; j < width; ++j) {
				line.add((Math.random() * 10));
			}
			matrix.add(line);
		}
		double portion = get_portion();
		double count = 0;
		numbers = new ArrayList<Integer>();
		numbers.add(0);
		while (Math.abs(count - matrix.size()) > 0.001)
		{
			count += portion;
			numbers.add((int)count);
		}
	}

	Matrix(ArrayList<ArrayList<Double>> matrix) { // конструктр копирования 
		tp = Executors.newFixedThreadPool(n_threads);
		
		width = matrix.size();
		length = matrix.size();
		this.matrix = new ArrayList<ArrayList<Double>>();
		for (int i = 0; i < matrix.size(); i++) {
			ArrayList<Double> tmp = new ArrayList<Double>();
			for (int j = 0; j < matrix.get(i).size(); j++) {
				tmp.add(matrix.get(i).get(j));
			}
			this.matrix.add(tmp);
		}
		double portion = get_portion();
		double count = 0;
		numbers = new ArrayList<Integer>();
		numbers.add(0);
		while (Math.abs(count - matrix.size()) > 0.001)
		{
			count += portion;
			numbers.add((int)count);
		}
	}
	
	double get_portion()
	{
		return (((double)matrix.size()) / n_threads);
	}

	void print() {
		for (int i = 0; i < matrix.size(); i++) {
			for (int j = 0; j < matrix.get(i).size(); j++) {
				System.out.print(matrix.get(i).get(j) + " ");
			}
			System.out.println();
		}
	}
    
	boolean equals(ArrayList<ArrayList<Double>> matrix) {
		for(int i = 0; i < this.matrix.size();i++) 
			for(int j = 0; j < this.matrix.get(i).size();j++)
				if(Math.abs(this.matrix.get(i).get(j) - matrix.get(i).get(j)) > 0.0001)
					return false;
		return true;
	}
	ArrayList<ArrayList<Double>> get_mass() {
		return matrix;
	}

	ArrayList<ArrayList<Double>> get_inverse_matrix() throws InterruptedException, ExecutionException {
		double determinant = 0;
		ArrayList<ArrayList<Double>> inverse_matrix = new ArrayList<ArrayList<Double>>();
		ArrayList<Future> results = new ArrayList<Future>();
		for (int i = 0; i < n_threads; i++) {
			ParallelComputation item = new ParallelComputation(matrix, numbers.get(i), numbers.get(i + 1));
			
			Future task =  tp.submit(item);
			results.add(task);
//			Thread t = new Thread(task);
//			t.start();
		}
		for (int i = 0; i < n_threads; i++) {
			while (!results.get(i).isDone()) {

			}
		}
		for(int i = 0; i < length; i++) {
			ArrayList<Double> line = new ArrayList<Double>();
			for(int j = 0; j < n_threads;j++) {
				ArrayList<Double> item = new ArrayList<Double>();
				item = ((ArrayList<ArrayList<Double>>) results.get(j).get()).get(i);
				for(int k = 0; k < item.size(); k++) {
					line.add(item.get(k));
				}
			}
			inverse_matrix.add(line);
		}
		
		// до этого момента в inverse_matrix хранятся алгебраические дополнения 
		for (int i = 0; i < inverse_matrix.get(0).size(); ++i)
		{
			determinant += matrix.get(0).get(i) * inverse_matrix.get(0).get(i);
		}
		for (int i = 0; i < inverse_matrix.size(); i++) { // транспонируем матрицу 
			for (int j = i + 1; j < inverse_matrix.get(i).size(); j++) {
				double tmp = inverse_matrix.get(i).get(j);
				inverse_matrix.get(i).set(j, inverse_matrix.get(j).get(i));
				inverse_matrix.get(j).set(i, tmp);
			}
		}
		for (int i = 0; i < inverse_matrix.size(); ++i) {
			for (int j = 0; j < inverse_matrix.get(i).size(); ++j) {
				double elem = inverse_matrix.get(i).get(j) / determinant;
				inverse_matrix.get(i).set(j, elem);
			}
		}
		return inverse_matrix;
	}
}
