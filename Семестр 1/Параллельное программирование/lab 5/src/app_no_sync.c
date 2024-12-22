#include <stdlib.h>
#include <stdio.h>
#include <math.h>

#include <unistd.h>
#include <semaphore.h>
#include <pthread.h>

/*
    Вариант 3. 
    Читатели-писатели. Приоритет читателей. Два семафора и счетчик (п. 3.1).
*/

int ITER_COUNT = 150;

const int READERS_COUNT = 6;
const int WRITERS_COUNT = 6;

sem_t readerSem;
sem_t writerSem;
int readersCounter = 0;

void initReaders(pthread_t* readers);
void initWriters(pthread_t* writers);
void initWritersAndReaders(pthread_t* readers, pthread_t* writers);
void initReadersAndWriters(pthread_t* writers, pthread_t* readers);

void *reader(void *prm);
void tryToTurnOn();
void tryToTurnOff();

void *writer(void *prm);


int* resource;

int main(int argc, char* argv[]) {
    scanf("%d", &ITER_COUNT);

    resource = (int*)calloc(1, sizeof(int));

    pthread_t threadsRE[READERS_COUNT];
    pthread_t threadsWR[WRITERS_COUNT];

    sem_init(&readerSem, 0, 1);
    sem_init(&writerSem, 0, 1);

    //initWriters(&threadsWR[0]);
    //initReaders(&threadsRE[0]);

    //initReaders(&threadsRE[0]);
    //initWriters(&threadsWR[0]);

    initWritersAndReaders(&threadsWR[0], &threadsRE[0]);

    //initReadersAndWriters(&threadsRE[0], &threadsWR[0]);


    pthread_exit(NULL);

    sem_destroy(&readerSem);
    sem_destroy(&writerSem);

    return 0;
}

void initReaders(pthread_t* readers) {
    for (int i = 0; i < READERS_COUNT; i++) {
        pthread_create(&(readers[i]), NULL, reader, (void*)&i);
    }
}

void initWriters(pthread_t* writers) {
    for (int i = 0; i < WRITERS_COUNT; i++) {
        pthread_create(&(writers[i]), NULL, writer, (void*)&i);
    }
}

void initWritersAndReaders(pthread_t* readers, pthread_t* writers) {
    for (int i = 0 ; i < fmax(WRITERS_COUNT, READERS_COUNT); i++) {
        //sleep(1);
        if (i < WRITERS_COUNT) {
            pthread_create(&(writers[i]), NULL, writer, (void*)&i);
        }
        //sleep(1);
        if (i < READERS_COUNT) {
            pthread_create(&(readers[i]), NULL, reader, (void*)&i);
        }
    }
}

void initReadersAndWriters(pthread_t* writers, pthread_t* readers) {
    for (int i = 0 ; i < fmax(WRITERS_COUNT, READERS_COUNT); i++) {
        if (i < READERS_COUNT) {
            pthread_create(&(readers[i]), NULL, reader, (void*)&i);
        }
        if (i < WRITERS_COUNT) {
            pthread_create(&(writers[i]), NULL, writer, (void*)&i);
        }
    }
}

void* reader(void* reader_id) {
    int myId = *(int*)reader_id;
    for (int i = 0; i < ITER_COUNT; i++) {
        //tryToTurnOn();
        //sleep(1);

        printf("Читатель #%d читает \"%d\"\n", myId, *resource);
        
        //tryToTurnOff();
    }
}

void tryToTurnOn() {
    sem_wait(&readerSem);
    
    if (++readersCounter == 1) {
        sem_wait(&writerSem);
    }

    sem_post(&readerSem);
}

void tryToTurnOff() {
    sem_wait(&readerSem);

    if (--readersCounter == 0) {
        sem_post(&writerSem);
    }

    sem_post(&readerSem);
}

void* writer(void* writer_id) {
    int myId = *(int*)writer_id;
    for (int i = 0; i < ITER_COUNT; i++) {
        //sem_wait(&writerSem);
        //sleep(1);
        printf("Писатель #%d пишет \"%d\"\n", myId, *resource = 1+rand()%20);

        //sem_post(&writerSem);
    }
}