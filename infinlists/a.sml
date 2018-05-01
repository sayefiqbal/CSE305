datatype TYPE = INT of int | BOOL of bool | STRING of string | ERROR | NAME of string

datatype COMMAND = PUSH | POP | ADD | SUB | MUL | DIV | REM | NEG | SWAP | QUIT
                  | CAT | AND | OR | NOT | EQU | LTHAN

datatype 'a map = Map of ('a list)
                    | Empty
  fun pushM (item, Map m)        = Map (item::m)

datatype 'a stack = Stack of ('a list)
                    | Empty
  fun push (item, Stack s, Stack m)        =
    let
      val s = Stack (item::s)
    in
      Stack ([(s,m)])
    end
  fun pop1 (Stack (first_item::s)) = (first_item, Stack s)
  fun pop2 (Stack (first_item::second_item::s)) = (first_item, second_item, Stack s) 
  fun top (Stack (first_item::s)) = (first_item)

fun isNeg (x: int) = 
  if (x<0) then "-"^ Int.toString(Int.abs(x))
  else Int.toString(x)

fun isBool (x: bool) = 
  if(x=true) then ":true:"
  else ":false:"

fun give (INT(x)) = isNeg(x)
  | give (STRING(x)) = x 
  | give (BOOL(x)) = isBool(x)
  | give (NAME(x)) = x
  | give (ERROR) = ":error:"

