# show-octal-acls-in-ls

## How to use:

```
ls -lA | java lsOktalACL
```



### Difference

```
ls -l
insgesamt 28
-rw-rw-r-- 1 user user 4236 Jan  6 19:54 Indexer.class
-rw-rw-r-- 1 user user 5600 Jan  6 21:09 lsOktalACL.class
drwxrwxr-x 2 user user 4096 Jan  6 14:29 META-INF
drwxrwxr-x 2 user user 4096 Jan  6 19:54 onServer
-rwsrw-r-- 1 user user  122 Jan  6 19:54 Regex_Tester.txt
```

to

```
ls -l|java lsOktalACL 
insgesamt 28
f 664 1 user user 4236 Jan  6 19:54 Indexer.class
f 664 1 user user 5600 Jan  6 21:09 lsOktalACL.class
d 775 2 user user 4096 Jan  6 14:29 META-INF
d 775 2 user user 4096 Jan  6 19:54 onServer
f 4764 1 user user  122 Jan  6 19:54 Regex_Tester.txt
```

f = file
d = dir
s = SUID-Bit (Set User ID) with "x": 6.xxx / 4xxx
S = SUID-Bit (Set User ID) without "x": 6.xxx / 4xxx