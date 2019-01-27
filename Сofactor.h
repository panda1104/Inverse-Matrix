#pragma once
#define HAVE_STRUCT_TIMESPEC
#include "pthread.h"
#include <iostream>
#include "math.h"
#include <omp.h>
#include <stdio.h>
#include <conio.h>
#include <ctime>
#include <deque>
#include <list>
#include <omp.h>
using namespace std;
struct Args
{
	deque<deque<double>> matrix;
	int lowwer;
	int upper;
	deque<deque<double>> result_matrix;
	Args()
	{
		lowwer = 0;
		upper = 0;
	}
};
double GetMatrixVaL(deque<deque<double>>& matrix)
{
	double sum = 0;
	deque<deque<double>> minor = matrix;
	for (int i = 0; i < minor.size(); i++)
	{
		if (minor.size() == 1)
			return minor[0][0];
		minor.pop_front();
		for (int j = 0; j < minor.size(); j++)
		{
			minor[j].erase(minor[j].begin() + i);
		}
		if (!(i % 2))
			sum += (matrix[0][i] * GetMatrixVaL(minor));
		else
			sum -= (matrix[0][i] * GetMatrixVaL(minor));
		minor = matrix;
	}
	return sum;
}
static void* call(void* args)
{
	auto arg = (Args*)(args);
	deque<deque<double>> inverse_matrix;
	inverse_matrix.resize(arg->matrix.size());
	for (int i = 0; i < inverse_matrix.size(); ++i)
	{
		inverse_matrix[i].resize(arg->upper - arg->lowwer);
	}
	deque<deque<double>> copymatrix = arg->matrix;
	for (int i = 0; i < arg->matrix.size(); i++)
	{
		for (int j = arg->lowwer; j < arg->upper; j++)
		{
			copymatrix.erase(copymatrix.begin() + i);
			for (int k = 0; k < copymatrix.size(); k++)
				copymatrix[k].erase(copymatrix[k].begin() + j);
			if ((i + j) % 2 == 0)
				inverse_matrix[i][j - arg->lowwer] = GetMatrixVaL(copymatrix);
			else
				inverse_matrix[i][j - arg->lowwer] = (-1)*GetMatrixVaL(copymatrix);
			copymatrix = arg->matrix;
		}
	}
	arg->result_matrix = inverse_matrix;
	return nullptr;
}
void erase(deque<deque<double>>& matrix, int i)
{
	for (int j = i; j < matrix.size() - 1; j++)
	{
		deque<double> tmp = matrix[j];
		matrix[j] = matrix[j + 1];
		matrix[j + 1] = tmp;
	}
	matrix.pop_back();
}
void erase(deque<double>& matrix, int i)
{
	for (int j = i; j < matrix.size() - 1; j++)
	{
		double tmp = matrix[j];
		matrix[j] = matrix[j + 1];
		matrix[j + 1] = tmp;
	}
	matrix.pop_back();
}
deque<deque<double>> calculation_openMP(deque<deque<double>>& matrix)
{
	deque<deque<double>> inverse_matrix;
	double determinant = 0;
	inverse_matrix.resize(matrix.size());
	for (int i = 0; i < inverse_matrix.size(); ++i)
	{
		inverse_matrix[i].resize(matrix.size());
	}
	unsigned int n_threads = 4;
	omp_set_num_threads(n_threads);
	
	
	#pragma omp parallel 
	{
		long i, j, k, n = matrix.size(), m = matrix.size() - 1;
     #pragma omp for 
		for (i = 0; i < n; i++)
		{
			for (j = 0; j < n; j++)
			{
				deque<deque<double>> copymatrix = matrix;
				erase(copymatrix, i);
				for (k = 0; k < m; k++)
				{
					erase(copymatrix[k], j);
				}
				if ((i + j) % 2 == 0)
					inverse_matrix[i][j] = GetMatrixVaL(copymatrix);
				else
					inverse_matrix[i][j] = (-1)*GetMatrixVaL(copymatrix);
			}
		}
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