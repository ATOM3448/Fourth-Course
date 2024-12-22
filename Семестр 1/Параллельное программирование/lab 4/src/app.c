#include <omp.h>
#include <stdlib.h>
#include <stdio.h>
#include <math.h>

double f(double a);
double fi(double a);

const double xl = 0.5;
const double xh = 2.0;
const double p = 1.5;

//const int n = 10000000;
const int n = 1000000;

int main(int argc, char *argv[])
{   
    int threadsMaxCount = 8;
    if (argc == 2) {
        threadsMaxCount = atoi(argv[1]);
    }

    printf("\nMax threads = %d\n", omp_get_max_threads());
    
    double startTime = omp_get_wtime();

    double sum = 0.0;
    double h = (xh-xl) / (double)n;

    #pragma omp parallel num_threads(threadsMaxCount)
    {
        #pragma omp master 
        {
            printf("\nNum threads = %d\n", omp_get_num_threads());
        }

        printf("\nThist thread #%d\n", omp_get_thread_num());

        double localSum = 0.0;

        //#pragma omp for schedule(static, 1000000)
        #pragma omp for schedule(dynamic, 1000000)
        for (int i = 1; i <= n; i++) {
            double x = xl + h * ((double)i - 0.5);
            localSum += f(x);
        }
        localSum *= h;

        #pragma omp atomic
        sum += localSum;
    }

    double endTime = omp_get_wtime();

    printf("Running time %lf\n", endTime-startTime);

    FILE *myout = fopen("../out/appOut.txt", "w");
    fprintf(myout, "Integral is approximately  %.16f, Error   %.16f\n", sum, fabs(sum - fi(xh) + fi(xl)));
    fclose(myout);
}

double f(double a)
{
    return (1 / (a * (p * a + 1)));
}

double fi(double a)
{
    return -log((p * a + 1) / a);
}