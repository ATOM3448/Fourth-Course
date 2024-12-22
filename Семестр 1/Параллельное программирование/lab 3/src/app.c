#include <stdio.h>
#include <mpi.h>
#include <stdlib.h>

/*
Вариант 31
    В квадратной матрице с нечетным числом строк и столбцов выбрать треугольную
часть матрицы с вершиной в центре матрицы и основанием в последнем столбце.
*/

void doMagick(int myRank);
void fillMatrix(int matrixSize, int* matrix);
void printMatrix(int myRank, int* data, int matrixSize);
void buildDerivedType(int* indata, int matrixSize, MPI_Datatype* messageType);
void printVector(int myRank, int* data, int elemsCount);

const int n = 7;

int main(int argc, char* argv[]) {
    int threadsCount;
    int myRank;

    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &threadsCount);
    MPI_Comm_rank(MPI_COMM_WORLD , &myRank);

    doMagick(myRank);

    MPI_Finalize();
    return 0;
}

void doMagick(int myRank) {
    int* indata = (int*)calloc(n*n, sizeof(int));

    MPI_Datatype messageType; 
    buildDerivedType(indata, n, &messageType);

    if (myRank == 0) {
        fillMatrix(n, indata);
        printMatrix(myRank, indata, n);

        MPI_Send(indata, 1, messageType, 1, 0, MPI_COMM_WORLD);
        MPI_Send(indata, 1, messageType, 1, 1, MPI_COMM_WORLD);

        free(indata);
        indata = (int*)calloc(n*n, sizeof(int));

        MPI_Recv(indata, 1, messageType, 1, 0, MPI_COMM_WORLD, NULL);
        printMatrix(myRank, indata, n);
    }

    if (myRank != 0) {
        MPI_Recv(indata, 1, messageType, 0, 0, MPI_COMM_WORLD, NULL);
        printMatrix(myRank, indata, n);

        free(indata);
        int elemsCount = n/2+1;
        for (int i = 0; i < n/2; i++) {
            elemsCount += (i+1)*2;
        }
        indata = (int*)calloc(elemsCount, sizeof(int));
        
        MPI_Recv(indata, elemsCount, MPI_INT, 0, 1, MPI_COMM_WORLD, NULL);
        printVector(myRank, indata, elemsCount);

        MPI_Send(indata, elemsCount, MPI_INT, 0, 0, MPI_COMM_WORLD);
    }

    MPI_Type_free(&messageType);
    free(indata);
}

void fillMatrix(int matrixSize, int* matrix) {
    int elemsCount = matrixSize*matrixSize;

    for (int i = 0; i < elemsCount; i++) {
        matrix[i] = i+1;
    }
}

void printMatrix(int myRank, int* data, int matrixSize) {
    printf("Вывод процесса #%d\n", myRank);
    for (int i = 0; i < matrixSize; i++) {
        for (int j = 0; j < matrixSize; j++) {
            printf("%4d ", data[i*matrixSize+j]);
        }
        printf("\n");
    }
}

void buildDerivedType(int* indata, int matrixSize, MPI_Datatype* messageType) {
    int* blockLenghts = (int*)malloc(sizeof(int)*matrixSize);
    int* displacements = (int*)malloc(matrixSize*sizeof(int));

    int medianCollumn = matrixSize/2;

    for (int i = 0; i < matrixSize; i++) {
        blockLenghts[i] = matrixSize - medianCollumn - abs(medianCollumn-i);

        displacements[i] = matrixSize*i + (matrixSize - blockLenghts[i]);
    }

    MPI_Type_indexed(matrixSize, blockLenghts, displacements, MPI_INT, messageType);
    MPI_Type_commit(messageType);
}

void printVector(int myRank, int* data, int elemsCount) {
    printf("Вывод процесса #%d\n", myRank);

    for (int i = 0; i < elemsCount; i++) {
        printf("%4d ", data[i]);
    }
    printf("\n");
}