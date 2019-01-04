Compiler In&Out File Regulations
===================

Powered By @author 844264935@qq.com

## File Encoding

- All in UTF-8 unicode

## Lexical Analysis Part

### NFA In&Out File Regulation

- Definition of the format of NAF.

FILE_HEAD
·unicode of Epsilon
·Symbol Set whose symbol in unicode split by ','
·Number of States, which always starts from 1
·Initial States Set whose elements in number split by ','
·Final States Set whose elements in number split by ','
·Transform Matrix like this:
```
s\a|___a__|___b__|___c__|
 1 |s(1,a),s(1,b),s(1,c)|
 2 |s(2,a),s(2,b),s(2,c)|
......
EOF
```
* Above state in row, alphabet in column which is omitted and -1 defines the Ø as ERROR.
* Among them s(s,a) is a set of states split by '|'
* order of alphabet in column is same as symbol set row

- Sample File.
```
949
97,98,99,949
4
1,2
3
1,2,-1,-1
1,1|2,1,2|3
-1,2,-1,3
-1,-1,1|2|4,-1
```

### DFA In&Out File Regulation

- Definition of the format of DFA.

FILE_HEAD
·Symbol Set whose symbol in unicode split by ','
·Number of States, which always starts from 1
·Initial State
·Final State
·Transform Matrix like this:
```
s\a|___a__|___b__|___c__|
 1 |s(1,a),s(1,b),s(1,c)|
 2 |s(2,a),s(2,b),s(2,c)|
......
EOF
```
Above state in row, alphabet in col which is omitted and -1 defines the Ø as ERROR.

- Sample File.
```
97,98,99
4
1
3
1,2,-1
1,1,3
-1,-1,3
-1,-1,-1
```

  


