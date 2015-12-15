grammar Bytecode;


// classe (innerClass si possono trovare ad inizio o alla fine)
classfile
	: (classDec innerClass? constantPool '{' fields* methodDeclaration+ '}' innerClass?) EOF
	;

classDec
	: classModifier? (CLASS | ENUM) ilmio=packageAndClassName ('extends' packageAndClassName)? (('implements' packageAndClassName) (',' packageAndClassName)*)?
	;
	
CLASS:'class';
ENUM: 'enum';

classModifier
	: 'public'
	| 'static'
	| 'final'
	| 'abstract'
	;
	
innerClass
	: 'InnerClasses:' ((('static'|'public')* ('= '|ref|'of')+ | ref) ';')+
	;


fields
	: fieldModifier* type fieldName';'
	;

fieldModifier
	:	classModifier
	|	'protected'
	|	'private'
	;

fieldName: packageAndClassName;


//costant pool
constantPool
	: 'Constant pool:' tableEntries
	;
	

tableEntries: tableEntry+;

//riga della constantpool
tableEntry
	: ref '= ' constantAndInfo
	;

constantAndInfo
	: 'Class' ref 
	| 'Fieldref' ref'.'ref
	| 'Methodref' ref'.'ref
	| 'InterfaceMethodref' ref'.'ref
	|  STRING ref
	| 'Integer' num
	| 'Float' DEC
	| 'Long' num'l'
	| 'Double' DEC
	| 'NameAndType' ref':'ref
	| 'Utf8' ((Identifier|identifierExtended)|NAT|'%'|';'| '(' | ')'|'/' | '-' | '<' | '>' | ';' | '[' | ']' 
		| '.' | ':' | '\'' | '!' | '?' | '^' |'\\'|',')* //si puo' verificare che sia vuoto
	;
	

// -------- metodo, ogni campo statico della classe verra' creato un metodo "static {};"
methodDeclaration
	:	methodModifier* (methodHeader|'{}') ';' methodBody
	;

methodModifier
	:	fieldModifier
	|	'abstract'
	|	'synchronized'
	|	'native'
	|	'strictfp'
	;

methodHeader
	:	result? methodDeclarator throws_? //i costruttori non hanno tipo di ritorno esplicito (result?)
	;

// se non si fa questo strano giro nel caso dei costruttori al posto del result viene matchato il nome del costruttore invece del tipo di ritorno
result
	: 	{ ! _input.LT(1).getText().substring(_input.LT(1).getText().length() - 1).equals("(")}? type 
	|	'void'
	;
	
type
	:	primitiveType
	|	referenceType
	;

primitiveType
	:	numericType
	|	'boolean'
	;
	
numericType
	:	integralType
	|	floatingPointType
	;

integralType
	:	'byte'
	|	'short'
	|	'int'
	|	'long'
	|	'char'
	;

floatingPointType
	:	'float'
	|	'double'
	;


referenceType//classOrInterfaceType
	:   packageAndClassName
	;
	
packageAndClassName
	:	((Identifier|identifierExtended) '['* ']'*)
	|   packageAndClassName '.' ((Identifier|identifierExtended) '['* ']'*)
	;	

methodDeclarator
	:	methodName '(' formalParameters? ')'
	;

methodName: packageAndClassName;


formalParameters
	:	formalParameter (',' formalParameter)*
	;

formalParameter
	:	'final'? (type |THIS)
	;

throws_
	:	'throws' exceptionTypeList
	;

exceptionTypeList
	:	exceptionType (',' exceptionType)*
	;

exceptionType
	:	packageAndClassName
	;
	
methodBody
	: 'Code:' instructionLine+ 
	;


// istruzioni 
instructionLine : INDEX instruction ((NAT? (',')? num?) | ref);

//'astore' ('_'NAT)?

