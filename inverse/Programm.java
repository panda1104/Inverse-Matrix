package inverse;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Programm {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		Matrix table = new Matrix(10);// создается матрица 10 на 10
		System.out.println("Матрица:");
		table.print();
		long start = System.currentTimeMillis();
		ArrayList<ArrayList<Double>> inverse_matrix = table.get_inverse_matrix(); // считаем обратную матрицу
		long end = System.currentTimeMillis();
		System.out.println("Время:" + (end - start));
		Matrix inverse_table = new Matrix(inverse_matrix); // записываем высчитанную обратную матрицу в новый объект
		System.out.println("Обратная матрица:");
		inverse_table.print();
		start = System.currentTimeMillis();
		ArrayList<ArrayList<Double>> double_inverse_matrix = inverse_table.get_inverse_matrix(); // ищем обратную матрицу к обратной матрице 
		end = System.currentTimeMillis();
		System.out.println("Время:" + (end - start));
		Matrix double_inverse_table = new Matrix(double_inverse_matrix);
		System.out.println("Дважды обратная матрица:");
		double_inverse_table.print();
		if(table.equals(double_inverse_matrix)) // сравниваем исходную и полученную матрицу 
			System.out.println("Success");
		else
			System.out.println("Error");
	}
}
