#include <mpi.h>
#include <stdio.h>
#include <math.h>

static double f(double a);
static double fi(double a);

int main(int argc, char *argv[])
{
    int done = 0, n = 10000, myid, numprocs, i;
    double myfunk, funk, h, sum, x;
    double xl = 0.5, xh = 2;
    double startwtime, endwtime;
    int namelen;
    char processor_name[MPI_MAX_PROCESSOR_NAME];
    MPI_Status stats;

    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
    MPI_Comm_rank(MPI_COMM_WORLD, &myid);
    n = 0;
    while (!done)
    {
        if (myid == 0)
        {
            printf("Enter the number of intervals (0 quit) ");

            fflush(stdout);
            scanf("%d", &n);

            startwtime = MPI_Wtime();

            for (i = 1; i < numprocs; i++)
            {
                MPI_Send(&n, 1, MPI_INT, i, 1, MPI_COMM_WORLD);
            }
        }

        else
        {
            MPI_Recv(&n, 1, MPI_INT, 0, 1, MPI_COMM_WORLD, &stats);
        }
        if (n == 0)
            done = 1;
        else
        {
            h = (xh - xl) / (double)n;
            sum = 0.0;
            for (i = myid + 1; i <= n; i += numprocs)
            {
                x = xl + h * ((double)i - 0.5);
                sum += f(x);
            }
            myfunk = h * sum;

            if (myid != 0)
            {
                MPI_Send(&myfunk, 1, MPI_DOUBLE, 0, 1, MPI_COMM_WORLD);
            }

            if (myid == 0)
            {

                funk = myfunk;
                for (i = 1; i < numprocs; i++)
                {
                    MPI_Recv(&myfunk, 1, MPI_DOUBLE, i, 1, MPI_COMM_WORLD, &stats);
                    funk += myfunk;
                };

                endwtime = MPI_Wtime();

                FILE* myout = fopen("../out/integMyOut.txt", "w");
                fprintf(myout, "Integral is approximately  %.16f, Error   %.16f\n", funk, abs(funk - fi(xh) + fi(xl)));
                fprintf(myout, "Time of calculation = %f\n", endwtime - startwtime);

                fclose(myout);
            }
        }
    }
    MPI_Finalize();
}

static double f(double a)
{   
    return (1/(a*(1.5*a+1)));
}

static double fi(double a)
{
    return -log((1.5*a+1)/a);
}
