package inverse;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Programm {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		Matrix table = new Matrix(10);// ��������� ������� 10 �� 10
		System.out.println("�������:");
		table.print();
		long start = System.currentTimeMillis();
		ArrayList<ArrayList<Double>> inverse_matrix = table.get_inverse_matrix(); // ������� �������� �������
		long end = System.currentTimeMillis();
		System.out.println("�����:" + (end - start));
		Matrix inverse_table = new Matrix(inverse_matrix); // ���������� ����������� �������� ������� � ����� ������
		System.out.println("�������� �������:");
		inverse_table.print();
		start = System.currentTimeMillis();
		ArrayList<ArrayList<Double>> double_inverse_matrix = inverse_table.get_inverse_matrix(); // ���� �������� ������� � �������� ������� 
		end = System.currentTimeMillis();
		System.out.println("�����:" + (end - start));
		Matrix double_inverse_table = new Matrix(double_inverse_matrix);
		System.out.println("������ �������� �������:");
		double_inverse_table.print();
		if(table.equals(double_inverse_matrix)) // ���������� �������� � ���������� ������� 
			System.out.println("Success");
		else
			System.out.println("Error");
	}
}
