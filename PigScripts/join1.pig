a = load '$fileA' as (id:int,name:chararray);
b = load '$fileB' using PigStorage('|') as (id:int,name:chararray);
innerJ = join a by id,b by id;
leftJ = join a by id left outer, b by id;
cgrp = cogroup a by id,b by id;
store innerJ into '$inner';
store leftJ into '$left';
store cgrp into '$cgrp';
