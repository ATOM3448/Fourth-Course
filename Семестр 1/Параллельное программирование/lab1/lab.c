// Вариант 23
// Метод 1

#include <mpi.h>
#include <stdio.h>
#include <math.h>
#include <stdbool.h>

static double f(double);
static double fi(double);

int main(int argc, char *argv[])
{
    double a = 0.5, b = 4.0, Eps;
    bool notDone = true;
    int i, n, rank, size, Ierr, namelen;
    double Sum, Gsum, Isum, time1, time2, a1, b1, x, F;
    char processor_name[MPI_MAX_PROCESSOR_NAME];
    MPI_Status stats;

    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Get_processor_name(processor_name, &namelen);

    printf("Process %d of %d on %s\n", rank, size, processor_name);

    Eps = 1.0;
    while (notDone)
    {
        if (rank == 0)
        {
            printf("Enter Eps ( > 0.1 - quit) ");
            fflush(stdout);
            scanf("%lf", &Eps);
            printf("%10.9lf\n", Eps);
            time1 = MPI_Wtime();

            for (i = 1; i < size; i++)
            {
                MPI_Send(&Eps, 1, MPI_DOUBLE, i, 1, MPI_COMM_WORLD);
            }
        }

        else
            MPI_Recv(&Eps, 1, MPI_DOUBLE, 0, 1, MPI_COMM_WORLD, &stats);

        if (Eps > 0.1)
            notDone = false;
        else
        {
            a1 = a + (b - a) * rank / size;
            b1 = a1 + (b - a) / size;
            n = 1.0 / (Eps * size);

            Sum = 0.0;
            for (i = 1; i <= n; i++)
            {
                x = a1 + (b1 - a1) * (i - 0.5) / n;
                Sum += f(x);
            }
            printf("Process %d SUMM  %.16f \n", rank, Sum / (n * size) * (b - a));

            if (rank != 0)
                MPI_Send(&Sum, 1, MPI_DOUBLE, 0, 1, MPI_COMM_WORLD);

            if (rank == 0)
            {
                Gsum = Sum;
                for (i = 1; i < size; i++)
                {
                    MPI_Recv(&Isum, 1, MPI_DOUBLE, i, 1, MPI_COMM_WORLD, &stats);
                    Gsum += Isum;
                };

                Gsum = Gsum / (n * size) * (b - a);

                printf("\nIntegral of function ln(1/x) from %5.2f to %5.2f.\n", a, b);
                printf("%d point. Integral = %18.16f, error = %18.16f.\n\n", n * size, Gsum, fi(b) - fi(a) - Gsum);
                time2 = MPI_Wtime();
                printf("Time calculation = %f seconds.\n", time2 - time1);
                fflush(stdout);
            }
        }
    }
    MPI_Finalize();
    return 0;
}

static double f(double a)
{
    return (log(1 / a));
}

static double fi(double a)
{
    return ((log(1 / a) + 1) * a);
}