fun comp [] = PUSH
  | comp ((#"p")::(#"u")::(#"s")::(#"h")::xs) = PUSH
  | comp ((#"p")::(#"o")::(#"p")::xs) = POP
  | comp ((#"a")::(#"d")::(#"d")::xs) = ADD
  | comp ((#"s")::(#"u")::(#"b")::xs) = SUB
  | comp ((#"m")::(#"u")::(#"l")::xs) = MUL
  | comp ((#"d")::(#"i")::(#"v")::xs) = DIV
  | comp ((#"r")::(#"e")::(#"m")::xs) = REM
  | comp ((#"n")::(#"e")::(#"g")::xs) = NEG
  | comp ((#"c")::(#"a")::(#"t")::xs) = CAT
  | comp ((#"a")::(#"n")::(#"d")::xs) = AND
  | comp ((#"o")::(#"r")::xs) = OR
  | comp ((#"n")::(#"o")::(#"t")::xs) = NOT
  | comp ((#"e")::(#"q")::(#"u")::(#"a")::(#"l")::xs) = EQU
  | comp ((#"l")::(#"e")::(#"s")::(#"s")::(#"T")::(#"h")::(#"a")::(#"n")::xs) = LTHAN
  | comp ((#"s")::(#"w")::(#"a")::(#"p")::xs) = SWAP
  | comp ((#"q")::(#"u")::(#"i")::(#"t")::xs) = QUIT


fun giveInt (INT(x)) = x

fun doIt (PUSH, valT, s : TYPE stack, Stack m) = push (valT, s, Stack m) 
  (*)  | doIt (POP, valT, Stack(first::xs) : TYPE stack) = Stack xs
    | doIt (ADD, valT, Stack(first::second::xs): TYPE stack) = 
          ((case first of INT(x) => 
            (case second of INT(x) => push(INT(giveInt(first) + x), Stack xs)))  
          handle _ => push(ERROR, Stack (first::second::xs)))*)
  (*)  | doIt (SUB, valT, Stack(first::second::xs): TYPE stack) = 
          ((case first of INT(x) => 
            (case second of INT(x) => push(INT(x - giveInt(first)), Stack xs)))  
          handle _ => push(ERROR, Stack (first::second::xs)))
    | doIt (MUL, valT, Stack(first::second::xs): TYPE stack) = 
          ((case first of INT(x) => 
            (case second of INT(x) => push(INT(x * giveInt(first)), Stack xs)))  
          handle _ => push(ERROR, Stack (first::second::xs)))
    | doIt (DIV, valT, Stack(first::second::xs): TYPE stack) = 
          ((case first of INT(x) => 
            (case second of INT(x) => push(INT(x div giveInt(first)), Stack xs)))  
          handle _ => push(ERROR, Stack (first::second::xs)))
    | doIt (REM, valT, Stack(first::second::xs): TYPE stack) = 
          ((case first of INT(x) => 
            (case second of INT(x) => push(INT(x mod giveInt(first)), Stack xs)))  
          handle _ => push(ERROR, Stack (first::second::xs)))
    | doIt (NEG, valT, Stack(first::xs): TYPE stack) = 
          (case first of INT(x) => push(INT(~x), Stack xs)
          | _ => push(ERROR, Stack (first::xs)))
    | doIt (SWAP, valT, Stack(first::second::xs): TYPE stack) = 
          Stack(second::first::xs)
    | doIt (CAT, valT, Stack(first::second::xs): TYPE stack) = 
          ((case first of STRING(y) => 
            (case second of STRING(x) => push(STRING(x^y), Stack xs)))
            handle _ => push(ERROR, Stack(first::second::xs)))    
    | doIt (AND, valT, Stack(first::second::xs): TYPE stack) = 
          ((case first of BOOL(y) => 
            (case second of BOOL(x) => push(BOOL(x andalso y), Stack xs)))
            handle _ => push(ERROR, Stack(first::second::xs)))  
    | doIt (OR, valT, Stack(first::second::xs): TYPE stack) = 
          ((case first of BOOL(y) => 
            (case second of BOOL(x) => push(BOOL(x orelse y), Stack xs)))
            handle _ => push(ERROR, Stack(first::second::xs))) 
    | doIt (EQU, valT, Stack(first::second::xs): TYPE stack) = 
          ((case first of INT(y) => 
            (case second of INT(x) => push(BOOL(y=x), Stack xs)))
            handle _ => push(ERROR, Stack(first::second::xs))) 
    | doIt (NOT, valT, Stack(first::xs): TYPE stack) = 
          (case first of BOOL(x) => push(BOOL(not x), Stack xs)
            | _ => push(ERROR, Stack(first::xs)))  
    | doIt (LTHAN, valT, Stack(first::second::xs): TYPE stack) = 
          ((case first of INT(y) => 
            (case second of INT(x) => push(BOOL(y>x), Stack xs)))  
          handle _ => push(ERROR, Stack (first::second::xs)))
    | doIt (QUIT, valT, Stack(xs): TYPE stack) = 
          Stack(xs)
    | doIt(TYPE, valT, Stack xs) = push(ERROR, Stack xs) *)

fun checkFirst s =
   let val c = String.sub(s,0) in
        Char.isAlpha(c) 
   end;

fun strOrName (v : string) = 
  if(String.sub(v,String.size(v)-1) = #"\"") 
  then STRING(String.substring(v,1,String.size(v)-2))
    else if checkFirst(v) then NAME(v)
      else ERROR

fun fig(v : string) = 
  case Int.fromString v of 
     SOME v => INT(v) 
     | NONE => case Bool.fromString v of 
                SOME v => BOOL(v)
                | NONE => strOrName(v)

fun split (v : string, x) = String.tokens (fn c => c = x) v

fun isLEmpty(l : string list) = 
  if(List.length(l)=0) then "hi"
  else if (List.length(l) = 1) then hd(l)
  else hd(l) ^ hd(tl l)

fun isError(l : string) = 
  if (l = "error") then ":error:"
  else l

fun readlist (infile : string) = let
  val ins = TextIO.openIn infile
  fun loop ins =
   case TextIO.inputLine ins of
      SOME line => line :: loop ins
      | NONE      => []
  in
    loop ins before TextIO.closeIn ins
  end

fun writeList ((outFile : string), Stack s : TYPE stack) = 
  let 
    val outs = TextIO.openOut outFile
    fun helper (s) = 
      case s of [] => TextIO.closeOut outs
           | (hd::xs) => (TextIO.output(outs, (give(hd)) ^ "\n"); helper (tl (s)))
    in
    helper(s)
  end 

fun interpreter (infile : string, outfile : string) =
  let
  val ins = TextIO.openIn infile
  val readL = readlist(infile)
  val s = Stack([]) : TYPE stack
  val m = Stack([]) : ((TYPE * TYPE)) stack

  fun lol (s:string) = 
    if (String.size(s) <= 3) then split(s, #" ")
    else if (String.substring(s,0,4) = "push") then ["push", String.substring(s,5,String.size(s)-5)]
    else split(s, #" ")

  fun computeIt(readL, Stack s : TYPE stack, Stack m : (TYPE * TYPE) stack) = 
    let 
    fun compute ([], Stack s: TYPE stack, Stack m : (TYPE * TYPE) stack) = writeList(outfile, Stack s)
      | compute (head::tail, Stack s : TYPE stack, Stack m : (TYPE * TYPE) stack) = 
        let 
          val lines = split(head, #"\n")
          val lines = (hd(lines))
          val split_list = lol(lines) 
          val s = s 
          val command_text = hd(split_list)
          val value = isLEmpty(tl split_list)
          val value = hd(split (value, #":"))
          val value = fig(isError(value))
          val lcommand = explode(command_text)
          val comm = comp(lcommand)
          val s = doIt(comm, value, Stack s, Stack m)
        in 
          compute(tail, s, Stack m)
        end  
    in 
      compute (readL, Stack s, Stack m)
    end 
  in
    computeIt (readL, s, m)
  end