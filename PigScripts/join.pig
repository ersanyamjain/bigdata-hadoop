a = load '/data/a.txt' as (id:int,name:chararray);
b = load '/data/b.txt' using PigStorage('|') as (id:int,name:chararray);
innerJ = join a by id,b by id;
leftJ = join a by id left outer, b by id;
cgrp = cogroup a by id,b by id;
store innerJ into '/innerJ';
store leftJ into '/leftJ';
store cgrp into '/cgrp';
