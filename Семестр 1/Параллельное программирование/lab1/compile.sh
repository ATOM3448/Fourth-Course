#!/bin/bash
mpiCC src/integMy.c -o build/integMy |
mpiCC src/integMy_Bcast.c -o build/integMyBcast |
mpiCC src/integMy_Pack.c -o build/integMyPack