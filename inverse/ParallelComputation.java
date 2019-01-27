package inverse;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class ParallelComputation implements Callable {
	ArrayList<ArrayList<Double>> matrix;
	ArrayList<ArrayList<Double>> minor;
	int line, column;
	int lowwer, upper;

	ParallelComputation(ArrayList<ArrayList<Double>> matrix, int lowwer, int upper) {
		this.lowwer = lowwer;
		this.upper = upper;
		this.matrix = new ArrayList<ArrayList<Double>>();
		for (int i = 0; i < matrix.size(); i++) {
			ArrayList<Double> c = new ArrayList<Double>();
			this.matrix.add(c);
			for (int j = 0; j < matrix.get(i).size(); j++) {
				this.matrix.get(i).add(matrix.get(i).get(j));
			}
		}
	}

	private void copy(ArrayList<ArrayList<Double>> copy_matrix) {
		for (int i = 0; i < matrix.size(); i++) {
			ArrayList<Double> c = new ArrayList<Double>();
			copy_matrix.add(c);
			for (int j = 0; j < matrix.get(i).size(); j++) {
				copy_matrix.get(i).add(matrix.get(i).get(j));
			}
		}
	}

	double cofactor(ArrayList<ArrayList<Double>> matrix) {
		double sum = 0;
		minor = new ArrayList<ArrayList<Double>>();
		for (int i = 0; i < matrix.size(); i++) {
			ArrayList<Double> c = new ArrayList<Double>();
			minor.add(c);
			for (int j = 0; j < matrix.get(i).size(); j++) {
				minor.get(i).add(matrix.get(i).get(j));
			}
		}
		for (int i = 0; i < minor.size(); i++) {
			if (minor.size() == 1) {
				double y = minor.get(0).get(0);
				return y;
			}
			minor.remove(0);
			for (int j = 0; j < minor.size(); j++) {
				minor.get(j).remove(i);
			}
			if (i % 2 == 0)
				sum += (matrix.get(0).get(i) * cofactor(minor));
			else
				sum -= (matrix.get(0).get(i) * cofactor(minor));
			minor.clear();
			for (int k = 0; k < matrix.size(); k++) {
				ArrayList<Double> c = new ArrayList<Double>();
				minor.add(c);
				for (int j = 0; j < matrix.get(k).size(); j++) {
					minor.get(k).add(matrix.get(k).get(j));
				}
			}
		}
		return sum;
	}

	@Override
	public ArrayList<ArrayList<Double>> call() throws Exception {
		// TODO Auto-generated method stub
		ArrayList<ArrayList<Double>> copy_matrix = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> inverse_matrix = new ArrayList<ArrayList<Double>>();
		copy(copy_matrix);
		for (int i = 0; i < matrix.size(); i++) {
			ArrayList<Double> line = new ArrayList<Double>();
			for (int j = lowwer; j < upper; j++) {
				copy_matrix.remove(i);
				for (int k = 0; k < copy_matrix.size(); k++)
					copy_matrix.get(k).remove(j);
				if ((i + j) % 2 == 0) {
					line.add(cofactor(copy_matrix));
				} else {
					line.add((-1) * cofactor(copy_matrix));
				}
				copy_matrix.clear();
				copy(copy_matrix);
			}
			inverse_matrix.add(line);
		}
		return inverse_matrix;
	}
}