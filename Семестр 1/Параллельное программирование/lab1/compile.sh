#!/bin/bash
mpiCC integMy.c -o integMy |
mpiCC integMy_Bcast.c -o integMyBcast |
mpiCC integMy_Pack.c -o integMyPack