instruction
	: 'aconst_null' #Const
	| 'aload' #Load 
	|  ALOAD #Load
	| 'areturn' #Return
	| 'astore' #Store
	|  ASTORE #Store  
	| 'athrow' #Athrow
	| 'dup' #Dup
	| 'getfield' #Get
	| 'getstatic'  #Get
	| 'goto'  #Goto //indirizzo per il goto e' dato come offset da sommare/sottrarre all'indirizzo nel program counter 
	| 'iadd' #Operation
	| 'iconst_m1'#Const
	| 'iconst_0' #Const
	| 'iconst_1' #Const
	| 'iconst_2' #Const
	| 'iconst_3' #Const
	| 'iconst_4' #Const
	| 'iconst_5' #Const
	| 'bipush'  #Const
	| 'idiv' #Operation
	| 'if_acmpeq' #If
	| 'if_acmpne' #If
	| 'if_icmpeq' #If 
	| 'if_icmpne' #If
	| 'if_icmplt' #If 
	| 'if_icmpge' #If 
	| 'if_icmpgt' #If 
	| 'if_icmple' #If 
	| 'ifeq'  #If
	| 'ifne'  #If
	| 'iflt'  #If
	| 'ifle'  #If
	| 'ifgt'  #If
	| 'ifge'  #If
	| 'ifnonnull' #If 
	| 'ifnull' #If
	| 'iinc'  #Iinc
	| 'iload' #Load 
	| ILOAD #Load
	| 'imul' #Operation
	| 'invokespecial' #Invoke 
	| 'invokestatic'  #Invoke
	| 'invokevirtual' #Invoke 
	| 'irem' #Operation
	| 'ireturn' #Return
	| 'istore'  #Store
	| ISTORE #Store
	| 'isub' #Operation
	| 'ldc' #Const
	| 'ldc_w' #Const
	| 'ldc2_w' #Const // ldc2_w is used for loading category 2 constants (doubles and longs)
	| 'monitorenter' #Monitorenter 
	| 'monitorexit' #Monitorexit
	| 'new'  #New
	| 'pop' #Pop
	| 'putfield'  #Put
	| 'putstatic' #Put
	| 'anewarray' #NotImplemented // riguarda array
	| 'aastore' #NotImplemented // riguarda array
	| 'aaload' #NotImplemented  // riguarda array
	| 'f2l'  #Conversion //conversione float in long
	| 'lmul' #Operation //moltiplicazione long
	| 'return' #Return
	;

INDEX: NAT':';

ref : REFNUM
	;
	
ALOAD: 'aload_' NAT;
ILOAD: 'iload_'NAT;
ISTORE:'istore_'NAT ;
ASTORE:'astore_'NAT;

 //numerazione della constant pool parte da 1
REFNUM: '#'[1-9][0-9]*;

num: NAT | INT;

//naturali
NAT : [0-9]+; 

//interi
INT : '-'? NAT;

//per float e double
DEC: '-'? [0-9]*'.'[0-9]*;

THIS: 'this';

STRING: 'String';

// identificatori Java (NON spostare da qui)
Identifier
	:	JavaLetter JavaLetterOrDigit*
	;

identifierExtended : Identifier | STRING | THIS ;
	

