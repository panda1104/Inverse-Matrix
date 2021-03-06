// Inverse_matrix_computation.cpp: определяет точку входа для консольного приложения.
//

#include "stdafx.h"
#include "Parallel_computation.h"
int main()
{
	setlocale(LC_ALL, "Russian");
	Matrix table(7);
	cout<<"Матрица:"<<endl;
	table.print();
	unsigned int start = clock();
	deque<deque<double>> inverse_matrix = table.get_inverse_matrix();
	unsigned int end = clock();
	cout<<"Время:" <<" " << (end - start) << endl;
	Matrix inverse_table(inverse_matrix);
	cout<<"Обратная матрица:"<<endl;
	inverse_table.print();
	start = clock();
	deque<deque<double>> double_inverse_matrix = inverse_table.get_inverse_matrix();
	end = clock();
	cout << "Время:" << (end - start) << endl;
	Matrix double_inverse_table(double_inverse_matrix);
	cout << "Дважды обратная матрица:" << endl;
	double_inverse_table.print();
	if (table.equals(double_inverse_matrix))
		cout << "Success"<<endl;
	else
		cout << "Error"<<endl;
	start = clock();
	deque<deque<double>> OpenMP_inverse_matrix = calculation_openMP(inverse_matrix);
	end = clock();
	cout << "Время обработки с помощью OpenMP:" << end - start << endl;
	Matrix OpenMP_inverse_table(OpenMP_inverse_matrix);
	OpenMP_inverse_table.print();
	if (table.equals(OpenMP_inverse_matrix))
		cout << "Success";
	else
		cout << "Error";
	return 0;
}

