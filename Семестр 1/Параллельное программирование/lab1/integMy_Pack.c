#include <mpi.h>
#include <stdio.h>
#include <math.h>

static double f(double a, double p);
static double fi(double a, double p);

int main(int argc, char *argv[])
{
    int done = 0, n, myid, numprocs, i;
    double myfunk, funk, h, sum, x;
    double xl, xh, p;
    double startwtime, endwtime;
    int namelen;
    char processor_name[MPI_MAX_PROCESSOR_NAME];
    MPI_Status stats;

    FILE *myout = fopen("integPackOut.txt", "w");
    fclose(myout);

    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
    MPI_Comm_rank(MPI_COMM_WORLD, &myid);
    n = 0;
    while (!done)
    {
        int sizePack = 100;
        char packBuffer[sizePack];
        int packPosition = 0;
        if (myid == 0)
        {
            printf("Enter the number of intervals (0 quit) ");
            fflush(stdout);
            scanf("%d", &n);
            if (n == 0)
            {
                MPI_Bcast(&n, 1, MPI_INT, 0, MPI_COMM_WORLD);
                done = 1;
                continue;
            }

            printf("Enter xl ");
            fflush(stdout);
            scanf("%lf", &xl);

            printf("Enter xh ");
            fflush(stdout);
            scanf("%lf", &xh);

            printf("Enter the parameter ");
            fflush(stdout);
            scanf("%lf", &p);

            MPI_Pack(&xl, 1, MPI_DOUBLE, &packBuffer, sizePack, &packPosition, MPI_COMM_WORLD);
            MPI_Pack(&xh, 1, MPI_DOUBLE, &packBuffer, sizePack, &packPosition, MPI_COMM_WORLD);
            MPI_Pack(&p, 1, MPI_DOUBLE, &packBuffer, sizePack, &packPosition, MPI_COMM_WORLD);

            startwtime = MPI_Wtime();
        }

        MPI_Bcast(&n, 1, MPI_INT, 0, MPI_COMM_WORLD);
        if (n == 0)
        {
            done = 1;
            continue;
        }

        MPI_Bcast(&packBuffer, sizePack, MPI_PACKED, 0, MPI_COMM_WORLD);
        if (myid != 0)
        {
            MPI_Unpack(&packBuffer, sizePack, &packPosition, &xl, 1, MPI_DOUBLE, MPI_COMM_WORLD);
            MPI_Unpack(&packBuffer, sizePack, &packPosition, &xh, 1, MPI_DOUBLE, MPI_COMM_WORLD);
            MPI_Unpack(&packBuffer, sizePack, &packPosition, &p, 1, MPI_DOUBLE, MPI_COMM_WORLD);   
        }

        h = (xh - xl) / (double)n;
        sum = 0.0;
        for (i = myid + 1; i <= n; i += numprocs)
        {
            x = xl + h * ((double)i - 0.5);
            sum += f(x, p);
        }
        myfunk = h * sum;

        myout = fopen("integPackOut.txt", "a");

        fprintf(myout, "Process %d SUMM %.16f\n", myid, myfunk);

        fclose(myout);

        MPI_Reduce(&myfunk, &funk, 1, MPI_DOUBLE, MPI_SUM, 0, MPI_COMM_WORLD);

        if (myid == 0)
        {
            FILE *myout = fopen("integPackOut.txt", "a");

            fprintf(myout, "Integral is approximately  %.16f, Error   %.16f\n", funk, abs(funk - fi(xh, p) + fi(xl, p)));
            endwtime = MPI_Wtime();
            fprintf(myout, "Time of calculation = %f\n", endwtime - startwtime);

            fclose(myout);
        }
    }
    MPI_Finalize();
}

static double f(double a, double p)
{
    return (1 / (a * (p * a + 1)));
}

static double fi(double a, double p)
{
    return -log((p * a + 1) / a);
}