fragment
JavaLetter
	:	[a-zA-Z$_] // these are the "java letters" below 0xFF
	|	// covers all characters above 0xFF which are not a surrogate
		~[\u0000-\u00FF\uD800-\uDBFF]
		{Character.isJavaIdentifierStart(_input.LA(-1))}?
	|	// covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
		[\uD800-\uDBFF] [\uDC00-\uDFFF]
		{Character.isJavaIdentifierStart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
	;

fragment	
JavaLetterOrDigit
	:	[a-zA-Z0-9$_] // these are the "java letters or digits" below 0xFF
	|	// covers all characters above 0xFF which are not a surrogate
		~[\u0000-\u00FF\uD800-\uDBFF]
		{Character.isJavaIdentifierPart(_input.LA(-1))}?
	|	// covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
		[\uD800-\uDBFF] [\uDC00-\uDFFF]
		{Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
	;

// skip

//before "constant pool:"
CLASSFILE: 'Classfile ' ~[\r\n]+ {skip();};
LAST: 'Last modified '  [0-9|a-z|A-Z|','|'\-'|';'|' ']+ ' size ' [0-9]+ ' bytes'  '\r'? '\n' {skip();};
MD5: 'MD5 checksum ' [0-9|a-z]+  '\r'? '\n' {skip();};
COMPILED: 'Compiled from ' ['"'|a-z|A-Z|'.'|0-9]+  '\r'? '\n'{skip();};
ENCLOSINGMETHOD: 'EnclosingMethod: ' ['#'|0-9|'.']+ {skip();};
SOURCE: 'SourceFile: ' ['"'|a-z|A-Z|'.'|0-9]+  '\r'? '\n'{skip();};
MINOR: 'minor version: ' [0-9]+  '\r'? '\n'{skip();};
MAJOR: 'major version: ' [0-9]+  '\r'? '\n'{skip();};
// ci si puo' trovare nella situazione in cui dopo 'flags:' non c'e' nulla (non so perche' ma e' capitato)
FLAGS: 'flags:' [ A-Z|'_'|',']*  '\r'? '\n'{skip();};
CONSTANTVALUE: 'ConstantValue:' [ A-Z|a-z|0-9]* {skip();};


//before "Code:"
DESCRIPTOR: 'descriptor:'  ~[\r\n]* {skip();};

//after "Code:"


EXCEPTIONTABLE: 'Exception table:'  '\r'? '\n'{skip();};
FROMTO: 'from' ' '+ 'to' ' '+ 'target' ' '+ 'type' '\r'? '\n'{skip();};
FROMTONUMBERS: NAT [' '|'/t']+ NAT [' '|'/t']+ NAT [' '|'/t']+ [a-z]+  '\r'? '\n'{skip();};
LINENUMBERTABLE: 'LineNumberTable:'  '\r'? '\n' {skip();};
LINENUMBERTABLECONTENT: 'line ' [0-9]+ ': ' [0-9]+  '\r'? '\n'{skip();}; 
LOCALVARIABLETABLE: 'LocalVariableTable:'  '\r'? '\n' {skip();};
LOCALVARIABLECONTENT: 'Start  Length  Slot  Name   Signature' '\r'? '\n'{skip();}; 
LOCALVARIABLELINE: [0-9]+ [' '|'/t']+ [0-9]+ [' '|'/t']+ [0-9]+ [' '|'/t']+  ~[\r\n]+ {skip();}; 
STACKMAPTABLE: 'StackMapTable: number_of_entries = ' [0-9]+ '\r'? '\n'{skip();};
FRAMETYPE: 'frame_type = ' [0-9]+ [' '|'*'|'/'|A-Z|a-z|'_'|0-9]*  '\r'? '\n'{skip();};
OFFSETDELTA: 'offset_delta = ' [0-9]+ [' '|'*'|'/'|A-Z|a-z]*  '\r'? '\n'{skip();};
LOCALS: 'locals = ' ~[\r\n]* {skip();};
STACK: 'stack =' ~[\r\n]*  {skip();};
STACKLOCALARGSSIZE: 'stack=' ( |[0-9]+ ', locals='[0-9]+ ', args_size='[0-9]+)  '\r'? '\n'{skip();};
EXCEPTION: 'Exceptions:'  '\r'? '\n'{skip();};
THROWS: 'throws ' [A-Z|a-z|'.']+  '\r'? '\n'{skip();};
//INNER: 'InnerClasses:'  '\r'? '\n'{skip(); System.out.println("skippato INNERCLASS");};
//INNERCLASSESREF: REFNUM';' {skip(); System.out.println("skippato INNERCLASSREF");};
//STATICINNERCLASS: 'static #'[0-9]+ ~[\r\n]* {skip(); System.out.println("skippato STATICINNERCLASS");};
//STATICINNERCLASS2: 'public #'[0-9]+ ~[\r\n]* {skip(); System.out.println("skippato STATICINNERCLASS");};


//general skip

WS : [ \r\n\t] -> skip // skip spaces, tabs, newlines
   ; 		
   
COMMENT
    :   '/*' .*? '*/' {skip();}
    ;

LINE_COMMENT
    :   '//' ~[\r\n]* {skip();}
    ;
    

