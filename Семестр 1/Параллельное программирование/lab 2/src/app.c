#include <stdio.h>
#include <mpi.h>
#include <stdlib.h>

/*
Вычислить Y[i] = a * X[i] + Y[i] (SAXPY) для массива случайных чисел.
Определить максимальное и минимальное значения полученного массива.
*/

/*
    ПРИМЕЧАНИЯ:
    1.  Части при расчете индексов учавствует число 2.
    Это количество векторов, с которыми идет работа (XY).
    2.  Размерность saxpyArray - (2, n).
*/

const int n = 100000;
const float dp = 50.0;
const float a = 0.5;

void saxpyArraysInit(float* saxpyArray);
void sendingParamsSet(int* sendCounts, int* displs, int threadsCount,
        int elementsCountOnThread, int extraElementsCount);
void saxpyCompute(float* saxpyArray, int saxpyArraySize, float* result);
void recvParamsSet(int* recvCounts, int* displs, int threadsCount,
        int elementsCountOnThread, int extraElementsCount);
void minmax(float* array, int size, float* min, float* max);

int main(int argc, char *argv[])
{
    int threadsCount;
    int myRank;

    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &threadsCount);
    MPI_Comm_rank(MPI_COMM_WORLD, &myRank);

    printf("Proccess %d of %d is started\n", myRank, threadsCount);

    float* saxpyArray;

    int elementsCountOnThread = n / threadsCount;
    int extraElementsCount = n % threadsCount;
    
    int* sendCounts;
    int* displs;
    if (myRank == 0) {
        int arraySize = threadsCount * sizeof(int);

        sendCounts = (int*)malloc(arraySize);
        displs = (int*)malloc(arraySize);
        sendingParamsSet(sendCounts, displs, threadsCount, elementsCountOnThread, extraElementsCount);

        saxpyArray = (float*)malloc(n * 2 * sizeof(float));
        saxpyArraysInit(saxpyArray);
    }
    
    int currentThreadElementsCount = elementsCountOnThread;

    if (myRank < extraElementsCount) {
        currentThreadElementsCount++;
    }

    float* currentThreadData = (float*) malloc(currentThreadElementsCount * 2 * sizeof(float));

    MPI_Scatterv(saxpyArray, sendCounts, displs, MPI_FLOAT,
            currentThreadData, currentThreadElementsCount * 2, MPI_FLOAT,
            0, MPI_COMM_WORLD);

    float* currentThreadResult = (float*)malloc(currentThreadElementsCount * sizeof(float));
    saxpyCompute(currentThreadData, currentThreadElementsCount, currentThreadResult);

    int* recvCounts;
    if (myRank == 0) {
        recvCounts = (int*)malloc(threadsCount * sizeof(int));
        recvParamsSet(recvCounts, displs, threadsCount, elementsCountOnThread, extraElementsCount);
    }
    
    float* result = (float*)malloc(n * sizeof(float));
    
    MPI_Gatherv(currentThreadResult, currentThreadElementsCount, MPI_FLOAT,
            result, recvCounts, displs, MPI_FLOAT,
            0, MPI_COMM_WORLD);


    free(currentThreadData);
    free(currentThreadResult);


    if (myRank == 0) {
        float* min = (float*)malloc(sizeof(float));
        float* max = (float*)malloc(sizeof(float));

        minmax(result, n, min, max);

        printf("Results:\n\tFirst: %8.2f\n\tLast: %8.2f\n\tMin: %8.2f\n\tMax: %8.2f\n", result[0], result[n-1], *min, *max);

        free(saxpyArray);
        free(sendCounts);
        free(displs);
        free(result);
    }

    MPI_Finalize();
    return 0;
}

void saxpyArraysInit(float* saxpyArray) {
    int elemsCount = n*2;

    for (int i = 0; i < elemsCount; i++) {
        saxpyArray[i] = (float)rand() / RAND_MAX * dp * 2.0f - dp;
    }
}

void sendingParamsSet(int* sendCounts, int* displs, int threadsCount,
        int elementsCountOnThread, int extraElementsCount) {

    elementsCountOnThread++;
    for (int i = 0; i < extraElementsCount; i++) {
        sendCounts[i] = elementsCountOnThread * 2;
        displs[i] = i * elementsCountOnThread * 2;
    }

    elementsCountOnThread--;
    for (int i = extraElementsCount; i < threadsCount; i++) {
        sendCounts[i] = elementsCountOnThread * 2;
        displs[i] = (elementsCountOnThread * i + extraElementsCount) * 2;
    }
}

void saxpyCompute(float* saxpyArray, int saxpyArraySize, float* result) {
    for (int i = 0; i < saxpyArraySize; i++) {
        int xInd = i*2;
        result[i] =  a * saxpyArray[xInd] + saxpyArray[xInd+1];
    }
}

void recvParamsSet(int* recvCounts, int* displs, int threadsCount,
        int elementsCountOnThread, int extraElementsCount) {
    elementsCountOnThread++;
    for (int i = 0; i < extraElementsCount; i++) {
        recvCounts[i] = elementsCountOnThread;
        displs[i] = i * elementsCountOnThread;
    }

    elementsCountOnThread--;
    for (int i = extraElementsCount; i < threadsCount; i++) {
        recvCounts[i] = elementsCountOnThread;
        displs[i] = elementsCountOnThread * i + extraElementsCount;
    }
}

void minmax(float* array, int size, float* min, float* max) {
    *min = array[0];
    *max = array[0];
    for (int i = 1; i < size; i++) {
        if (*min > array[i]) {
            *min = array[i];
            continue;
        }
        if (*max < array[i]) {
            *max = array[i];
        }
    }
}