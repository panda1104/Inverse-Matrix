#pragma once
#include "Сofactor.h"
class Matrix {
private:
	deque<deque<double>> matrix;
	deque<int> numbers;
	int width, length;
	const int n_threads = 4;
public:

	Matrix(int w){
		srand(time(NULL));
		width = w;
		length = w;
		for (int i = 0; i < length; ++i) {
			deque<double> line;
			for (int j = 0; j < width; ++j) {
				line.push_back((double)(rand()) / RAND_MAX * 10);
			}
			matrix.push_back(line);
		}
		double portion = get_portion();
		double count = 0;
		numbers.push_back(0);
		while (abs(count - matrix.size()) > 0.001)
		{
			count += portion;
			numbers.push_back((int)count);
		}
	}
	double get_portion()
	{
		return (((double)matrix.size()) / n_threads);
	}
	Matrix(deque<deque<double>> matrix){
		width = matrix.size();
		length = matrix.size();
		this->matrix = matrix;
		double portion = get_portion();
		double count = 0;
		numbers.push_back(0);
		while (abs(count - matrix.size()) > 0.001)
		{
			count += portion;
			numbers.push_back((int)count);
		}
	}

	void print() {
		for (int i = 0; i < matrix.size(); i++) {
			for (int j = 0; j < matrix[i].size(); j++) {
				cout<< matrix[i][j] << " ";
			}
			cout << endl;
		}
	}

	bool equals(deque<deque<double>> matrix) {
		for (int i = 0; i < this->matrix.size(); i++)
			for (int j = 0; j < this->matrix[i].size(); j++)
				if (abs(this->matrix[i][j] - matrix[i][j]) > 0.0001)
					return false;
		return true;
	}
	deque<deque<double>> get_mass() {
		return matrix;
	}

	deque<deque<double>> get_inverse_matrix()
	{
		double determinant = 0;
		deque<deque<double>> inverse_matrix;

		deque<pthread_t> ThreadArray;
		ThreadArray.resize(n_threads); // создание контейнера потоков 
		
		deque<Args*> ThreadArgs;
		for (int i = 0; i < n_threads; i++)
		{
			ThreadArgs.push_back(new Args());
		}
	for (int i = 0; i < n_threads; i++) {
		ThreadArgs[i]->matrix = matrix;
		ThreadArgs[i]->lowwer = numbers[i];
		ThreadArgs[i]->upper = numbers[i + 1];
		pthread_create(&ThreadArray[i], nullptr, call, ThreadArgs[i]); // указатель на поток, ..., указатель на функцию для выполнения, аргументы для функции
	}
	for (int i = 0; i < length; i++) {
		deque<double> line;
		for (int j = 0; j < n_threads; j++) {
			while (!pthread_join(ThreadArray[j], NULL)){}
				for (int k = 0; k < ThreadArgs[j]->result_matrix[i].size(); k++)
				{
					line.push_back(ThreadArgs[j]->result_matrix[i][k]);
				}
		}
		inverse_matrix.push_back(line);
	}
	for (int i = 0; i < inverse_matrix[0].size(); ++i)
	{
		determinant += matrix[0][i] * inverse_matrix[0][i];
	}
	for (int i = 0; i < inverse_matrix.size(); i++) {
		for (int j = i + 1; j < inverse_matrix[i].size(); j++) {
			double tmp = inverse_matrix[i][j];
			inverse_matrix[i][j] = inverse_matrix[j][i];
			inverse_matrix[j][i] = tmp;
		}
	}
	for (int i = 0; i < inverse_matrix.size(); ++i) {
		for (int j = 0; j < inverse_matrix[i].size(); ++j) {
			inverse_matrix[i][j] = inverse_matrix[i][j] / determinant;
		}
	}
	return inverse_matrix;
	}
};